/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.testAdapter;

import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventListener;

/**
 *
 * @author lee
 */
public class TestEventAdapter implements EventAdapter {

    private EventAdapter parentAdapter;

    @Override
    public boolean addListener(EventListener l, Class<?>... listeningClasses) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fireEvent(Event e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EventAdapter getParentAdapter() {
        return this.parentAdapter;  
    }

    @Override
    public void setParentAdapter(EventAdapter e) {
        this.parentAdapter = e;
    }

    @Override
    public boolean removeListener(EventListener l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
