/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.worker;

import org.dlect.controller.MainController;
import org.dlect.controller.event.ControllerType;
import org.dlect.exception.DLectException;

/**
 *
 * @author lee
 */
public class LoginWorker extends DLectSwingWorker<Void> {

    public LoginWorker(ErrorDisplayable displayable, MainController controller) {
        super(displayable, controller, ControllerType.LOGIN);
    }

    @Override
    protected void doAction() throws DLectException {
        getController().getLoginController().doLogin();
    }

}
