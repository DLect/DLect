/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import com.google.common.collect.Lists;
import java.util.List;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.events.listenable.Listenable;
import org.dlect.logging.TestLogging;

import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
public class RecordingEventListener implements EventListener {

    private final List<Event> recieved = Lists.newArrayList();
    private final List<Event> notAsserted = Lists.newArrayList();
    private final List<Event> asserted = Lists.newArrayList();

    public void assertEvent(Event evt) {
        if (!notAsserted.contains(evt)) {
            if (recieved.contains(evt)) {
                fail("Event " + evt + " already asserted sufficently. Recieved Events: " + recieved);
            } else {
                fail("Event " + evt + " was not recieved. Recieved Events: " + recieved);
            }
        } else {
            notAsserted.remove(evt);
            asserted.add(evt);
        }
    }

    public void noMoreEvents() {
        assertTrue("Events still left to assert: " + notAsserted, notAsserted.isEmpty());
    }

    @Override
    public void processEvent(Event e) {
        TestLogging.LOG.error("Event Fired: {}", e);
        recieved.add(e);
        notAsserted.add(e);
    }

    static RecordingEventListener addListener(Listenable<?> loaded) {
        RecordingEventListener l = new RecordingEventListener();
        loaded.addListener(l);
        return l;
    }

}
