/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.lee.echo360.test.builders.LectureBuilder;

/**
 *
 * @author lee
 */
public class LectureTest {

    public LectureTest() {
    }

    /**
     * Test of getTime method, of class Lecture.
     */
    @Test
    @Ignore
    public void testGetTime() {
        System.out.println("getTime");
        Lecture instance = null;
        Date expResult = null;
        Date result = instance.getTime();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUrl method, of class Lecture.
     */
    @Test
    @Ignore
    public void testGetUrl() {
        System.out.println("getUrl");
        Lecture instance = null;
        URI expResult = null;
        URI result = instance.getUrl();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStreams method, of class Lecture.
     */
    @Test
    @Ignore
    public void testGetStreams() {
        System.out.println("getStreams");
        Lecture instance = null;
        List expResult = null;
        List result = instance.getStreams();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubjectCode method, of class Lecture.
     */
    @Test
    @Ignore
    public void testGetSubjectCode() {
        System.out.println("getSubjectCode");
        Lecture instance = null;
        String expResult = "";
        String result = instance.getSubjectCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContentID method, of class Lecture.
     */
    @Test
    @Ignore
    public void testGetContentID() {
        System.out.println("getContentID");
        Lecture instance = null;
        String expResult = "";
        String result = instance.getContentID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareTo method, of class Lecture.
     */
    @Test
    @Ignore
    public void testCompareTo() {
        System.out.println("compareTo");
        Lecture o = null;
        Lecture instance = null;
        int expResult = 0;
        int result = instance.compareTo(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Lecture.
     */
    @Test
    @Ignore
    public void testToString() {
        System.out.println("toString");
        Lecture instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Lecture.
     */
    @Test
    @Ignore
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        Lecture instance = null;
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDownloadEnabled method, of class Lecture.
     */
    @Test
    public void testIsDownloadEnabled() {
        DownloadType dt = DownloadType.AUDIO;
        LectureBuilder b = LectureBuilder.getBuilder().enabled(dt, true);
        Lecture instance = b.build(); // DEn = true
        boolean expResult = true;
        boolean result = instance.isDownloadEnabled(dt);
        assertEquals(expResult, result);

        b.enabled(false);
        instance = b.build(); // DEn = true
        expResult = true;
        result = instance.isDownloadEnabled(dt);
        assertEquals(expResult, result);

        b.enabled(true);
        instance = b.build(); // DEn = true
        expResult = true;
        result = instance.isDownloadEnabled(dt);
        assertEquals(expResult, result);

        b.enabled(dt, false);
        instance = b.build(); // DEn = true
        expResult = false;
        result = instance.isDownloadEnabled(dt);
        assertEquals(expResult, result);

        b.file(dt, true);
        instance = b.build(); // DEn = true
        expResult = false;
        result = instance.isDownloadEnabled(dt);
        assertEquals(expResult, result);
    }

    /**
     * Test of isDownloadEnabledOrPresent method, of class Lecture.
     */
    @Test
    public void testIsDownloadEnabledOrPresent() {
        System.out.println("isDownloadEnabledOrPresent");
        DownloadType dt = DownloadType.AUDIO;
        LectureBuilder b = LectureBuilder.getBuilder().enabled(dt, true);
        Lecture instance = b.build(); // DEn = true
        boolean expResult = true;
        boolean result = instance.isDownloadEnabledOrPresent(dt);
        assertEquals(expResult, result);

        b.enabled(false);
        instance = b.build(); // DEn = true
        expResult = true;
        result = instance.isDownloadEnabledOrPresent(dt);
        assertEquals(expResult, result);

        b.enabled(true);
        instance = b.build(); // DEn = true
        expResult = true;
        result = instance.isDownloadEnabledOrPresent(dt);
        assertEquals(expResult, result);

        b.enabled(dt, false);
        instance = b.build(); // DEn = true
        expResult = false;
        result = instance.isDownloadEnabledOrPresent(dt);
        assertEquals(expResult, result);

        b.file(dt, true);
        instance = b.build(); // DEn = true
        expResult = true;
        result = instance.isDownloadEnabledOrPresent(dt);
        assertEquals(expResult, result);
    }

    /**
     * Test of isDownloadEnabledOrPresentAndEnabled method, of class Lecture.
     */
    @Test
    public void testIsDownloadEnabledOrPresentAndEnabled() {
        System.out.println("isDownloadEnabledOrPresentAndEnabled");
        DownloadType dt = DownloadType.AUDIO;
        LectureBuilder b = LectureBuilder.getBuilder().enabled(dt, true);
        Lecture instance = b.build(); // DEn = true
        boolean expResult = false;
        boolean result = instance.isDownloadEnabledOrPresentAndEnabled(dt);
        assertEquals(expResult, result);

        b.enabled(false);
        instance = b.build(); // DEn = true
        expResult = false;
        result = instance.isDownloadEnabledOrPresentAndEnabled(dt);
        assertEquals(expResult, result);

        b.enabled(true);
        instance = b.build(); // DEn = true
        expResult = true;
        result = instance.isDownloadEnabledOrPresentAndEnabled(dt);
        assertEquals(expResult, result);

        b.enabled(dt, false);
        instance = b.build(); // DEn = true
        expResult = false;
        result = instance.isDownloadEnabledOrPresentAndEnabled(dt);
        assertEquals(expResult, result);

        b.file(dt, true);
        instance = b.build(); // DEn = true
        expResult = true;
        result = instance.isDownloadEnabledOrPresentAndEnabled(dt);
        assertEquals(expResult, result);
    }

    /**
     * Test of setDownloadEnabled method, of class Lecture.
     */
    @Test
    @Ignore
    public void testSetDownloadEnabled() {
        System.out.println("setDownloadEnabled");
        DownloadType type = null;
        boolean downloadEnabled = false;
        Lecture instance = null;
        boolean expResult = false;
        boolean result = instance.setDownloadEnabled(type, downloadEnabled);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isFilePresent method, of class Lecture.
     */
    @Test
    @Ignore
    public void testIsFilePresent() {
        System.out.println("isFilePresent");
        DownloadType downloadType = null;
        Lecture instance = null;
        boolean expResult = false;
        boolean result = instance.isFilePresent(downloadType);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFileName method, of class Lecture.
     */
    @Test
    @Ignore
    public void testGetFileName() {
        System.out.println("getFileName");
        DownloadType type = null;
        Lecture instance = null;
        File expResult = null;
        String result = instance.getFileName(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFilePresent method, of class Lecture.
     */
    @Ignore
    @Test
    public void testSetFilePresent() {
        System.out.println("setFilePresent");
        DownloadType type = null;
        boolean audioFilesPresent = false;
        Lecture instance = null;
        boolean expResult = false;
        boolean result = instance.setFilePresent(type, audioFilesPresent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isEnabled method, of class Lecture.
     */
    @Test
    @Ignore
    public void testIsEnabled() {
        System.out.println("isEnabled");
        Lecture instance = null;
        boolean expResult = false;
        boolean result = instance.isEnabled();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEnabled method, of class Lecture.
     */
    @Test
    @Ignore
    public void testSetEnabled() {
        System.out.println("setEnabled");
        boolean enabled = false;
        Lecture instance = null;
        instance.setEnabled(enabled);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}