/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import java.beans.PropertyChangeListener;
import org.lee.echo360.providers.implementers.test.MobileProviderLocaliserForTesting;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.implementers.test.xml.BlackboardMarshaller;
import org.lee.echo360.providers.implementers.test.xml.EnrollmentInfo;
import org.lee.echo360.providers.implementers.test.xml.SubjectInfo;
import org.lee.echo360.util.ExceptionReporter;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
public class BlackboardMobileProviderLectureImplTest {

    public static final int LECTURES_PER_SUBJECT = 500;

    public BlackboardMobileProviderLectureImplTest() {
    }
    private EnrollmentInfo ei = BlackboardMarshaller.getRandomEnrollmentInfo(1, LECTURES_PER_SUBJECT);
    private MobileProviderLocaliserForTesting mpl = new MobileProviderLocaliserForTesting(ei);
    private MobileXmlProvider mxp = new MobileXmlProviderForTesting(ei);
    private BlackboardMobileProviderLectureImpl bmpli;

    @BeforeClass
    public static void setUpClass() {
        ExceptionReporter.setExecuteOnSeperateThread(false);
    }

    @AfterClass
    public static void tearDownClass() {
        ExceptionReporter.setExecuteOnSeperateThread(false);
    }

    @Before
    public void setUp() {
        bmpli = new BlackboardMobileProviderLectureImpl(mxp, mpl);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getLecturesIn method, of class
     * BlackboardMobileProviderLectureImpl.
     */
    @Test(timeout = 2000L)
    public void testGetLecturesIn() {
        PropertyChangeListener l = DebuggingListener.getInstance();
        SubjectInfo get = ei.courses.first();
        Blackboard b = mpl.addAllSubjectsToBlackboard(new Blackboard(), ei);
        Subject s = b.getSubjects().first();

        bmpli.getLecturesIn(b, s);

        assertEquals("Subject Lectures List was an invalid size", get.data.lectures.size(), s.getLectures().size());
    }

    /**
     * Test of getLecturesIn method, of class
     * BlackboardMobileProviderLectureImpl.
     */
    @Test(timeout = 2000L)
    public void testMockedGetLecturesIn() {
        PropertyChangeListener l = DebuggingListener.getInstance();
        SubjectInfo get = ei.courses.first();
        Blackboard b = mpl.addAllSubjectsToBlackboard(new Blackboard(), ei);
        Subject s = b.getSubjects().first();
        Subject spy = spy(s);

        //doNothing().when(spy).addLecture(any(Lecture.class));

        bmpli.getLecturesIn(b, spy);

        verify(spy, times(LECTURES_PER_SUBJECT)).addLecture(any(Lecture.class));
        assertEquals("Subject Lectures List was an invalid size", get.data.lectures.size(), s.getLectures().size());
    }
}