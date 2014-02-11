/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Maps;
import java.util.ConcurrentModificationException;
import java.util.Map;
import javax.annotation.Nonnull;
import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventID;
import org.dlect.events.ListEvent;

/**
 * A helper object that handles the actual event firing for collections. This object also prevents event handlers from
 * modifying the same collection. Thus hopefully mitigating the risk of concurrent modification exceptions at a later
 * time.
 *
 * @param <T> The type of object that this class accepts.
 */
public class CollectionEventHelper<T> {

    private final EventAdapter adapter;
    private final EventID eventID;
    private final Object source;

    private final transient Map<Thread, Event> firingEvents = Maps.newHashMap();

    /**
     * Creates a new helper using the given eventID, source and adapter. All events fired through this helper will be
     * fired to the given event adapter and will be initialised with the given event ID and source.
     *
     * @param eventID The event ID of the events that this object will fire.
     * @param source  The source of the events the this object will fire
     * @param adapter The adapter to fire the events through.
     *
     * @throws IllegalArgumentException If any argument is null.
     */
    public CollectionEventHelper(@Nonnull EventID eventID, @Nonnull Object source, @Nonnull EventAdapter adapter) {
        if (eventID == null) {
            throw new IllegalArgumentException("EventID is null");
        }
        if (source == null) {
            throw new IllegalArgumentException("Source is null");
        }
        if (adapter == null) {
            throw new IllegalArgumentException("Event Adapter is null");
        }
        this.eventID = eventID;
        this.source = source;
        this.adapter = adapter;
    }

    protected void beginChange(Event toFire) {
        Thread t = Thread.currentThread();
        synchronized (firingEvents) {
            if (isLocked(t)) {
                throw new ConcurrentModificationException("Attempting to modify a collection from within an event handler is prohibited. Old Event: " + firingEvents.get(t) + "; New Event: " + toFire);
            } else {
                firingEvents.put(t, toFire);
            }
        }
    }

    protected boolean isLocked(Thread t) {
        synchronized (firingEvents) {
            return firingEvents.containsKey(t);
        }
    }

    protected boolean isLocked() {
        return isLocked(Thread.currentThread());
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

    /**
     * Fires an addition event to this object's adapter. This method must be called after the change has occurred in
     * the list. This method enforces that not EventListener may modify the collection from inside the event handler
     * handling an event from the associated collection.
     *
     * @param addedElement The object added to the associated collection
     *
     * @see ListEvent#getAddEvent(java.lang.Object, org.dlect.events.EventID, java.lang.Object)
     * @see EventAdapter#fireEvent(org.dlect.events.Event)
     */
    public void fireAdd(T addedElement) {
        final ListEvent event = ListEvent.getAddEvent(source, eventID, addedElement);
        beginChange(event);
        try {
            adapter.fireEvent(event);
        } finally {
            endChange();
        }
    }

    /**
     * Fires a replacement event to this object's adapter. This method must be called after the change has occurred in
     * the list. This method enforces that not EventListener may modify the collection from inside the event handler
     * handling an event from the associated collection.
     *
     * @param removedElement The object removed from the associated collection
     *
     * @see ListEvent#getRemoveEvent(java.lang.Object, org.dlect.events.EventID, java.lang.Object)
     * @see EventAdapter#fireEvent(org.dlect.events.Event)
     */
    public void fireRemove(T removedElement) {
        final ListEvent event = ListEvent.getRemoveEvent(source, eventID, removedElement);
        beginChange(event);
        try {
            adapter.fireEvent(event);
        } finally {
            endChange();
        }
    }

    /**
     * Fires a replacement event to this object's adapter. This method must be called after the change has occurred in
     * the list. This method enforces that not EventListener may modify the collection from inside the event handler
     * handling an event from the associated collection.
     *
     * @param original    The original object before it was replaced.
     * @param replacement The object replacing the original object.
     *
     * @see ListEvent#getReplaceEvent(java.lang.Object, org.dlect.events.EventID, java.lang.Object, java.lang.Object)
     * @see EventAdapter#fireEvent(org.dlect.events.Event)
     */
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
