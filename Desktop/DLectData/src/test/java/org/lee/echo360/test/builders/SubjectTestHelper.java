/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lee.echo360.test.builders;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.lee.echo360.providers.mobile.MobileProviderLocaliser;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Semester;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.mobile.MobileXmlProvider;
import static org.lee.echo360.test.TestUtilities.createXMLDocument;
import org.lee.echo360.util.StringUtil;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 *
 * @author lee
 */
public class SubjectTestHelper {

    private static final ThreadLocalRandom r = ThreadLocalRandom.current();
    private static final String XML_PREFIX = "<?xml version=\"1.0\" "
            + "encoding=\"UTF-8\"?><mobileresponse "
            + "status=\"OK\" coursesDisplayName=\"Courses\" "
            + "orgsDisplayName=\"Organisations\" defaultLocale=\"en_GB\" "
            + "assessments3.0=\"false\"><courses>";
    private static final String XML_POSTFIX = "</courses></mobileresponse>";
    private static final String SUBJECT_XML = "<course bbid=\"%s\" "
            + "name=\"%s\" courseid=\"%s\" "
            + "role=\"Courses in which you are enrolled:\" isAvail=\"true\" "
            + "locale=\"en_GB\" enrollmentdate=\"%s\" "
            + "roleIdentifier=\"S\" />";

    public void initMockedObjects(MobileXmlProvider xmlProv, MobileProviderLocaliser localiser) throws Exception {
        String xml = genXML();
        doReturn(createXMLDocument(xml)).when(xmlProv).getSubjects();
        for (Subj subj : subjects) {
            subj.initMock(localiser);
        }
    }

    public void checkMockedObjects(MobileXmlProvider xmlProv, MobileProviderLocaliser localiser, Blackboard b) {
        for (Subj subj : subjects) {
            subj.checkMock(localiser);
        }
        List<Subject> bbSubj = new ArrayList<Subject>(b.getSubjects());
        for (Subject subject : bbSubj) {
            boolean found = false;
            for (Subj subj : subjects) {
                if (subj.bbid.equals(subject.getBlackboardId())) {
                    subj.check(subject);
                    found = true;
                    break;
                }
            }
            assertTrue("Rouge Subject Found(" + subject + ")", found);
        }
    }
    private List<Subj> subjects = new ArrayList<Subj>();

    public SubjectTestHelper() {
    }

    public SubjectTestHelper addRandomSubject() {
        String bbName = randString(10);
        String name = randString(10);
        String bbid = randString(10);
        String courseid = randString(10);
        String fileName = randString(10);
        final int semester = r.nextInt(10, 1 << 8);
        Date enrollmentDate = new Date(r.nextLong(1 << 10) * 1000);
        return addSubject(bbName, name, bbid, courseid, fileName, semester, enrollmentDate);
    }

    public SubjectTestHelper addSubject(String bbName, String name, String bbid, String courseid, String fileName, int semester, Date enrollmentDate) {
        subjects.add(new Subj(bbName, name, bbid, courseid, fileName, semester, enrollmentDate));
        return this;
    }

    public SubjectTestHelper addSubject(String bbName, String name, String bbid, String courseid, String fileName, int semester, long dTime) {
        return addSubject(bbName, name, bbid, courseid, fileName, semester, new Date(dTime));
    }

    private String genXML() {
        StringBuilder xml = new StringBuilder(XML_PREFIX);
        for (Subj subj : subjects) {
            xml.append(subj.genXML());
        }
        xml.append(XML_POSTFIX);
        return xml.toString();
    }

    private String randString(int recLen) {
        return r.nextInt(100, 999) + new BigInteger(recLen * 5, r).toString(32);
    }

    private static class Subj {

        final String bbName, name, bbid, courseid, fileName;
        final int semester;
        final Date enrollmentDate;
        final boolean getsToName, getsToFile, getsToSubject;

        Subj(String bbName, String name, String bbid, String courseid, String fileName, int semester, Date enrollmentDate) {
            this.bbName = (bbName == null ? "" : bbName);
            this.name = name;
            this.bbid = (bbid == null ? "" : bbid);
            this.courseid = (courseid == null ? "" : courseid);
            this.fileName = fileName;
            this.semester = semester;
            this.enrollmentDate = enrollmentDate;
            if (this.bbName.isEmpty() || this.bbid.isEmpty() || this.courseid.isEmpty() || this.enrollmentDate == null) {
                getsToName = false;
                getsToFile = false;
                getsToSubject = false;
            } else if (this.name == null || this.name.isEmpty() || this.semester == Integer.MIN_VALUE) {
                getsToName = true;
                getsToFile = false;
                getsToSubject = false;
            } else if (this.fileName == null || this.fileName.isEmpty()) {
                getsToName = true;
                getsToFile = true;
                getsToSubject = false;
            } else {
                getsToName = true;
                getsToFile = true;
                getsToSubject = true;
            }
        }

        String genXML() {
            return String.format(SUBJECT_XML, bbid, bbName, courseid, StringUtil.formatDate(StringUtil.XML_DATE_FORMAT_WITH_TIME_ZONE, enrollmentDate));
        }

        void initMock(MobileProviderLocaliser localiser) {
            if (getsToName) {
                doReturn(name).when(localiser).getCourseName(bbName);
                doReturn(semester).when(localiser).getSemesterNumber(bbName, courseid, bbid, enrollmentDate);
            }
            if (getsToFile) {
                doReturn(fileName).when(localiser).getCourseFileName(bbName, name);
                throw new IllegalStateException("DSAGadkjhgsdflkjghalsfkgshdfghjlsfdghljsdflhgfd");
                //doReturn(new Semester(semester, semester + " Semester", "Sem " + semester)).when(localiser).getSemester(semester);
            }
        }

        void checkMock(MobileProviderLocaliser localiser) {
            if (getsToName) {
                verify(localiser).getCourseName(bbName);
                verify(localiser).getSemesterNumber(bbName, courseid, bbid, enrollmentDate);
            }
            if (getsToFile) {
                verify(localiser).getCourseFileName(bbName, name);
                throw new IllegalStateException("DSAGadkjhgsdflkjghalsfkgshdfghjlsfdghljsdflhgfd");
//                verify(localiser).getSemester(semester);
            }
        }

        private void check(Subject s) {
            assertTrue("Should not have got to subject (" + s + ")", getsToSubject);
            assertEquals(s + ": getBlackboardId", bbid, s.getBlackboardId());
            assertEquals(s + ": getCourseID", courseid, s.getCourseID());
            assertEquals(s + ": getFolderName", fileName, s.getFolderName());
            assertEquals(s + ": getName", name, s.getName());
            assertEquals(s + ": getSemesterCode", semester, s.getSemesterCode());
            assertFalse(s + ": isDownloadEnabled", s.isDownloadEnabled());
            assertTrue(s + ": getLectures.isEmpty", s.getLectures().isEmpty());
            assertTrue(s + ": getStreams.isEmpty", s.getStreams().isEmpty());
        }
    }
}
