/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helper;

import java.util.Map.Entry;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public class SubjectHelper {

    public static void setDownloadTypeEnabled(Subject subject, DownloadType dt, boolean selected) {
        for (Lecture l : subject.getLectures()) {
            for (Entry<DownloadType, LectureDownload> e : l.getLectureDownloads().entrySet()) {
                if (e.getKey().equals(dt)) {
                    e.getValue().setDownloadEnabled(selected);
                }
            }
        }
    }

    public static void setEnabled(Subject s, boolean enabled) {
        for (Lecture lecture : s.getLectures()) {
            lecture.setEnabled(enabled);
        }
    }

    public static void setStreamEnabled(Subject subject, Stream stream, boolean selected) {
        for (Lecture l : subject.getLectures()) {
            if (l.getStreams().contains(stream)) {
                l.setEnabled(selected);
            }
        }
    }

}
