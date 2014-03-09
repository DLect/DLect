/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.lecture;

import com.google.common.collect.Maps;
import java.util.Map;
import org.dlect.controller.MainController;
import org.dlect.controller.event.ControllerEvent;
import org.dlect.controller.event.ControllerState;
import org.dlect.controller.event.ControllerType;
import org.dlect.controller.helper.Controller;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.model.Database;
import org.dlect.model.Semester;
import org.dlect.model.Semester.SemesterEventID;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class LectureStateUpdater implements EventListener {

    private final Database d;
    private final Map<Subject, LectureStateUpdateHandler> lectureHandlers = Maps.newHashMap();

    protected LectureStateUpdater(Database d) {
        // TODO implement this for LectureWorker.
        this.d = d;
    }

    @Override
    public void processEvent(Event e) {
        if (e instanceof ControllerEvent) {
            ControllerEvent ce = (ControllerEvent) e;
            if (ce.getEventID() == ControllerType.SUBJECT && ce.getAfter() == ControllerState.STARTED) {
                for (Semester sem : d.getSemesters()) {
                    for (Subject subject : sem.getSubjects()) {
                        addHandlerFor(subject);
                    }
                }
            } else if (ce.getEventID() == ControllerType.LECTURE && ce.getAfter() != ControllerState.STARTED) {
                for (Semester sem : d.getSemesters()) {
                    for (Subject subject : sem.getSubjects()) {
                        LectureStateUpdateHandler get = lectureHandlers.get(subject);
                        if (get == null) {
                            get = new EmptyLectureStateUpdateHandler(subject);
                            get.init();
                        }
                        get.updateLectures();
                    }
                }
            }
        } else if (e.getEventID().equals(SemesterEventID.SUBJECT)) {
            Subject before = (Subject) e.getBefore();
            Subject after = (Subject) e.getAfter();
            if (before != null) {
                lectureHandlers.remove(before);
            }
            if (after != null) {
                LectureStateUpdateHandler lsuh = new EmptyLectureStateUpdateHandler(after);
                lsuh.init();
                lectureHandlers.put(after, lsuh);
            }
        }
    }

    private void addHandlerFor(Subject subject) {
        LectureStateUpdateHandler lsuh;
        if (subject.getLectures().isEmpty()) {
            lsuh = new EmptyLectureStateUpdateHandler(subject);
        } else {
            lsuh = new UpdatingLectureStateUpdateHandler(subject);
        }
        lsuh.init();
        lectureHandlers.put(subject, lsuh);
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
        return o instanceof LectureStateUpdater;
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
        LectureStateUpdater sdu = new LectureStateUpdater(mc.getDatabaseHandler().getDatabase());
        mc.addListener(sdu, Controller.class);
    }

}
