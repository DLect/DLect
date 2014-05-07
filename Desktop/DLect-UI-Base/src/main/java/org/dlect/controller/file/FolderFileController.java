/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    protected static final ThreadLocalDateFormat LECTURE_FILENAME_DATE_FORMAT = new ThreadLocalDateFormat("yyyy-MM-dd HH.mm");

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

        String outputFileName = subjectFolderName + " ~ " + lectureDate + "." + ld.getDownloadExtension();
        
        Path parent = getParentFolder().toPath();

        if (!Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        
        Path download = Paths.get(parent.toString(), subjectFolderName, outputFileName);
        
        ProviderLogger.LOGGER.error("Download File({}; {}): {}", subjectFolderName, lectureDate, download.toFile());

        return download.toFile();
    }

    public String formatSubjectFolder(Subject s) {
        return s.getName().replaceAll("[\\\\/:;]", "-");
    }

    public String formatLectureDate(Lecture l, LectureDownload ld) {
        return LECTURE_FILENAME_DATE_FORMAT.format(l.getTime());
    }

}
