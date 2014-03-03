/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.worker;

import org.dlect.controller.event.ControllerType;
import org.dlect.exception.DLectExceptionCause;

/**
 *
 * @author lee
 */
public interface ErrorDisplayable {

    public void showErrorBox(ControllerType type, Object parameter, DLectExceptionCause get);

}
