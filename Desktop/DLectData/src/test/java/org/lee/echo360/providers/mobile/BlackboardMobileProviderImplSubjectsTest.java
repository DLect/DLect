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
package org.lee.echo360.providers.mobile;

import java.util.SortedSet;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Subject;
import org.lee.echo360.test.builders.SubjectTestHelper;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@Ignore
public class BlackboardMobileProviderImplSubjectsTest {

    private MobileXmlProvider xmlProv;
    private Blackboard b;
    private MobileProviderLocaliser localiser;
    private BlackboardMobileProviderImpl bmp;

    public BlackboardMobileProviderImplSubjectsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void preTestInit() {
        b = createBlackboard();
        xmlProv = mock(MobileXmlProvider.class);
        localiser = mock(MobileProviderLocaliser.class);
        bmp = new BlackboardMobileProviderImpl(xmlProv, localiser);
    }

    public static Blackboard createBlackboard() {
        final Blackboard b = new Blackboard();
        b.setUsername("testUser");
        b.setPassword("testPass");
        return b;
    }

    @After
    public void postTestCheck() throws Exception {
        verify(xmlProv).getSubjects();
        verifyNoMoreInteractions(xmlProv);
        verifyNoMoreInteractions(localiser);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetValidSubjects() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper().addRandomSubject();
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertFalse("No subjects added", subjs.isEmpty());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetZeroSubjects() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper();
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertTrue("Plucked subject out of thin air", subjs.isEmpty());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetSubjectWithoutBBName() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper().addSubject("", null, "bbid", "cid", null, 0, null);
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertTrue("Plucked subject out of thin air", subjs.isEmpty());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetSubjectWithoutBBID() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper().addSubject("bbName", null, "", "cid", null, 0, null);
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertTrue("Plucked subject out of thin air", subjs.isEmpty());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetSubjectWithoutCID() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper().addSubject("bbName", null, "bbid", "", null, 0, null);
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertTrue("Plucked subject out of thin air", subjs.isEmpty());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetSubjectWithoutDate() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper().addSubject("bbName", null, "bbid", "cid", null, 0, null);
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertTrue("Plucked subject out of thin air", subjs.isEmpty());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetSubjectNameNoSem() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper().addSubject("bbName", "bl", "bbid", "cid", null, Integer.MAX_VALUE, 0);
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertTrue("Plucked subject out of thin air", subjs.isEmpty());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetSubjectNoFile() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper().addSubject("bbName", "bl", "bbid", "cid", null, 1, 0);
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertTrue("Plucked subject out of thin air", subjs.isEmpty());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetMultipleSubject() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper().addRandomSubject().addRandomSubject();
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertEquals("Wrong Number of Subjects", 2, subjs.size());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }

    /**
     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testGetMultipleSubjectWithInvalid() throws Exception {
        final SubjectTestHelper subjectTester = new SubjectTestHelper()
                .addRandomSubject().addRandomSubject().addRandomSubject()
                .addSubject("bbName", null, "bbid", "cid", null, 0, null);
        subjectTester.initMockedObjects(xmlProv, localiser);

        ActionResult result = bmp.getSubjectLocator().getSubjects(b);
        assertEquals(ActionResult.SUCCEDED, result);
        SortedSet<Subject> subjs = b.getSubjects();
        assertEquals("Wrong Number of Subjects", 3, subjs.size());

        // Verify Mock Interactions
        subjectTester.checkMockedObjects(xmlProv, localiser, b);
    }
}
