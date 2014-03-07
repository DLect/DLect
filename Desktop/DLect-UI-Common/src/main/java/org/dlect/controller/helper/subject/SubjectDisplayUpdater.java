/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import org.dlect.controller.MainController;
import org.dlect.controller.event.ControllerEvent;
import org.dlect.controller.event.ControllerState;
import org.dlect.controller.event.ControllerType;
import org.dlect.controller.helper.Controller;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.model.Database;

/**
 *
 * @author lee
 */
public class SubjectDisplayUpdater implements EventListener {

    private final Database d;
    private SubjectDisplayUpdateHandler sduh = null;

    protected SubjectDisplayUpdater(Database d) {
        this.d = d;
    }

    @Override
    public void processEvent(Event e) {
        if (e instanceof ControllerEvent) {
            ControllerEvent ce = (ControllerEvent) e;
            if (ce.getEventID() == ControllerType.SUBJECT) {
                if (ce.getAfter() == ControllerState.STARTED) {
                    if (d.getSemesters().isEmpty()) {
                        sduh = new EmptySubjectDisplayUpdateHandler(d);
                    } else {
                        sduh = new UpdatingSubjectDisplayUpdateHandler(d);
                    }
                } else {
                    if (sduh != null) {
                        sduh.updateSubjects();
                    }
                }
            }
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
        return o instanceof SubjectDisplayUpdater;
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
        SubjectDisplayUpdater sdu = new SubjectDisplayUpdater(mc.getDatabaseHandler().getDatabase());
        mc.addListener(sdu, Controller.class);
    }

}
