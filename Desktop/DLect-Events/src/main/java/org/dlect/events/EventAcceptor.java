/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import com.google.common.collect.Sets;
import java.util.Set;

/**
 *
 * @author lee
 * @param <T>
 */
public class EventAcceptor<T extends Event> {

    private final Set<EventListener<? super T>> listeners = Sets.newIdentityHashSet();

    private EventAcceptor<? super T> parentAcceptor;

    public boolean addListener(EventListener<? super T> listener) {
        return this.listeners.add(listener);
    }

    public boolean removeListener(EventListener<? super T> listener) {
        return this.listeners.remove(listener);
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
        validateParent(parentAcceptor);
        this.parentAcceptor = parentAcceptor;
    }

    public EventAcceptor<? super T> getParent() {
        return this.parentAcceptor;
    }

    public <E extends T> void fireEvent(E event) {
        for (EventListener<? super T> listener : this.listeners) {
            listener.eventFired(event);
        }
        if (this.parentAcceptor != null) {
            this.parentAcceptor.fireEvent(event);
        }
    }

    protected final void validateParent(EventAcceptor<? super T> parentAcceptor) {
        validateParentNotThis(parentAcceptor);
        EventAcceptor<?> parent = parentAcceptor;
        while (parent != null) {
            validateParentNotThis(parent);
        }
    }

    protected final void validateParentNotThis(EventAcceptor<?> parentAcceptor) {
        if (parentAcceptor == this) {
            throw new IllegalArgumentException("Trying to set parent to `this`. Don't do it, it causes Stack Overflow Exceptions.");
        }
    }

}
