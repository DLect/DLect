/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Maps;
import java.util.ConcurrentModificationException;
import java.util.Map;
import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventID;
import org.dlect.events.ListEvent;

/**
 *
 * @author lee
 * @param <T> The type of object that this class accepts.
 */
public class CollectionEventHelper<T> {

    private final EventAdapter adapter;
    private final EventID eventID;
    private final Object source;

    private final transient Map<Thread, Event> firingEvents = Maps.newHashMap();

    public CollectionEventHelper(EventID event, Object source, EventAdapter adapter) {
        this.eventID = event;
        this.source = source;
        this.adapter = adapter;
    }

    protected void beginChange(Event toFire) {
        Thread t = Thread.currentThread();
        synchronized (firingEvents) {
            if (firingEvents.containsKey(t)) {
                throw new ConcurrentModificationException("Attempting to modify a collection from within an event handler is prohibited. Old Event: " + firingEvents.get(t) + "; New Event: " + toFire);
            } else {
                firingEvents.put(t, toFire);
            }
        }
    }

    protected boolean isLocked() {
        synchronized (firingEvents) {
            return firingEvents.containsKey(Thread.currentThread());
        }
    }

    protected void endChange() {
        Thread t = Thread.currentThread();
        synchronized (firingEvents) {
            if (firingEvents.containsKey(t)) {
                firingEvents.remove(t);
            } else {
                throw new IllegalStateException("Change ended but thread not recorded as started.");
            }
        }
    }

    public void fireAdd(T addedElement) {
        final ListEvent event = ListEvent.getAddEvent(source, eventID, addedElement);
        beginChange(event);
        try {
            adapter.fireEvent(event);
        } finally {
            endChange();
        }
    }

    public void fireRemove(T removedElement) {
        final ListEvent event = ListEvent.getRemoveEvent(source, eventID, removedElement);
        beginChange(event);
        try {
            adapter.fireEvent(event);
        } finally {
            endChange();
        }
    }

    public void fireReplace(T original, T replacement) {
        final ListEvent event = ListEvent.getReplaceEvent(source, eventID, original, replacement);
        beginChange(event);
        try {
            adapter.fireEvent(event);
        } finally {
            endChange();
        }
    }

}
