/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui;

import org.dlect.controller.event.ControllerEvent;
import org.dlect.controller.event.ControllerState;
import org.dlect.controller.event.ControllerType;
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

    public SubjectDisplayUpdater(Database d) {
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
                    // TODO remove this as a listener iff Succeded
                    // TODO remove the listener added above. 
                    // If Failed - 
                }
            }
        }
    }

}
