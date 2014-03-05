/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.worker;

import org.dlect.controller.MainController;
import org.dlect.controller.event.ControllerType;
import org.dlect.exception.DLectException;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class LectureWorker extends DLectSwingWorker<Subject> {

    public LectureWorker(ErrorDisplayable displayable, MainController controller, Subject subject) {
        super(displayable, controller, ControllerType.LECTURE, subject);
    }

    @Override
    protected void doAction() throws DLectException {
        getController().getLectureController().getLectures(getParameter());
    }

}
