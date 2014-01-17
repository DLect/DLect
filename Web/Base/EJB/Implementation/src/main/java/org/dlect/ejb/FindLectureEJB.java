/*
 * This file is part of DLect. DLect is a suite of code that facilitates the downloading of lecture recordings.
 *
 * Copyright © 2014 Lee Symes.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dlect.ejb;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.dlect.db.DataToDatabaseEJB;
import org.dlect.db.DatabaseMerger;
import org.dlect.ejb.internal.provder.lecture.LectureListingProvider;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.internal.beans.LoginCredentialBean;
import org.dlect.internal.data.LectureData;
import org.dlect.internal.data.StreamData;
import org.dlect.internal.data.SubjectData;
import org.dlect.internal.data.SubjectData_;
import org.dlect.internal.data.merge.PartialLecture;
import org.dlect.internal.data.merge.PartialLectureContent;
import org.dlect.internal.data.merge.PartialStream;
import org.dlect.internal.data.merge.config.LectureContentPartialDataConfig;
import org.dlect.internal.data.merge.config.LecturePartialDataConfig;
import org.dlect.internal.data.merge.config.StreamPartialDataConfig;
import org.dlect.internal.object.ProviderLectureData;
import org.dlect.log.EJBs;
import org.dlect.object.ResultType;

/**
 *
 * @author lee
 */
@Stateless
public class FindLectureEJB implements FindLectureEJBLocal {

    @Inject
    private DataToDatabaseEJB helper;

    @Inject
    private DatabaseMerger merger;

    @Inject
    private Instance<LoginCredentialBean> loginCreds;

    @Override
    public void findLecturesFor(long subjectId) throws DLectException {
        SubjectData subD = getSubject(subjectId);

        ProviderLectureData lecturesIn = getLectureData(subD);

        Map<PartialLecture, LectureData> lectureMapping = mergeLecture(subD, lecturesIn);
        Map<PartialStream, StreamData> streamMapping = mergeStream(subD, lecturesIn);
        
        interlaceLectureStream(lecturesIn, lectureMapping, streamMapping);

        helper.flush();
    }

    public ProviderLectureData getLectureData(SubjectData subD) throws DLectException, DLectException {
        LectureListingProvider llp = loginCreds.get().getProvider().getLectureListingProvider();
        if (llp == null) {
            throw CommonExceptionBuilder.getIllegalReturnTypeException("Provider returned a null LectureListingProvider",
                                                                       loginCreds.get().getProvider(), llp);
        }
        ProviderLectureData lecturesIn = llp.getLecturesIn(subD);
        if (lecturesIn == null) {
            throw CommonExceptionBuilder.getIllegalReturnTypeException("Provider returned a null ProviderLectureData for " + subD,
                                                                       llp, lecturesIn);
        }
        return lecturesIn;
    }

    public void interlaceLectureStream(ProviderLectureData lecturesIn, Map<PartialLecture, LectureData> lectureMapping,
                                       Map<PartialStream, StreamData> streamMapping) {
        for (Entry<PartialLecture, Collection<PartialStream>> entry : lecturesIn.getLectureStreamMapping().asMap().entrySet()) {
            LectureData ld = lectureMapping.get(entry.getKey());

            Collection<PartialStream> e = entry.getValue();
            Set<StreamData> streamD = Sets.newHashSet();

            for (PartialStream ps : e) {
                streamD.add(streamMapping.get(ps));
            }
            ld.setStreamData(streamD);
        }
    }

    public Map<PartialStream, StreamData> mergeStream(SubjectData subD, ProviderLectureData lecturesIn) {
        Map<PartialStream, StreamData> streamMapping = Maps.newHashMap();
        StreamPartialDataConfig streamPDC = new StreamPartialDataConfig(subD);
        for (PartialStream stream : lecturesIn.getStreams()) {
            StreamData ld = merger.mergePartialData(streamPDC, stream);

            streamMapping.put(stream, ld);
        }
        return streamMapping;
    }

    public Map<PartialLecture, LectureData> mergeLecture(SubjectData subD, ProviderLectureData lecturesIn) {
        Map<PartialLecture, LectureData> lectureMapping = Maps.newHashMap();
        LecturePartialDataConfig lecturePDC = new LecturePartialDataConfig(subD);
        for (PartialLecture lecture : lecturesIn.getLectures()) {
            LectureData ld = merger.mergePartialData(lecturePDC, lecture);

            lectureMapping.put(lecture, ld);

            LectureContentPartialDataConfig lectureContentPDC = new LectureContentPartialDataConfig(ld);
            for (PartialLectureContent pLecCont : lecture.getLectureContent()) {
                merger.mergePartialData(lectureContentPDC, pLecCont);
            }
        }
        return lectureMapping;
    }

    public SubjectData getSubject(long subjectId) throws DLectException {
        SubjectData subD = helper.getEqualTo(SubjectData_.subjectId, subjectId);
        EJBs.LOG.error("SubjectData: {}", subD);
        if (!loginCreds.get().getListing().contains(subD)) {
            throw DLectExceptionBuilder.build(ResultType.BAD_INPUT, "Cannot access subject.");
        }
        return subD;
    }

}
