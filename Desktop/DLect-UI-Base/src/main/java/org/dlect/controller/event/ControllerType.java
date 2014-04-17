/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.event;

import org.dlect.controller.helper.Controller;
import org.dlect.events.EventID;

/**
 *
 * @author lee
 */
public enum ControllerType implements EventID {

    LOGIN, SUBJECT, LECTURE;

    @Override
    public Class<?> getAppliedClass() {
        return Controller.class;
    }

    @Override
    public String getName() {
        return name();
    }

}
