/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.BlackboardProviderWrapper;
import org.lee.echo360.test.builders.ControllerHelper;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
public class PropertiesControllerTest {

    public PropertiesControllerTest() {
    }

    /**
     * Test of setCredentials method, of class PropertiesController.
     */
    @Test
    @Ignore
    public void testSetCredentials() {
        System.out.println("setCredentials");
        String user = "";
        String pass = "";
        PropertiesController instance = null;
        instance.setCredentials(user, pass);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setProviderClass method, of class PropertiesController.
     */
    @Test
    @Ignore
    public void testSetProviderClass() {
        System.out.println("setProviderClass");
        Class providerClass = null;
        PropertiesController instance = null;
        instance.setProviderClass(providerClass);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBlackboard method, of class PropertiesController.
     */
    @Test
    @Ignore
    public void testGetBlackboard() {
        System.out.println("getBlackboard");
        PropertiesController instance = null;
        Blackboard expResult = null;
        Blackboard result = instance.getBlackboard();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProviderClass method, of class PropertiesController.
     */
    @Test
    @Ignore
    public void testGetProviderClass() {
        System.out.println("getProviderClass");
        PropertiesController instance = null;
        Class expResult = null;
        Class result = instance.getProviderClass();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProvider method, of class PropertiesController.
     */
    @Test
    @Ignore
    public void testGetProvider() {
        System.out.println("getProvider");
        PropertiesController instance = null;
        BlackboardProviderWrapper expResult = null;
        BlackboardProviderWrapper result = instance.getProvider();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initBlackboard method, of class PropertiesController.
     */
    @Test
    @Ignore
    public void testInitBlackboard() {
        System.out.println("initBlackboard");
        PropertiesController instance = null;
        instance.initBlackboard();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFolderFor method, of class PropertiesController.
     */
    @Test
    public void testGetFolderFor_Subject() {
        System.out.println("getFolderFor");
        String subFileName = "SubjectFileName.tmp";
        final File tmpDir = FileUtils.getTempDirectory();
        File tmpFile = FileUtils.getFile(tmpDir, subFileName);
        Subject s = mock(Subject.class);
        when(s.getFolderName()).thenReturn(subFileName);

        ControllerHelper ch = ControllerHelper.build();
        ApplicationPropertiesController a = ch.getApplicationPropertiesController();
        doReturn(tmpDir).when(a).getDataDirectory();
        
        PropertiesController instance = new PropertiesController(ch.getMainController());
        File result = instance.getFolderFor(s);
        assertEquals(tmpFile, result);

        verify(a, atLeastOnce()).getDataDirectory();
        ch.verifyNoMoreInteractions();
    }

    /**
     * Test of getFileFor method, of class PropertiesController.
     */
    @Test
    public void testGetFileFor_3args() {
        System.out.println("getFileFor");
        String subFileName = "SubjectFileName.tmp";
        String lecFileName = "LectureFileName.tmp";
        File tmpDir = FileUtils.getTempDirectory();
        File tmpFile = FileUtils.getFile(tmpDir, subFileName, lecFileName);
        Subject s = mock(Subject.class);
        when(s.getFolderName()).thenReturn(subFileName);

        Lecture l = mock(Lecture.class);
        when(l.getFileName(DownloadType.VIDEO)).thenReturn(lecFileName);

        ControllerHelper ch = ControllerHelper.build();
        ApplicationPropertiesController a = ch.getApplicationPropertiesController();
        doReturn(tmpDir).when(a).getDataDirectory();
        
        PropertiesController instance = new PropertiesController(ch.getMainController());
        File result = instance.getFileFor(s, l, DownloadType.VIDEO);
        assertEquals(tmpFile, result);
        
        verify(a, atLeastOnce()).getDataDirectory();

        ch.verifyNoMoreInteractions();
    }

    /**
     * Test of getFolderFor method, of class PropertiesController.
     */
    @Test
    public void testGetFolderFor_File_Subject() {
        System.out.println("getFolderFor");
        String subFileName = "SubjectFileName.tmp";
        File tmpDir = FileUtils.getTempDirectory();
        File tmpFile = FileUtils.getFile(tmpDir, subFileName);
        Subject s = mock(Subject.class);
        when(s.getFolderName()).thenReturn(subFileName);

        ControllerHelper ch = ControllerHelper.build();

        PropertiesController instance = new PropertiesController(ch.getMainController());
        File result = instance.getFolderFor(tmpDir, s);
        assertEquals(tmpFile, result);

        ch.verifyNoMoreInteractions();
    }

    /**
     * Test of getFileFor method, of class PropertiesController.
     */
    @Test
    public void testGetFileFor_4args() {
        System.out.println("getFileFor");
        String subFileName = "SubjectFileName.tmp";
        String lecFileName = "LectureFileName.tmp";
        File tmpDir = FileUtils.getTempDirectory();
        File tmpFile = FileUtils.getFile(tmpDir, subFileName, lecFileName);
        Subject s = mock(Subject.class);
        when(s.getFolderName()).thenReturn(subFileName);

        Lecture l = mock(Lecture.class);
        when(l.getFileName(DownloadType.VIDEO)).thenReturn(lecFileName);

        ControllerHelper ch = ControllerHelper.build();

        PropertiesController instance = new PropertiesController(ch.getMainController());
        File result = instance.getFileFor(tmpDir, s, l, DownloadType.VIDEO);
        assertEquals(tmpFile, result);

        ch.verifyNoMoreInteractions();
    }

    /**
     * Test of getParentFolder method, of class PropertiesController.
     */
    @Test
    public void testGetParentFolder() {
        System.out.println("getParentFolder");
        File tmpDir = FileUtils.getTempDirectory();
        
        ControllerHelper ch = ControllerHelper.build();
        ApplicationPropertiesController a = ch.getApplicationPropertiesController();
        doReturn(tmpDir).when(a).getDataDirectory();
        
        PropertiesController instance = new PropertiesController(ch.getMainController());
        File result = instance.getParentFolder();
        assertEquals(tmpDir, result);
    }

    /**
     * Test of setBlackboard method, of class PropertiesController.
     */
    @Test
    @Ignore
    public void testSetBlackboard() {
        System.out.println("setBlackboard");
        Blackboard blackboard = null;
        PropertiesController instance = null;
        instance.setBlackboard(blackboard);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}