/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.event;

import javax.annotation.Nonnull;
import org.dlect.events.Event;
import org.dlect.helper.Conditions;

/**
 *
 * @author lee
 */
public class ControllerEvent extends Event {

    private final Object parameter;
    private final ControllerState state;
    private final ControllerType eventID;

    public ControllerEvent(@Nonnull Object source, @Nonnull ControllerType eventID, Object parameter, @Nonnull ControllerState state) {
        super(source, eventID, null, state);
        Conditions.checkNonNull(state, "Controller State");
        this.parameter = parameter;

        this.state = state;
        this.eventID = eventID;
    }

    @Override
    @Nonnull
    public ControllerState getAfter() {
        return state;
    }

    public Object getParameter() {
        return parameter;
    }

    @Deprecated
    @Override
    public Object getBefore() {
        return null;
    }

    @Override
    public ControllerType getEventID() {
        return eventID;
    }

    @Override
    public String toString() {
        return "ControllerEvent{" + "source=" + getSource() + ", eventID=" + eventID + ", state=" + state + ", parameter=" + parameter + '}';
    }

}
