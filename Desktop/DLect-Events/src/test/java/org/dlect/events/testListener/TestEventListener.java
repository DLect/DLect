/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.events.testListener;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.dlect.events.Event;
import org.dlect.events.EventListener;

/**
 *
 * @author lee
 */
public class TestEventListener implements EventListener {

    private final List<Event> events = Lists.newArrayList();

    public TestEventListener() {
    }

    /**
     * 
     * @return A changeable view of the events list.
     */
    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    
    
    @Override
    public void processEvent(Event e) {
        events.add(e);
    }
    
}
