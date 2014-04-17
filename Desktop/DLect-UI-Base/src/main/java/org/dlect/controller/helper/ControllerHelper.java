/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper;

import com.google.common.collect.ImmutableList;
import org.dlect.controller.event.ControllerType;
import org.dlect.events.Event;
import org.dlect.events.EventID;

/**
 *
 * @author lee
 */
public class ControllerHelper {

    public static final ImmutableList<EventID> CONTROLLER_EVENT_IDS = ImmutableList
            .<EventID>copyOf(ControllerType.values());

    public static boolean isControllerEvent(Event e) {
        return isControllerEventID(e.getEventID());
    }

    public static boolean isControllerEventID(EventID eventID) {
        return CONTROLLER_EVENT_IDS.contains(eventID);
    }

}
