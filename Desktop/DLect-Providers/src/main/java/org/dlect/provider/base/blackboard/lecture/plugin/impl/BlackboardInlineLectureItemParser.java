/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin.impl;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableLectureDownload;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardLectureRecordingItem;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardLectureRecordingPage;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubjectMapItem;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureItemParser;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureMapping;

public class BlackboardInlineLectureItemParser extends BlackboardLectureItemParser {

    private static final String ECHO360_LINK_TYPE = "resource/x-apreso";
    private static final Pattern ECHO360_LECTURE_ID = Pattern.compile(".*2F([a-z0-9\\-]+)$");

    private final BlackboardLectureCustomiser lectureCustomiser;
    private final BlackboardLecturePageParser lecturePageParser;

    private final Set<String> exploredPages = Sets.newHashSet();

    public BlackboardInlineLectureItemParser(BlackboardLectureCustomiser lectureCustomiser, BlackboardLecturePageParser lecturePageParser) {
        this.lectureCustomiser = lectureCustomiser;
        this.lecturePageParser = lecturePageParser;
    }

    public BlackboardInlineLectureItemParser(BlackboardHttpClient httpClient, BlackboardLectureCustomiser lectureCustomiser) {
        this(lectureCustomiser, new BlackboardLecturePageParserImpl(httpClient));
    }

    @Override
    public BlackboardLectureMapping getLecturesIn(URI baseUri, BlackboardSubjectMapItem bsmi, ImmutableSemester sem,
                                                  ImmutableSubject s) throws DLectException {
        try {
            if (ECHO360_LINK_TYPE.equalsIgnoreCase(bsmi.getLinkType())) {
                URI normal = normaliseUri(baseUri, bsmi.getViewUrl());

                if (!exploredPages.contains(normal.toString())) {
                    BlackboardLectureRecordingPage page = lecturePageParser.getPageFor(normal);

                    BlackboardLectureMapping results = processPage(page, sem, s);

                    exploredPages.add(normal.toString());

                    return results;
                }
            }
            return BlackboardLectureMapping.empty();
        } catch (IOException ex) {
            throw new DLectException(DLectExceptionCause.NO_CONNECTION, ex);
        }
    }

    protected BlackboardLectureMapping processPage(BlackboardLectureRecordingPage page,
                                                   ImmutableSemester semester,
                                                   ImmutableSubject subject) throws DLectException {
        Set<ImmutableLecture> lectures = Sets.newHashSetWithExpectedSize(page.getItems().size());
        Multimap<ImmutableLecture, ImmutableStream> lectureStreams = HashMultimap.create(page.getItems().size(), 2);
        for (BlackboardLectureRecordingItem blri : page.getItems()) {
            Optional<Date> opDate = lectureCustomiser.getLectureTime(blri.getUrl(), blri.getTitle(), blri.getCaptureDate());

            if (opDate.isPresent()) {
                Date d = opDate.get();
                Collection<ImmutableStream> streams = lectureCustomiser.getLectureStream(blri.getUrl(), blri.getTitle(), d, semester, subject);

                Map<DownloadType, ImmutableLectureDownload> downloads = getDownloadType(blri.getUrl());

                ImmutableLecture l = new ImmutableLecture(getContentIdFromUrl(blri.getUrl()), d, false, streams, downloads);

                lectureStreams.putAll(l, streams);
                lectures.add(l);
            }
        }
        return new BlackboardLectureMapping(lectures, lectureStreams);
    }

    private static String getContentIdFromUrl(URI url) {
        String uri = url.toString();

        Matcher m = ECHO360_LECTURE_ID.matcher(uri);
        if (m.find()) {
           return  m.group(1);
        } else {
            throw new IllegalArgumentException("Failed to parse URL: " + uri);
        }
    }

    private static URI normaliseUri(URI baseUri, String viewUrl) throws IOException {
        try {
            URI u = new URL(baseUri.toURL(), viewUrl).toURI();

            String scheme = u.getScheme();
            String userInfo = u.getUserInfo();
            String host = u.getHost();
            int port = u.getPort();
            String path = u.getPath();
            String query = u.getQuery();

            return new URI(scheme, userInfo, host, port, path, query, "");
        } catch (MalformedURLException | URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

}
