/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.lee.echo360.control.exceptions.InvalidImplemetationException;
import org.lee.echo360.media.MediaTagEncoder;
import org.lee.echo360.media.PlaylistGenerator;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.BlackboardProviderWrapper;
import org.lee.echo360.util.ExceptionReporter;
import org.lee.echo360.util.ThreadUtil;

/**
 *
 * @author lee
 */
public class DownloadController {

    private final MainController ctl;
    private final Object LOCK = new Object();

    public DownloadController(MainController controller) {
        this.ctl = controller;
    }

    public void downloadAllSelected(Collection<Subject> subjects) {
        ExecutorService subjectService = Executors.newFixedThreadPool(subjects.size());
        for (final Subject subject : subjects) {
            if (subject.isDownloadEnabled()) {
                subjectService.submit(new Runnable() {
                    @Override
                    public void run() {
                        downloadSubject(subject);
                    }
                });
            }
        }
        ThreadUtil.waitForCompletion(subjectService);
    }

    public void downloadSubject(final Subject s) {
        ctl.downloadingStarted(s);
        try {
            final Blackboard b = ctl.getPropertiesController().getBlackboard();
            ExecutorService downloadService = Executors.newFixedThreadPool(2);
            for (final Lecture lecture : s.getLectures()) {
                downloadService.submit(new Runnable() {
                    @Override
                    public void run() {
                        downloadLecture(lecture, s, b);
                    }
                });
            }
            ThreadUtil.waitForCompletion(downloadService);
        } finally {
            ctl.downloadingFinished(s);
        }
    }

    private boolean downloadLecture(Lecture l, Subject s, Blackboard b) {
        if (l.isEnabled()) {
            for (DownloadType dt : DownloadType.values()) {
                if (l.isDownloadEnabled(dt)) {
                    try {
                        downloadLecture(l, s, b, dt);
                    } catch (Throwable t) {
                        ExceptionReporter.reportException(t);
                    }
                }
            }
        }
        return true;
    }

    private void updatePlaylists(Subject s, DownloadType dt) {
        PlaylistGenerator.buildAllPlaylistsFor(s, dt, ctl.getPropertiesController());
    }

    public void downloadLecture(Subject s, Lecture l, DownloadType dt) {
        downloadLecture(l, s, ctl.getPropertiesController().getBlackboard(), dt);
    }

    public void downloadLecture(Lecture l, Subject s, Blackboard b, DownloadType dt) {
        if (l.isFilePresent(dt)) {
            return;
        }
        ctl.downloadStarting(s, l, dt);
        final PropertiesController pctl = ctl.getPropertiesController();
        BlackboardProviderWrapper w = pctl.getProvider();
        try {
            File file = pctl.getFileFor(s, l, dt);
            file.getParentFile().mkdirs();
            File tmpFile = new File(file.toString() + ".part");
            tmpFile.deleteOnExit();
            synchronized (LOCK) {
                w.downloadLectureTo(b, s, l, dt, tmpFile);
            }
            MediaTagEncoder.tag(tmpFile, file, s, l, dt);
            tmpFile.delete();
        } catch (InvalidImplemetationException ex) {
            ExceptionReporter.reportException(ex); // TODO add a status to download compeleted
        } finally {
            try {
                ctl.getLectureController().updateInformation(s, l);
                updatePlaylists(s, dt);
            } finally {
                ctl.downloadCompleted(s, l, dt);
            }
        }
    }
}
