/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.download;

import java.util.Map;
import org.dlect.controller.MainController;
import org.dlect.controller.download.event.DownloadEvent;
import org.dlect.controller.download.event.DownloadParameter;
import org.dlect.controller.download.event.DownloadStatus;
import org.dlect.controller.event.ControllerListenable;
import org.dlect.controller.helper.Controller;
import org.dlect.exception.DLectException;
import org.dlect.model.Database;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Semester;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public class DownloadController extends ControllerListenable<DownloadController> implements Controller {

    private final MainController ctl;

    public DownloadController(MainController ctl) {
        this.ctl = ctl;
    }

    private void checkValues(Subject s, Lecture l) {
        Database d = ctl.getDatabaseHandler().getDatabase();
        for (Semester semester : d.getSemesters()) {
            for (Subject subject : semester.getSubjects()) {
                if (subject.equals(s)) {
                    if (!subject.getLectures().contains(l)) {
                        throw new IllegalArgumentException("Subject does not contain lecture.");
                    }
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Database does not contain the given subject.");

    }

    private LectureDownload getLectureDownloadFrom(Lecture l, DownloadType dt) {
        Map<DownloadType, LectureDownload> dtld = l.getLectureDownloads();
        LectureDownload ld = dtld.get(dt);
        if (ld == null) {
            throw new IllegalArgumentException("There is not lecture download for the download type given");
        }
        return ld;
    }

    @Override
    public void init() {
        // No op.
    }

    public void downloadLectureDownload(Subject s, Lecture l, DownloadType dt) throws DLectException {
        checkValues(s, l);
        LectureDownload ld = getLectureDownloadFrom(l, dt);

        fireDownloadEvent(DownloadStatus.STARTING, s, l, dt);
        boolean event = false;
        try {
            ctl.getProviderHelper().getProvider().doDownload(s, l, ld);
            fireDownloadEvent(DownloadStatus.COMPLETED, s, l, dt);
            event = true;
        } finally {
            // Always fire a fail event. Even if a non-DLectException was thrown.
            if (!event) {
                fireDownloadEvent(DownloadStatus.FAILED, s, l, dt);
                // TODO(Later) if the download failed delete the file if it exists.
            }
        }
    }

    private void fireDownloadEvent(DownloadStatus downloadStatus, Subject s, Lecture l, DownloadType dt) {
        // TODO(Later) fill in the download statuses with real values.
        this.fireEvent(new DownloadEvent(this, downloadStatus, new DownloadParameter(s, l, dt), null, null));
    }

}
