/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Subject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class FolderFileControllerTest {

    private TestFolderFileController testObject;
    private File temporaryFolder;

    @Before
    public void setUp() throws IOException {
        testObject = new TestFolderFileController();
        temporaryFolder = Files.createTempDirectory("DLect-FFC").toFile();
        testObject.setFolder(temporaryFolder);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getParentFolder method, of class FolderFileController.
     */
    @Test
    public void testGetParentFolder_Default() {
        FolderFileController ffc = new FolderFileController();
        assertEquals(null, ffc.getParentFolder());
    }

    /**
     * Test of getParentFolder method, of class FolderFileController.
     */
    @Test
    public void testGetParentFolder_File() {
        FolderFileController ffc = new FolderFileController(temporaryFolder);
        assertEquals(temporaryFolder, ffc.getParentFolder());
    }

    /**
     * Test of getParentFolder method, of class FolderFileController.
     */
    @Test
    public void testGetParentFolder_NullFile() {
        FolderFileController ffc = new FolderFileController(null);
        assertEquals(null, ffc.getParentFolder());
    }

    /**
     * Test of getFileForDownload method, of class FolderFileController.
     */
    @Test
    public void testGetFileForDownload() throws Exception {
        Subject s = new Subject();
        Lecture l = new Lecture();
        LectureDownload ld = new LectureDownload(null, "mp4", true, true);

        testObject.map(s, "Subject");
        testObject.map(l, ld, "Lect Downl");
        
        File targetFolder = new File(temporaryFolder, "Subject");
        File targetFile = new File(targetFolder, "Subject ~ Lect Downl.mp4");
        
        System.out.println(targetFolder.toPath());
        
        assertTrue(Files.notExists(targetFolder.toPath()));
        assertTrue(Files.notExists(targetFile.toPath()));

        File ffd = testObject.getFileForDownload(s, l, ld);
        
        assertEquals(targetFile, ffd);
        assertEquals(targetFolder, ffd.getParentFile());
        
        assertTrue(Files.notExists(targetFolder.toPath()));
        assertTrue(Files.notExists(targetFile.toPath()));
    }

    /**
     * Test of formatSubjectFolder method, of class FolderFileController.
     */
    @Test
    public void testFormatSubjectFolder() {
    }

    /**
     * Test of formatLectureDate method, of class FolderFileController.
     */
    @Test
    public void testFormatLectureDate() {                     
    }

}
