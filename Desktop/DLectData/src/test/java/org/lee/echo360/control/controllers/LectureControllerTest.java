/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import com.google.common.collect.Lists;
import java.util.List;
import org.lee.echo360.test.configure.Reporter;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.implementers.test.MobileProviderForTestingImpl;
import org.lee.echo360.providers.implementers.test.xml.BlackboardMarshaller;
import org.lee.echo360.providers.implementers.test.xml.EnrollmentInfo;
import org.lee.echo360.providers.mobile.MobileXmlProvider;
import org.lee.echo360.providers.mobile.MobileXmlProviderForTesting;
import org.lee.echo360.test.configure.SavingLock;
import org.lee.echo360.util.ExceptionReporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
public class LectureControllerTest {

    private static final int LECTURES_PER_SUBJECT = 50;
    public static final long GET_ALL_LECTURES_TIMEOUT = 10000L;
    public static final int TEST_REPEATS = 30;
    private LectureController instance;
    private PropertiesController pc;
    private EnrollmentInfo ei = BlackboardMarshaller.getRandomEnrollmentInfo(10, LECTURES_PER_SUBJECT);
    private MobileProviderForTestingImpl mpl;
    private MobileXmlProvider mxp;
    private MainController mc;
    private List<ControllerAction> startedActions;
    private List<Pair<ControllerAction, ActionResult>> completedActions;

    public LectureControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Reporter.setUp();
        ReentrantLock mockedLock = SavingLock.setUpLock(mock(ReentrantLock.class));
        when(mockedLock.tryLock()).thenReturn(Boolean.FALSE);
    }

    @AfterClass
    public static void tearDownClass() {
        SavingLock.tearDownLock();
    }

    @Before
    public void setUp() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        mxp = new MobileXmlProviderForTesting(ei);
        mpl = new MobileProviderForTestingImpl(ei, mxp);
        mpl.putThisIntoProviders();

        createRealController();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getLecturesFor method, of class LectureController.
     */
    @Test(timeout = 1000L)
    public void testGetLecturesFor() {
        System.out.println("getLecturesFor");
        final Subject subject = pc.getBlackboard().getSubjects().first();

        instance.getLecturesFor(subject);

        assertEquals("Incorrect Lectures Length", LECTURES_PER_SUBJECT, subject.getLectures().size());


    }

    /**
     * Test of getAllLectures method, of class LectureController.
     */
    @Test(timeout = GET_ALL_LECTURES_TIMEOUT)
    public void testGetAllLectures() {
        System.out.println("getAllLectures");

        instance.getAllLectures();

        int index = 0;
        for (Subject s : pc.getBlackboard().getSubjects()) {
            assertEquals("Incorrect Lectures Length(Index: " + index + ")", LECTURES_PER_SUBJECT, s.getLectures().size());
            index++;
        }

        assertFalse("No 'Started' events captured", startedActions.isEmpty());
        assertEquals("Wrong Action captured last", ControllerAction.LECTURES, startedActions.get(startedActions.size() - 1));

//        assertEquals("Events not getting fired!!!!!!", mc.hasCompleted(ControllerAction.LECTURES), !completedActions.isEmpty());
        assertTrue("Not completed Lectures", mc.hasCompleted(ControllerAction.LECTURES));

        assertFalse("No 'Finished' events captured", completedActions.isEmpty());
        Pair<ControllerAction, ActionResult> compl = completedActions.get(completedActions.size() - 1);
        assertEquals("Wrong Action captured last", ControllerAction.LECTURES, compl.getKey());
        assertEquals("Bad Result captured", ActionResult.SUCCEDED, compl.getValue());
    }

    @Test(timeout = GET_ALL_LECTURES_TIMEOUT * TEST_REPEATS)
    public void testRepeatedlyGetAllLectures() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        for (int i = 0; i < TEST_REPEATS; i++) {
            setUp();
            testGetAllLectures();
            tearDown();
        }
    }

    private void createRealController() {
        mc = new MainController();

        startedActions = Lists.newArrayList();
        completedActions = Lists.newArrayList();

        mc.addControllerListener(new ControllerListener() {
            @Override
            public void start(ControllerAction action) {
                startedActions.add(action);
                System.out.println("Start: " + action);
            }

            @Override
            public void finished(ControllerAction action, ActionResult r) {
                completedActions.add(Pair.of(action, r));
                System.out.println("Finish: " + action + ";\n\tResult: " + r);
            }

            @Override
            public void error(Throwable e) {
                ExceptionReporter.reportException(e);
            }
        });

        pc = mc.getPropertiesController();
        pc.setBlackboard(mpl.addAllSubjectsToBlackboard(new Blackboard(), ei));
        pc.setProviderClass(mpl.getClass());

        instance = mc.getLectureController();
    }
}