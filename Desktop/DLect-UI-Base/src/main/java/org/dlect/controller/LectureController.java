/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller;

import org.dlect.controller.event.ControllerState;
import org.dlect.controller.event.ControllerListenable;
import org.dlect.controller.event.ControllerType;
import org.dlect.exception.DLectException;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class LectureController extends ControllerListenable<LectureController> {

    private final MainController ctl;

    public LectureController(MainController ctl) {
        this.ctl = ctl;
    }

    @Override
    public void init() {
        // No Op
    }

    public void getLectures(Subject s) throws DLectException {
        event(ControllerType.LECTURE).state(ControllerState.STARTED).parameter(s).fire();
        boolean event = false;
        try {
            ctl.getProviderHelper().getProvider().getLecturesIn(s);
            event(ControllerType.LECTURE).state(ControllerState.COMPLETED).parameter(s).fire();
            event = true;
        } finally {
            // Always fire a fail event. Even if a non-DLectException was thrown.
            if (!event) {
                event(ControllerType.LECTURE).state(ControllerState.FAILED).parameter(s).fire();
            }
        }
    }

}
