/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.lecture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.Collection;
import org.dlect.controller.MainController;
import org.dlect.controller.download.event.DownloadEvent;
import org.dlect.controller.helper.Controller;
import org.dlect.events.Event;
import org.dlect.events.EventID;
import org.dlect.events.EventListener;
import org.dlect.file.FileController;
import org.dlect.logging.ControllerLogger;
import org.dlect.logging.ProviderLogger;
import org.dlect.model.Database;
import org.dlect.model.Database.DatabaseEventID;
import org.dlect.model.Lecture;
import org.dlect.model.Lecture.LectureEventID;
import org.dlect.model.LectureDownload;
import org.dlect.model.LectureDownload.LectureDownloadEventID;
import org.dlect.model.Semester;
import org.dlect.model.Semester.SemesterEventID;
import org.dlect.model.Stream.StreamEventID;
import org.dlect.model.Subject;
import org.dlect.model.Subject.SubjectEventID;

/**
 *
 * @author lee
 */
public class LectureDownloadStateUpdater implements EventListener {

    private static final ImmutableSet<EventID> SEMESTER_EVENTS = ImmutableSet.<EventID>copyOf(SemesterEventID.values());
    private static final ImmutableSet<EventID> SUBJECT_EVENTS = ImmutableSet.<EventID>copyOf(SubjectEventID.values());
    private static final ImmutableSet<EventID> STREAM_EVENTS = ImmutableSet.<EventID>copyOf(StreamEventID.values());
    private static final ImmutableSet<EventID> LECTURE_EVENTS = ImmutableSet.<EventID>copyOf(LectureEventID.values());
    private static final ImmutableSet<EventID> LECTURE_DOWNLOAD_EVENTS = ImmutableSet.<EventID>copyOf(LectureDownloadEventID.values());

    private final MainController controller;

    public LectureDownloadStateUpdater(MainController controller) {
        this.controller = controller;
    }

    protected void updateState(Collection<Subject> s) {
        for (Subject subject : s) {
            for (Lecture lecture : subject.getLectures()) {
                updateState(subject, lecture);
            }
        }
    }

    protected void updateState(Subject s, Lecture l) {
        for (LectureDownload ld : l.getLectureDownloads().values()) {
            updateState(s, l, ld);
        }
    }

    protected void updateState(Lecture l) {
        for (Semester sem : controller.getDatabaseHandler().getDatabase().getSemesters()) {
            for (Subject sub : sem.getSubjects()) {
                if (sub.getLectures().contains(l)) {
                    updateState(sub, l);
                    return;
                }
            }
        }
    }

    protected void updateState(LectureDownload ld) {
        for (Semester sem : controller.getDatabaseHandler().getDatabase().getSemesters()) {
            for (Subject sub : sem.getSubjects()) {
                for (Lecture lecture : sub.getLectures()) {
                    if (lecture.getLectureDownloads().containsValue(ld)) {
                        updateState(sub, lecture, ld);
                    }
                }
            }
        }
    }

    protected void updateState(Subject s, Lecture l, LectureDownload ld) {
        FileController fc = controller.getFileController();

        try {
            ld.setDownloaded(fc.getFileForDownload(s, l, ld).exists());
        } catch (IOException ex) {
            ProviderLogger.LOGGER.error("Failed to create file for " + s + "; " + l + "; " + ld, ex);
            ld.setDownloaded(false);
        }
    }

    @Override
    public void processEvent(Event e) {
        Object after = e.getAfter();
        if (after != null) {
            if (DatabaseEventID.SEMESTER.equals(e.getEventID())) {
                Semester s = (Semester) after;
                updateState(s.getSubjects());
            } else if (SEMESTER_EVENTS.contains(e.getEventID())) {
                Subject s = (Subject) after;
                updateState(ImmutableList.of(s));
            } else if (SUBJECT_EVENTS.contains(e.getEventID())) {
                Subject s = (Subject) e.getSource();
                updateState(ImmutableList.of(s));
            } else if (STREAM_EVENTS.contains(e.getEventID())) {
                // TODO find subject and update itself. 
            } else if (LECTURE_EVENTS.contains(e.getEventID())) {
                Lecture l = (Lecture) e.getSource();
                updateState(l);
            } else if (LECTURE_DOWNLOAD_EVENTS.contains(e.getEventID())) {
                LectureDownload ld = (LectureDownload) e.getSource();
                updateState(ld);
            }
        }
        if (e instanceof DownloadEvent) {
            DownloadEvent ce = (DownloadEvent) e;
            updateState(ce.getParameter().getSubject(), ce.getParameter().getLecture());
        }
    }

    /**
     * All instances of this class are equal. This returns true iff the object is the same type as this class.
     *
     * @param o {@inheritDoc }
     *
     * @return {@inheritDoc }
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof LectureDownloadStateUpdater;
    }

    /**
     * All instances of this class are equal, so all hash codes are the same.
     *
     * @return A pre-defined hash code.
     */
    @Override
    public int hashCode() {
        return 1029456435;
    }

    public static void registerOn(MainController mc) {
        LectureDownloadStateUpdater sdu = new LectureDownloadStateUpdater(mc);
        mc.addListener(sdu, Controller.class, Database.class, Semester.class, Subject.class, Lecture.class, LectureDownload.class);
    }

}
