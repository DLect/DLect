/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.file;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import java.io.File;
import java.util.Map;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class TestFolderFileController extends FolderFileController {

    private File folder;
    private final Table<Lecture, LectureDownload, String> lectureFileNameMappings = HashBasedTable.create();
    private final Map<Subject, String> subjectFolderNameMappings = Maps.newHashMap();

    public void setFolder(File folder) {
        this.folder = folder;
    }

    @Override
    public File getParentFolder() {
        return folder;
    }

    public void map(Subject su, String st) {
        subjectFolderNameMappings.put(su, st);
    }

    public void map(Lecture le, LectureDownload ld, String st) {
        lectureFileNameMappings.put(le, ld, st);
    }

    @Override
    public String formatSubjectFolder(Subject s) {
        String g = subjectFolderNameMappings.get(s);
        if (g == null) {
            return "";
        }
        return g;
    }

    @Override
    public String formatLectureDate(Lecture l, LectureDownload ld) {
        String g = lectureFileNameMappings.get(l, ld);
        if (g == null) {
            return "";
        }
        return g;
    }

}
