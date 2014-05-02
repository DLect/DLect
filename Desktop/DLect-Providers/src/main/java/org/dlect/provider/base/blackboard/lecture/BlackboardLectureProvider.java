/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.provider.LectureProvider;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlParser;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubjectContentListing;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubjectMapItem;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureItemParser;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureItemParserBuilder;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureMapping;
import org.dlect.provider.objects.ImmutableSubjectData;

import static org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient.DLECT_SHORT_IDENTIFIER;

/**
 *
 * @author lee
 */
public class BlackboardLectureProvider implements LectureProvider {

    protected static final String SUBJECT_BASE_URL = "courseMap?v=1&language=en_GB&ver=" + DLECT_SHORT_IDENTIFIER + "&course_id=";

    private final URI baseUrl;
    private final BlackboardHttpClient httpClient;
    private final BlackboardXmlParser xmlParser;
    private final BlackboardLectureItemParserBuilder builder;
    private final BlackboardStreamProvider streamProvider;

    public BlackboardLectureProvider(URI baseUrl,
                                     BlackboardHttpClient httpClient,
                                     BlackboardXmlParser xmlParser,
                                     BlackboardLectureItemParserBuilder builder,
                                     BlackboardStreamProvider streamProvider) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.xmlParser = xmlParser;
        this.builder = builder;
        this.streamProvider = streamProvider;
    }

    protected Multimap<ImmutableLecture, ImmutableStream> fillMultimap(Collection<ImmutableLecture> lectures) {
        Multimap<ImmutableLecture, ImmutableStream> ls = HashMultimap.create();
        for (ImmutableLecture il : lectures) {
            ls.putAll(il, il.getStreams());
        }
        return ls;
    }

    @Override
    public ImmutableSubjectData getLecturesIn(ImmutableSemester sem, ImmutableSubject s) throws DLectException {
        String subjectContentLocation = SUBJECT_BASE_URL + s.getId();

        try (InputStream contentStream = httpClient.doGet(new URL(baseUrl.toURL(), subjectContentLocation).toURI())) {
            BlackboardSubjectContentListing listing = xmlParser.parseSubjectContent(contentStream);

            URI baseUri = URI.create(listing.getRootUrl());

            return parseLectureItems(listing, baseUri, sem, s);

        } catch (IOException ex) {
            throw new DLectException(DLectExceptionCause.NO_CONNECTION, ex);
        } catch (URISyntaxException ex) {
            throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, ex);
        }
    }

    protected ImmutableSubjectData parseLectureItems(BlackboardSubjectContentListing listing, URI baseUri,
                                                     ImmutableSemester sem, ImmutableSubject s) throws IOException,
                                                                                                       DLectException {
        Set<BlackboardLectureItemParser> parsers = ImmutableList.copyOf(builder.build(httpClient));

        Set<ImmutableStream> streams = Sets.newHashSet(s.getStreams());
        streams.addAll(streamProvider.getLectureStreamsFor(sem, s));
        Set<ImmutableLecture> lectures = Sets.newHashSet(s.getLectures());
        Multimap<ImmutableLecture, ImmutableStream> lectureStreams = fillMultimap(lectures);

        for (BlackboardLectureItemParser p : parsers) {
            for (BlackboardSubjectMapItem bsmi : listing.getAllItems()) {
                BlackboardLectureMapping lm = p.getLecturesIn(baseUri, bsmi, sem, s);
                if (lm != null) {
                    lectures.addAll(lm.getLectures());
                    lectureStreams.putAll(lm.getLectureStreamMapping());
                    streams.addAll(lm.getLectureStreamMapping().values());
                }
            }
        }
        return new ImmutableSubjectData(lectureStreams, lectures, streams);
    }

}
