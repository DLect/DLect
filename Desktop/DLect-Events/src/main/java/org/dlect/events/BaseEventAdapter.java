/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nonnull;

/**
 *
 * @author lee
 */
public class BaseEventAdapter implements EventAdapter {

    private EventAdapter parentAdapter;

    private final transient Set<EventListener> anyClassListeners;
    private final transient Multimap<Class<?>, EventListener> specificClassEventListeners;

    public BaseEventAdapter() {
        this.anyClassListeners = Sets.newHashSet();
        this.specificClassEventListeners = HashMultimap.create();
    }

    public BaseEventAdapter(Set<EventListener> anyClassListeners, Multimap<Class<?>, EventListener> specificClassEventListeners) {
        this.anyClassListeners = anyClassListeners;
        this.specificClassEventListeners = specificClassEventListeners;
    }

    @Override
    public boolean addListener(@Nonnull EventListener listener, Class<?>... listensTo) {
        if (listener == null) {
            throw new IllegalArgumentException("Null listener given to listen to " + Arrays.toString(listensTo));
        }
        synchronized (anyClassListeners) {
            if (anyClassListeners.contains(listener)) {
                // The listener is already listening to all events.
                return false;
            } else if (listensTo == null || listensTo.length == 0) {
                // Remove the listener from the classFiltered multimap to save time on event firing.
                removeListener(listener);
                return anyClassListeners.add(listener);
            } else {
                int size = specificClassEventListeners.size();
                for (Class<?> clz : listensTo) {
                    specificClassEventListeners.put(clz, listener);
                }
                /*
                 * If the size increased; then one of the additions was successful. This will not overlap with other
                 * adds as this is in a synchronised block.
                 */
                return size != specificClassEventListeners.size();
            }
        }
    }

    /**
     * This method checks for a loop contained in the given event adapter if it were added to {@code this}. This will
     * throw an {@link IllegalArgumentException} if a cycle is found.
     *
     * @param e
     */
    protected void checkForParentLoop(EventAdapter e) {
        Set<EventAdapter> adapters = Sets.newLinkedHashSet();
        // Linked to keep order if there is a problem.
        adapters.add(this);
        EventAdapter parent = e;
        while (parent != null) {
            if (adapters.contains(parent)) {
                throw new IllegalArgumentException("Encountered existing adapters. Adapter: " + parent + "; Parents(This is first): " + adapters);
            }
            adapters.add(parent);
            parent = parent.getParentAdapter();
        }
    }

    @Override
    public void fireEvent(Event e) {
        Set<EventListener> listeners = Sets.newHashSet();
        synchronized (anyClassListeners) {
            listeners.addAll(anyClassListeners);
            listeners.addAll(specificClassEventListeners.get(e.getSource().getClass()));
        }
        /*
         * Taken a copy of the listeners; don't care if anyone changes them; plus doing this outside the synchronised
         * block will allow other events to fire whilst this is processing.
         */
        for (EventListener el : listeners) {
            el.processEvent(e);
        }
    }

    @Override
    public EventAdapter getParentAdapter() {
        return this.parentAdapter;
    }

    @Override
    public void setParentAdapter(EventAdapter e) {
        if (e != null) {
            checkForParentLoop(e);
        }
        this.parentAdapter = e;
    }

    @Override
    public boolean removeListener(@Nonnull EventListener l) {
        synchronized (anyClassListeners) {
            boolean removed = anyClassListeners.remove(l);
            Collection<Entry<Class<?>, EventListener>> entries = specificClassEventListeners.entries();
            int size = entries.size();
            for (Iterator<Entry<Class<?>, EventListener>> it = entries.iterator(); it.hasNext();) {
                Entry<Class<?>, EventListener> entry = it.next();
                if (l.equals(entry.getValue())) {
                    it.remove();
                }
            }
            // If it was removed from the anyList or if the specificClassList changed size(an element was removed).
            return removed || size != entries.size();
        }
    }

}
