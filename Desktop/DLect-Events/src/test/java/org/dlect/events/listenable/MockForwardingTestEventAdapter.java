/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.listenable;

import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventListener;
import org.mockito.Mockito;

/**
 *
 * @author lee
 */
public class MockForwardingTestEventAdapter implements EventAdapter {

    private static MockForwardingTestEventAdapter current;

    private final EventAdapter mock;

    public MockForwardingTestEventAdapter() {
        mock = Mockito.mock(EventAdapter.class, "Recording Test Event Adapter Mock");
        current = this;
    }

    public EventAdapter getMock() {
        return mock;
    }

    @Override
    public boolean addListener(EventListener l, Class<?>... listeningClasses) {
        return mock.addListener(l, listeningClasses);
    }

    @Override
    public boolean removeListener(EventListener l) {
        return mock.removeListener(l);
    }

    @Override
    public void setParentAdapter(EventAdapter e) {
        mock.setParentAdapter(e);
    }

    @Override
    public EventAdapter getParentAdapter() {
        return mock.getParentAdapter();
    }

    @Override
    public void fireEvent(Event e) {
        mock.fireEvent(e);
    }

    public static MockForwardingTestEventAdapter getCurrent() {
        return current;
    }

}
