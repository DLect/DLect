/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.dlect.file.FileController;
import org.dlect.logging.ProviderLogger;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Subject;
import org.dlect.model.helper.ThreadLocalDateFormat;

/**
 *
 * @author lee
 */
public class FolderFileController implements FileController {
    
    private final File parentFolder;
    
    protected static final ThreadLocalDateFormat LECTURE_FILENAME_DATE_FORMAT = new ThreadLocalDateFormat("yyyy-MM-dd hh.mm");
    
    public FolderFileController() {
        this.parentFolder = null;
    }
    
    public FolderFileController(File parentFolder) {
        this.parentFolder = parentFolder;
    }
    
    public File getParentFolder() {
        return parentFolder;
    }
    
    @Override
    public File getFileForDownload(Subject s, Lecture l, LectureDownload ld) throws IOException {
        String subjectFolderName = formatSubjectFolder(s);
        String lectureDate = formatLectureDate(l, ld);
        
        Path parent = getParentFolder().toPath();
        
        if (!Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        
        Path subjectFolder = parent.resolve(subjectFolderName);
        
        if (Files.notExists(subjectFolder)) {
            try {
                Files.createDirectory(subjectFolder);
            } catch (FileAlreadyExistsException exising) {
                ProviderLogger.LOGGER.error("Lecture does not exist but threw exception. Folder: " + subjectFolderName, exising);
            }
        }
        
        if (!Files.isDirectory(subjectFolder)) {
            throw new IOException("Subject folder does not represent a folder on disk. Folder: " + subjectFolder);
        }
        
        Path download = parent.resolve(subjectFolderName + " ~ " + lectureDate + "." + ld.getDownloadExtension());
        
        ProviderLogger.LOGGER.error("Download File({}; {}): {}", subjectFolderName, lectureDate, download.toFile());
        
        return download.toFile();
    }
    
    public String formatSubjectFolder(Subject s) {
        return s.getName().replaceAll("[\\\\/:;]", "|");
    }
    
    public String formatLectureDate(Lecture l, LectureDownload ld) {
        return LECTURE_FILENAME_DATE_FORMAT.format(l.getTime());
    }
    
}
