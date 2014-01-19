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

package org.dlect.export;

import org.dlect.export.wrapper.SubjectResponse;
import org.dlect.export.wrapper.UniversityList;
import org.dlect.export.wrapper.SemesterResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.dlect.test.MarshalCapableTester.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class XmlExportTest {

    @Test
    public void testLecture() {
        System.out.println("Hello - I'm A TEST");
        testMarshalNonRootObject(Lecture.class);
    }

    @Test
    public void testLoginResult() {
        testMarshalObject(LoginResult.class);
    }

    @Test
    public void testSemester() {
        testMarshalObject(Semester.class);
    }

    @Test
    public void testSemesterResponse() {
        testMarshalObject(SemesterResponse.class);
    }

    @Test
    public void testStatus() {
        testMarshalNonRootObject(Status.class);
    }

    @Test
    public void testStream() {
        testMarshalNonRootObject(Stream.class);
    }

    @Test
    public void testSubject() {
        testMarshalObject(Subject.class);
    }

    @Test
    public void testSubjectList() {
        testMarshalObject(SubjectResponse.class);
    }

    @Test
    public void testUniversity() {
        testMarshalObject(University.class);
    }

    @Test
    public void testUniversityList() {
        testMarshalObject(UniversityList.class);
    }

}
