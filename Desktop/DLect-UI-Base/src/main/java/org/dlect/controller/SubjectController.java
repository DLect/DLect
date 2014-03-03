/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller;

import org.dlect.controller.event.ControllerListenable;
import org.dlect.controller.event.ControllerState;
import org.dlect.controller.event.ControllerType;
import org.dlect.controller.helper.Initilisable;
import org.dlect.exception.DLectException;

/**
 *
 * @author lee
 */
public class SubjectController extends ControllerListenable<SubjectController> implements Initilisable {

    private final MainController ctl;

    public SubjectController(MainController ctl) {
        this.ctl = ctl;
    }

    @Override
    public void init() {
        // No op
    }

    public void findSubjects() throws DLectException {
        event(ControllerType.SUBJECT).state(ControllerState.STARTED).fire();
        boolean event = false;
        try {
            ctl.getProviderHelper().getProvider().getSubjects();
            event(ControllerType.SUBJECT).state(ControllerState.COMPLETED).fire();
            event = true;
        } finally {
            // Always fire a fail event. Even if a non-DLectException was thrown.
            if (!event) {
                event(ControllerType.SUBJECT).state(ControllerState.FAILED).fire();
            }
        }
    }

}
