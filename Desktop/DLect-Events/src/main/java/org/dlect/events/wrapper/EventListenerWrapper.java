/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.wrapper;

import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventListener;

/**
 *
 * @author lee
 */
public abstract class EventListenerWrapper implements EventListener {

    private final EventAdapter addedTo;

    public EventListenerWrapper(EventAdapter addedTo) {
        this.addedTo = addedTo;
    }

    @Override
    public void processEvent(Event e) {
        processEventImpl(e);
    }

    protected void processEventImpl(Event e) {
        EventListener wrapped = getWrappedListener();
        if (checkListener(wrapped)) {
            wrapped.processEvent(e);
        }
    }

    protected boolean checkListener() {
        return checkListener(getWrappedListener());
    }

    protected boolean checkListener(EventListener wrapped) {
        if (wrapped == null || wrapped == this) {
            removeSelfFromAdapter();
            return false;
        }
        return true;
    }

    public abstract EventListener getWrappedListener();

    protected void removeSelfFromAdapter() {
        addedTo.removeListener(this);
    }

}
