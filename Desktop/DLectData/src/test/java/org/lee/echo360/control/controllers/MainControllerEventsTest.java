/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
public class MainControllerEventsTest {

    public MainControllerEventsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addControllerListener method, of class MainController.
     */
    @Test
    public void testStartedControllerListener() {
        System.out.println("addControllerListener");
        final List<ControllerAction> started = Lists.newArrayList();
        // Record only started ones.
        ControllerListener l = new ControllerListener() {
            @Override
            public void start(ControllerAction action) {
                started.add(action);
            }

            @Override
            public void finished(ControllerAction action, ActionResult r) {
            }

            @Override
            public void error(Throwable e) {
            }
        };
        MainController instance = new MainController();
        instance.addControllerListener(l);

        // Now test:
        List<ControllerAction> expected = Lists.newArrayList(ControllerAction.values());
        for (ControllerAction ex : expected) {
            instance.start(ex);
        }

        assertEquals("Not all events fired. Fired events: " + started, expected.size(), started.size());
        assertEquals("Events not fired in order.", expected, started);

    }

    /**
     * Test of addControllerListener method, of class MainController.
     */
    @Test
    public void testFinishedControllerListener() {
        System.out.println("addControllerListener");
        final List<Pair<ControllerAction, ActionResult>> finished = Lists.newArrayList();
        // Record only started ones.
        ControllerListener l = new ControllerListener() {
            @Override
            public void start(ControllerAction action) {
            }

            @Override
            public void finished(ControllerAction action, ActionResult r) {
                finished.add(Pair.of(action, r));
            }

            @Override
            public void error(Throwable e) {
            }
        };
        MainController instance = new MainController();
        instance.addControllerListener(l);

        // Now test:
        List<Pair<ControllerAction, ActionResult>> expected = Lists.newArrayList();
        List<ControllerAction> expectedCa = Lists.newArrayList(ControllerAction.values());
        List<ActionResult> expectedAr = Lists.newArrayList(ActionResult.values());
        for (ControllerAction ca : expectedCa) {
            for (ActionResult ar : expectedAr) {
                instance.finished(ca, ar);
                expected.add(Pair.of(ca, ar));
            }
        }
        assertEquals("Not all events fired. Fired events: " + finished, expected.size(), finished.size());
        assertEquals("Events not fired in order.", expected, finished);

    }

    /**
     * Test of removeControllerListener method, of class MainController.
     */
    @Test
    public void testRemoveControllerListener() {
        System.out.println("removeControllerListener");
        ControllerListener l = null;
        MainController instance = new MainController();
        instance.removeControllerListener(l);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of addDownloadProgressListener method, of class MainController.
     */
    @Test
    public void testAddDownloadProgressListener() {
        System.out.println("addDownloadProgressListener");
        DownloadProgressListener l = null;
        MainController instance = new MainController();
        instance.addDownloadProgressListener(l);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of removeDownloadProgressListener method, of class MainController.
     */
    @Test
    public void testRemoveDownloadProgressListener() {
        System.out.println("removeDownloadProgressListener");
        DownloadProgressListener l = null;
        MainController instance = new MainController();
        instance.removeDownloadProgressListener(l);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of downloadingStarted method, of class MainController.
     */
    @Test
    public void testDownloadingStarted() {
        System.out.println("downloadingStarted");
        Subject s = null;
        MainController instance = new MainController();
        instance.downloadingStarted(s);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of downloadingFinished method, of class MainController.
     */
    @Test
    public void testDownloadingFinished() {
        System.out.println("downloadingFinished");
        Subject s = null;
        MainController instance = new MainController();
        instance.downloadingFinished(s);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of downloadStarting method, of class MainController.
     */
    @Test
    public void testDownloadStarting() {
        System.out.println("downloadStarting");
        Subject s = null;
        Lecture l = null;
        DownloadType t = null;
        MainController instance = new MainController();
        instance.downloadStarting(s, l, t);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of downloadCompleted method, of class MainController.
     */
    @Test
    public void testDownloadCompleted() {
        System.out.println("downloadCompleted");
        Subject s = null;
        Lecture l = null;
        DownloadType t = null;
        MainController instance = new MainController();
        instance.downloadCompleted(s, l, t);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getLastSuccessfulAction method, of class MainController.
     */
    @Test
    public void testGetLastSuccessfulAction() {
        System.out.println("getLastSuccessfulAction");
        MainController instance = new MainController();
        ControllerAction expResult = null;
        ControllerAction result = instance.getLastSuccessfulAction();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getCurrentAction method, of class MainController.
     */
    @Test
    public void testGetCurrentAction() {
        System.out.println("getCurrentAction");
        MainController instance = new MainController();
        ControllerAction expResult = null;
        ControllerAction result = instance.getCurrentAction();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of hasCompleted method, of class MainController.
     */
    @Test
    public void testHasCompleted() {
        System.out.println("hasCompleted");
        ControllerAction a = null;
        MainController instance = new MainController();
        boolean expResult = false;
        boolean result = instance.hasCompleted(a);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getCurrentlyDownloadingSubjects method, of class MainController.
     */
    @Test
    public void testGetCurrentlyDownloadingSubjects() {
        System.out.println("getCurrentlyDownloadingSubjects");
        MainController instance = new MainController();
        Set expResult = null;
        Set result = instance.getCurrentlyDownloadingSubjects();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getSubjectLecutreDownloading method, of class MainController.
     */
    @Test
    public void testGetSubjectLecutreDownloading_0args() {
        System.out.println("getSubjectLecutreDownloading");
        MainController instance = new MainController();
        Set expResult = null;
        Set result = instance.getSubjectLecutreDownloading();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getSubjectLecutreDownloading method, of class MainController.
     */
    @Test
    public void testGetSubjectLecutreDownloading_Subject() {
        System.out.println("getSubjectLecutreDownloading");
        Subject filter = null;
        MainController instance = new MainController();
        Set expResult = null;
        Set result = instance.getSubjectLecutreDownloading(filter);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getLectureDownloading method, of class MainController.
     */
    @Test
    public void testGetLectureDownloading() {
        System.out.println("getLectureDownloading");
        Subject filter = null;
        MainController instance = new MainController();
        Collection expResult = null;
        Collection result = instance.getLectureDownloading(filter);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
}