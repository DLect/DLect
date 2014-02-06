/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import com.google.common.collect.IdentityHashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nonnull;

/**
 * Note that this method relies on object identity as opposed to object equality, however this may change soon.
 *
 * @param <T> The upper bound of event that this listener accepts.
 */
public class EventAcceptor<T extends Event> {

    @Nonnull
    private transient final SetMultimap<Class<?>, EventListener<? super T>> classFilteredListeners;

    private final Set<EventListener<? super T>> listeners;

    /**
     * A fair read/write lock that allows for re-entry into a locked block.
     */
    private transient final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);

    private EventAcceptor<? super T> parentAcceptor;

    public EventAcceptor() {
        this.classFilteredListeners = IdentityHashMultimap.create();
        this.listeners = Sets.newIdentityHashSet();
    }

    public EventAcceptor(EventAcceptor<? super T> parentAcceptor) {
        this();
        this.parentAcceptor = parentAcceptor;
    }

    public EventAcceptor(SetMultimap<Class<?>, EventListener<? super T>> classFilteredListeners, Set<EventListener<? super T>> listeners, EventAcceptor<? super T> parentAcceptor) {
        this(classFilteredListeners, listeners);
        this.parentAcceptor = parentAcceptor;
    }

    public EventAcceptor(SetMultimap<Class<?>, EventListener<? super T>> classFilteredListeners, Set<EventListener<? super T>> listeners) {
        if (classFilteredListeners == null) {
            throw new IllegalArgumentException("Null classFilteredDataListeners.");
        }
        if (listeners == null) {
            throw new IllegalArgumentException("Null dataListener.");
        }
        this.classFilteredListeners = classFilteredListeners;
        this.listeners = listeners;
    }

    /**
     *
     * @param listener  A non-null listener for data events. If listener is null then this method's actions are not
     *                  defined.
     * @param listensTo If this is empty, then listens to all events passing through this object; otherwise only listens
     *                  to events matching the classes listed.
     *
     * @return {@code true} iff the listener was successfully added.
     */
    public boolean addListener(@Nonnull EventListener<? super T> listener, Class<?>... listensTo) {
        if (listener == null) {
            throw new IllegalArgumentException("Null listener given to listen to " + Arrays.toString(listensTo));
        }
        rwl.writeLock().lock();
        try {
            if (listeners.contains(listener)) {
                // The listener is already listening to all events.
                return false;
            } else if (listensTo == null || listensTo.length == 0) {
                // Remove the listener from the classFiltered multimap to save time on event firing.
                removeListener(listener);
                return listeners.add(listener);
            } else {
                int size = classFilteredListeners.size();
                for (Class<?> clz : listensTo) {
                    classFilteredListeners.put(clz, listener);
                }
                /*
                 * If the size increased; then one of the additions was successful. This will not overlap with other
                 * adds as this method has aquired write lock.
                 */
                return size != classFilteredListeners.size();
            }
        } finally {
            rwl.writeLock().unlock();
        }
    }
    
    /**
     *
     * @param listener A non-null listener. If this listener is null then no actions are performed.
     */
    public void removeListener(@Nonnull EventListener<? super T> listener) {
        rwl.writeLock().lock();
        try {
            Set<Entry<Class<?>, EventListener<? super T>>> entries = classFilteredListeners.entries();
            for (Iterator<Entry<Class<?>, EventListener<? super T>>> it = entries.iterator(); it.hasNext();) {
                Entry<Class<?>, EventListener<? super T>> entry = it.next();
                // Checking for identity.
                if (listener == entry.getValue()) {
                    it.remove();
                }
            }
            listeners.remove(listener);
        } finally {
            rwl.writeLock().unlock();
        }
    }


    /**
     * This method will not allow a loop in the parent tree(where {@code this} appears as a parent of itself). This
     * method does not check for loops other than loops connecting {@code this} into itself. It is assumed that all the
     * parents about this one are valid. Failing to adhere to this pre-condition will result in an infinite loop without
     * stack overflow.
     *
     * @param parentAcceptor
     */
    public void setParent(EventAcceptor<? super T> parentAcceptor) {
        // TODO check if we have a common parent and maintain the shortest link to it.
        // E.G. Lecture & Stream
        validateParent(parentAcceptor);
        this.parentAcceptor = parentAcceptor;
    }

    public EventAcceptor<? super T> getParent() {
        return this.parentAcceptor;
    }

    public <E extends T> void fireEvent(E event) {
        for (EventListener<? super T> listener : filterListeners(event)) {
            listener.eventFired(event);
        }
        if (this.parentAcceptor != null) {
            this.parentAcceptor.fireEvent(event);
        }
    }
    
    protected Set<EventListener<? super T>> filterListeners(T e) {
        rwl.readLock().lock();
        try {
            HashSet<EventListener<? super T>> ret = Sets.newHashSet(listeners);
            for (Class<?> clz : classFilteredListeners.keySet()) {
                if (clz.equals(e.getOriginClass())) {
                    ret.addAll(classFilteredListeners.get(clz));
                }
            }
            return ret;
        } finally {
            rwl.readLock().unlock();
        }
    }

    protected final void validateParent(EventAcceptor<? super T> parentAcceptor) {
        validateParentNotThis(parentAcceptor);
        EventAcceptor<?> parent = parentAcceptor; // If `parentAcceptor` is null then don't enter loop
        while (parent != null) {
            // `parent` not null here. Then validate it.
            parent = parent.getParent();
            validateParentNotThis(parent);
        }
    }

    protected final void validateParentNotThis(EventAcceptor<?> parentAcceptor) {
        if (parentAcceptor == this) {
            throw new IllegalArgumentException("Trying to set parent to `this`. Don't do it, it causes Stack Overflow Exceptions.");
        }
    }

}
