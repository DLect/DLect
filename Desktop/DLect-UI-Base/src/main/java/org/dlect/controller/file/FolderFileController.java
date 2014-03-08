/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.file;

import java.io.File;
import java.io.IOException;
import org.dlect.file.FileController;
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
    public File getStreamForDownload(Subject s, Lecture l, LectureDownload ld) throws IOException {
        String subjectFolderName = formatSubjectFolder(s);
        String lectureDate = formatLectureDate(l, ld);

        final File parent = getParentFolder();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        File subjectFolder = new File(parent, subjectFolderName);
        if (!subjectFolder.exists()) {
            subjectFolder.mkdir();
        }
        if (!subjectFolder.isDirectory()) {
            throw new IOException("Subject folder does not represent a folder on disk.");
        }

        File f = new File(subjectFolder, lectureDate);

        return f;
    }

    public String formatSubjectFolder(Subject s) {
        return s.getName().replaceAll("[\\\\/:;]", "|");
    }

    public String formatLectureDate(Lecture l, LectureDownload ld) {
        return LECTURE_FILENAME_DATE_FORMAT.format(l.getTime());
    }

}
