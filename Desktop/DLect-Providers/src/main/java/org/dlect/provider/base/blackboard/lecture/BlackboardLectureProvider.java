/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableLectureDownload;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.logging.ProviderLogger;
import org.dlect.model.formatter.DownloadType;
import org.dlect.provider.ImmutableSubjectData;
import org.dlect.provider.LectureProvider;
import org.dlect.provider.base.blackboard.BlackboardLectureCustomiser;
import org.dlect.provider.base.blackboard.helper.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlParser;

import static org.dlect.provider.base.blackboard.helper.BlackboardHttpClient.DLECT_IDENTIFIER;

/**
 *
 * @author lee
 */
public class BlackboardLectureProvider implements LectureProvider {

    private static final ImmutableMap<DownloadType, String> DOWNLOAD_TYPE_EXTENSIONS = ImmutableMap
            .<DownloadType, String>builder()
            .put(DownloadType.AUDIO, "mp3")
            .put(DownloadType.VIDEO, "m4v")
            .build();

    private static final String ECHO360_LINK_TYPE = "resource/x-apreso";

    private final URI baseUrl;
    private final BlackboardHttpClient httpClient;
    private final BlackboardLectureCustomiser lectureCustomiser;
    private final BlackboardXmlParser xmlParser;
    private final BlackboardLecturePageParser lecturePageParser;

    public BlackboardLectureProvider(URI baseUrl,
                                     BlackboardLectureCustomiser lectureCustomiser,
                                     BlackboardHttpClient httpClient,
                                     BlackboardXmlParser xmlParser) {
        this(baseUrl, httpClient, lectureCustomiser, xmlParser, new BlackboardLecturePageParserImpl(httpClient));
    }

    public BlackboardLectureProvider(URI baseUrl,
                                     BlackboardHttpClient httpClient,
                                     BlackboardLectureCustomiser lectureCustomiser,
                                     BlackboardXmlParser xmlParser,
                                     BlackboardLecturePageParser lecturePageParser) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.lectureCustomiser = lectureCustomiser;
        this.xmlParser = xmlParser;
        this.lecturePageParser = lecturePageParser;
    }

    protected Multimap<ImmutableLecture, ImmutableStream> fillMultimap(Collection<ImmutableLecture> lectures) {
        Multimap<ImmutableLecture, ImmutableStream> ls = HashMultimap.create();
        for (ImmutableLecture il : lectures) {
            ls.putAll(il, il.getStreams());
        }
        return ls;
    }

    protected Map<DownloadType, ImmutableLectureDownload> getDownloadType(URI url) throws IOException {
        String urlPostfix = "%2Fmedia."; // <= URLEncoded.encoded("/media.")

        Map<DownloadType, ImmutableLectureDownload> map = Maps.newHashMap();

        for (Entry<DownloadType, String> entry : DOWNLOAD_TYPE_EXTENSIONS.entrySet()) {
            DownloadType dt = entry.getKey();
            String ext = entry.getValue();

            try {
                map.put(dt, new ImmutableLectureDownload(
                        new URL(url.toString() + urlPostfix + ext).toURI(), ext, false, false));
            } catch (MalformedURLException | URISyntaxException ex) {
                throw new IOException(ex);
            }
        }

        return map;
    }

    @Override
    public ImmutableSubjectData getLecturesIn(ImmutableSubject s) throws DLectException {
        String subjectContentLocation = "courseMap?v=1&language=en_GB&ver=" + DLECT_IDENTIFIER + "&course_id=" + s.getId();

        try (InputStream contentStream = httpClient.doGet(new URL(baseUrl.toURL(), subjectContentLocation).toURI())) {
            BlackboardSubjectContentListing listing = xmlParser.parseSubjectContent(contentStream);

            URI baseUri = URI.create(listing.getRootUrl());

            List<String> exploredPages = Lists.newArrayList();

            Set<ImmutableStream> streams = Sets.newHashSet(s.getStreams());
            streams.addAll(lectureCustomiser.getLectureStreamsFor(s));
            Set<ImmutableLecture> lectures = Sets.newHashSet(s.getLectures());
            Multimap<ImmutableLecture, ImmutableStream> lectureStreams = fillMultimap(lectures);

            for (BlackboardSubjectMapItem bsmi : listing.getAllItems()) {
                if (ECHO360_LINK_TYPE.equals(bsmi.getLinkType()) && bsmi.isAvaliable()) {
                    URI normal = BlackboardLectureUtils.normaliseUri(baseUri, bsmi.getViewUrl());

                    if (!exploredPages.contains(normal.toString())) {
                        BlackboardLectureRecordingPage page = lecturePageParser.getPageFor(normal);

                        processPage(page, lectures, lectureStreams, s);
                    }
                }
            }

            return new ImmutableSubjectData(lectureStreams, lectures, streams);
        } catch (IOException ex) {
            throw new DLectException(DLectExceptionCause.NO_CONNECTION, ex);
        } catch (URISyntaxException ex) {
            throw new DLectException(DLectExceptionCause.INVALID_DATA_FORMAT, ex);
        }
    }

    protected void processPage(BlackboardLectureRecordingPage page,
                               Set<ImmutableLecture> lectures,
                               Multimap<ImmutableLecture, ImmutableStream> lectureStreams,
                               ImmutableSubject subject) throws DLectException {
        for (BlackboardLectureRecordingItem blri : page.getItems()) {
            try {

                Optional<Date> opDate = lectureCustomiser.getLectureTime(blri.getUrl(), blri.getTitle(), blri.getCaptureDate());

                if (opDate.isPresent()) {
                    Date d = opDate.get();
                    Collection<ImmutableStream> streams = lectureCustomiser.getLectureStream(blri.getUrl(), blri.getTitle(), d, subject);

                    Map<DownloadType, ImmutableLectureDownload> downloads = getDownloadType(blri.getUrl());

                    ImmutableLecture l = new ImmutableLecture(blri.getContentId(), d, false, streams, downloads);

                    lectureStreams.putAll(l, streams);
                    lectures.add(l);
                }
            } catch (IOException ex) {
                ProviderLogger.LOGGER.error("Failed to process page item " + blri, ex);
                throw new DLectException(DLectExceptionCause.INVALID_DATA_FORMAT, ex);
            }
        }
    }

}
