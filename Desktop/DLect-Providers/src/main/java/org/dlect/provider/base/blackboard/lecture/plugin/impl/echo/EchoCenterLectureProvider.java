/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin.impl.echo;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubjectMapItem;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureItemParser;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureMapping;

/**
 *
 * @author lee
 */
public class EchoCenterLectureProvider extends BlackboardLectureItemParser {

    protected static final String LTI_LINK_TYPE = "resource/x-osc-basiclti";

    private final EchoCenterLTIAccessor accessor;
    private final EchoCenterLectureCustomiser lectureCustomiser;

    public EchoCenterLectureProvider(BlackboardHttpClient httpClient, EchoCenterLectureCustomiser lectureCustomiser) {
        this.accessor = new EchoCenterLTIAccessor(httpClient);
        this.lectureCustomiser = lectureCustomiser;
    }

    @Override
    public BlackboardLectureMapping getLecturesIn(URI baseUri,
                                                  BlackboardSubjectMapItem bsmi,
                                                  ImmutableSemester semester,
                                                  ImmutableSubject subject) throws DLectException {
        if (LTI_LINK_TYPE.equalsIgnoreCase(bsmi.getLinkType())) {
            EchoCenterJsonParser parser = accessor.checkEchoCenter(baseUri, bsmi);
            if (parser != null) {
                List<EchoCenterLecture> psj = parser.parseSectionJSON();

                Set<ImmutableLecture> lectures = Sets.newHashSetWithExpectedSize(psj.size());
                Multimap<ImmutableLecture, ImmutableStream> lectureStreams = HashMultimap.create(psj.size(), 2);

                for (EchoCenterLecture ecl : psj) {
                    Collection<ImmutableStream> streams = lectureCustomiser.getStreamsFor(ecl, semester, subject);

                    URI base;
                    try {
                        base = new URI(ecl.getBaseUrl());
                    } catch (URISyntaxException ex) {
                        throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE,
                                                 "Failed to convert the base Url of the following "
                                                 + "lecture into a uri object: " + ecl, ex);
                    }
                    ImmutableLecture l = new ImmutableLecture(ecl.getId(), ecl.getStartTime(), true, streams, getDownloadType(base, false));

                    lectures.add(l);
                    lectureStreams.putAll(l, streams);
                }
                return new BlackboardLectureMapping(lectures, lectureStreams);
            }
        }
        return BlackboardLectureMapping.empty();
    }

}
