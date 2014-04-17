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
package org.dlect.ejb.internal.provder.impl.blackboard.au.uq;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.dlect.ejb.internal.provder.impl.blackboard.BlackboardUniversityCustomisation;
import org.dlect.ejb.internal.provder.impl.blackboard.au.uq.rota.builders.UQRotaStorage;
import org.dlect.ejb.internal.provder.impl.blackboard.echo360.BlackboardEcho360LectureDataCustomiser;
import org.dlect.ejb.internal.provder.impl.blackboard.echo360.Echo360BlackboardHelper;
import org.dlect.except.DLectException;
import org.dlect.export.Lecture;
import org.dlect.export.Semester;
import org.dlect.export.Subject;
import org.dlect.internal.data.merge.PartialLectureContent;
import org.dlect.internal.data.merge.PartialLectureWithStream;
import org.dlect.internal.data.merge.PartialSemester;
import org.dlect.internal.data.merge.PartialStream;
import org.dlect.internal.data.merge.PartialSubjectWithSemester;
import org.dlect.log.Customisers;
import org.dlect.object.CommonDataType;
import org.dlect.object.Pair;
import org.dlect.plugin.provider.australia.uq.rota.UQCampus;
import org.dlect.provider.common.blackboard.helpers.BlackboardMobileHelper;
import org.dlect.provider.common.blackboard.xml.BlackboardCourseMapItem;

import static org.dlect.helpers.RegularExpressionHelper.findGroup;
import static org.dlect.helpers.RegularExpressionHelper.findMatcher;

@Stateless
public class UQBlackboardUniversityCustomisation implements BlackboardUniversityCustomisation, BlackboardEcho360LectureDataCustomiser {

    /**
     * This regex defines the following groups:
     * - 1: Campus Building-Room
     * - 2: Campus
     * - 3: Building
     * - 4: Room
     */
    private static final Pattern UQ_LECTOPIA_ROOM_INFO_REGEXP = Pattern.compile("Rm: (([A-Z]+) .*?([1-9][0-9]*)-(0*\\S*))");

    private static final List<Pair<String, CommonDataType>> UQ_LECTOPIA_DATA_TYPES = ImmutableList.of(Pair.of("m4v", CommonDataType.VIDEO),
                                                                                                      Pair.of("mp3", CommonDataType.AUDIO));

    private static final ImmutableSet<String> codes = ImmutableSet.of(BlackboardMobileHelper.toUniversityCode(921));

//    @Override
    public List<PartialStream> getAllStreamData(Semester semester, Subject subject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
    public Set<PartialLectureContent> getDownloadsFor(Semester semester, Subject subject, Lecture l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
    public Optional<PartialLectureWithStream> getLectureDataFor(Semester semester, Subject subject, URL rootUrl, BlackboardCourseMapItem item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLectureLocation(Semester semester, Subject subject, Date d, String name) throws DLectException {
        return findGroup(UQ_LECTOPIA_ROOM_INFO_REGEXP, name, 1);
    }

    @Override
    public List<String> getStreamsFor(Semester semester, Subject subject, Date d, String name) throws DLectException {
        Matcher m = findMatcher(UQ_LECTOPIA_ROOM_INFO_REGEXP, name);
        UQCampus campus = UQCampus.getMatching(m.group(2));
        String building = m.group(3);
        String room = m.group(4);

        List<String> streamIds = UQStreamBuilder.getStreamsFor(storage, semester.getCode(), subject.getName(), d, campus, building, room);
        return streamIds;
    }

    @PostConstruct
    public void init() {
        lectureHelper = new Echo360BlackboardHelper(storage, this);
    }

    private Echo360BlackboardHelper lectureHelper;

    @Inject
    private UQRotaStorage storage;
//
//    @Override
//    public List<PartialStream> getAllStreamData(Semester semester, Subject subject) {
//        return UQStreamBuilder.getAllStreamsFor(storage, semester.getCode(), subject.getName());
//    }
//
//    @Override
//    public Set<PartialLectureContent> getDownloadsFor(Semester semester, Subject subject, Lecture l) {
//        Set<PartialLectureContent> content = Sets.newHashSet();
//        if (l.getContents() == null) {
//            return content;
//        }
//        String baseUrl = null;  
//        for (LectureContent lc : l.getContents()) {
//            if (CommonDataType.BASE_URL.sameAs(lc.getType())) {
//                baseUrl = lc.getUrl();
//                break;
//            }
//        }
//
//        String s;
//        s = UQDownloadContentHelper.getBaseDownloadUrlFrom(baseUrl);
//
//        for (Pair<String, CommonDataType> pair : UQ_LECTOPIA_DATA_TYPES) {
//
//        }
//
//        return null;
//    }
//
//    @Override
//    public Optional<PartialLectureWithStream> getLectureDataFor(Semester semester, Subject subject, URL rootUrl, BlackboardCourseMapItem item) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

//    @Override
//    public Optional<PartialLectureWithStream> getLectureDataFor(Semester semester, Subject subject, URL rootUrl,
//                                                                BlackboardCourseMapItem item) {
//        return lectureHelper.getLectureDataFor(semester, subject, rootUrl, item);
//    }
//    @Override
    public Optional<PartialStream> getStreamDataFor(Semester semester, Subject subject, String streamId) {
        return UQStreamBuilder.getStreamFor(storage, semester.getCode(), subject.getName(), streamId);
    }

    @Override
    public Set<String> getUniversityCode() {
        // copyOf checks for immutable set.
        return ImmutableSet.copyOf(codes);
    }

    @Override
    public Optional<PartialSemester> getSemesterFor(String semCode) {
        try {
            return Optional.fromNullable(UQSemesterBuilder.getSemesterFor(storage, Integer.parseInt(semCode)));
        } catch (DLectException | NumberFormatException ex) {
            Customisers.LOG.error("Failed to get semester " + semCode, ex);
            return Optional.absent();
        }
    }

    @Override
    public Optional<PartialSubjectWithSemester> getSubjectDataFor(String bbid, String name, String courseid, Date enrollmentdate) {
        try {
            // This method automatically caches the UQRotaSubject, so there is no need for cache-duplication.
            PartialSubjectWithSemester cs = UQSubjectBuilder.getSubjectFor(storage, bbid, courseid);

            return Optional.fromNullable(cs);
        } catch (DLectException ex) {
            Customisers.LOG.error("Get Subject for failed for bbid: " + bbid + "; and courseid: " + courseid);
            return Optional.absent();
        }
    }

}
