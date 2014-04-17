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
package org.dlect.ejb.internal.provder.impl.blackboard;

import com.google.common.base.Optional;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import org.dlect.export.Lecture;
import org.dlect.export.Semester;
import org.dlect.export.Subject;
import org.dlect.internal.data.merge.PartialLectureContent;
import org.dlect.internal.data.merge.PartialLectureWithStream;
import org.dlect.internal.data.merge.PartialSemester;
import org.dlect.internal.data.merge.PartialStream;
import org.dlect.internal.data.merge.PartialSubjectWithSemester;
import org.dlect.provider.common.blackboard.xml.BlackboardCourseMapItem;

/**
 * .
 *
 * Please note that this class will recieve many calls in it's lifetime, a weak map between bbid and the return values
 * is recommended as the application does no extra caching of responces.
 *
 *
 * @author lee
 */
@Local
public interface BlackboardUniversityCustomisation {

    public Set<PartialLectureContent> getDownloadsFor(Semester semester, Subject subject, Lecture l);
    
    /**
     *
     * @param semester The semester for this lecture data.
     * @param subject  The subject for this lecture data.
     * @param rootUrl
     * @param item     The lecture data.
     *
     * @return A sufficiently initialised CustomiserLecture or {@linkplain Optional#absent()}
     */
    public Optional<PartialLectureWithStream> getLectureDataFor(Semester semester, Subject subject, URL rootUrl,
                                                                          BlackboardCourseMapItem item);

    /**
     * This is an optional interface method that can return an empty list. If any Stream ID from
     * {@link #getLectureDataFor(Semester, Subject, URL, BlackboardCourseMapItem) } is not found in the list returned, it will be
     * individually requested from {@link #getStreamDataFor(Semester, Subject, String) }
     *
     *
     * @param semester
     * @param subject
     *
     * @return
     */
    public List<PartialStream> getAllStreamData(Semester semester, Subject subject);

    /**
     *
     * @param semester The semester for this lecture data.
     * @param subject  The subject for this lecture data.
     * @param id       The stream id given in list.
     *
     * @return A sufficiently initialised CustomiserLecture or {@linkplain Optional#absent()}
     */
    public Optional<PartialStream> getStreamDataFor(Semester semester, Subject subject, String id);

    // TODO consider introducing DLectExceptions to all these methods.
    /**
     * Get the list of University codes that this customisation supports. These
     * should include the {@code "BBM"} pre-fix, however they should use
     * {@link org.dlect.helpers.internal.impl.BlackboardMobileHelper#toUniversityCode(long)} to obtain the code.
     *
     * @return The codes that this provider supports.
     */
    public Set<String> getUniversityCode();

    /**
     * Return a string indicating the subject's name for storage. If this method returns null, then a warning is logged
     * and an error thrown.
     *
     * @param bbid           The internal Blackboard ID.
     * @param name           The name of the course.
     * @param courseid       The internal course ID.
     * @param enrollmentdate The date Blackboard provides as the enrolment date.
     *
     * @return An Optional object which is either:<br />
     * Not set indicating no support for this subject. OR <br />
     * Contains a populated {@linkplain CustomiserSubject}.
     */
    public Optional<PartialSubjectWithSemester> getSubjectDataFor(String bbid, String name, String courseid, Date enrollmentdate);

    /**
     * Return a semester for a code given in the {@link #getSubjectDataFor(java.lang.String, java.lang.String, java.lang.String, java.util.Date)
     * }
     *
     * @param semCode
     *
     * @return
     */
    public Optional<PartialSemester> getSemesterFor(String semCode);

}
