/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.worker.download;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import org.dlect.controller.MainController;
import org.dlect.controller.download.event.DownloadParameter;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public class DownloadWorkerHelper {

    public static void downloadAllSelectedIn(DownloadErrorDisplayable displayable, MainController controller, Subject... subjects) {
        downloadAllSelectedIn(displayable, controller, ImmutableList.copyOf(subjects));
    }

    public static void downloadAllSelectedIn(DownloadErrorDisplayable displayable, MainController controller, Collection<Subject> subjects) {
        Set<DownloadParameter> param = Sets.newHashSet();
        for (Subject subject : subjects) {
            for (Lecture lecture : subject.getLectures()) {
                if (lecture.isEnabled()) {
                    for (Entry<DownloadType, LectureDownload> entry : lecture.getLectureDownloads().entrySet()) {
                        DownloadType downloadType = entry.getKey();
                        LectureDownload lectureDownload = entry.getValue();
                        if (lectureDownload.isDownloadEnabled() && !lectureDownload.isDownloaded()) {
                            param.add(new DownloadParameter(subject, lecture, downloadType));
                        }
                    }
                }
            }
        }
        new MultiDownloadWorker(displayable, controller, param).execute();
    }

}
