/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An adaptor that allows for events to be fired to listeners.
 *
 * <p />
 * Note that all event listeners are compared using the {@linkplain EventListener#equals(java.lang.Object)} method not
 * object identity.
 *
 * @author lee
 */
public interface EventAdapter {

    /**
     * Adds the given event listener to this class, with an optional filtering by event origin classes.
     * <p />
     * All implementers must uphold the following contract:
     * <ul>
     * <li>If no listening classes are given then the event listener will be fired for all events passing this adaptor.
     * </li>
     * <li>Otherwise (as in one or more listening classes are given) the given event listener will be fired for any
     * event whose origin's class is equal to any of the classes given. This action is additive so calling this method
     * with the same event listener and different listening classes will cause this event listener to be called on the
     * union of all the given listening classes.</li>
     * </ul>
     *
     * @param l                The event listener to attach to this adapter and is to be notified of events as per the
     *                         contract above.
     * @param listeningClasses A possibly empty list of classes that the given event listener wants to be notified on.
     *                         The notification strategy is given in the contract above.
     *
     * @return {@code true} iff the listener's
     */
    public boolean addListener(@Nonnull EventListener l, Class<?>... listeningClasses);

    /**
     * Removes the event listener from this adaptor. This method may be safely called from
     * {@link EventListener#processEvent(org.dlect.events.Event) } without exception.
     *
     * <p />
     * Calling this method guarantees that any events fired after this method completes will not notify the given event
     * listener.
     *
     * @param l The event listener to remove from the adapter.
     *
     * @return {@code true} iff the event listener was present before the method call.
     */
    public boolean removeListener(EventListener l);

    /**
     * Sets the given {@link EventAdapter} as the parent of this adaptor; or resets it if the given adapter is null.
     *
     * <p />
     * If this adaptor is not {@code null} then this method will check for a cycle in the parent tree; throwing an
     * exception if a cycle is found. Implementers must check for a cycle anywhere in the tree; not just
     *
     * @param e The adaptor to be set as parent adapter. If this is null then it will remove the parent.
     *
     * @throws IllegalArgumentException If a cycle(an {@link EventAdapter} is a parent of itself) is detected.
     */
    public void setParentAdapter(EventAdapter e);

    /**
     * Gets the current parent adapter or {@code null} if none is defined.
     *
     * @return The parent adapter of {@code null} if none is defined.
     */
    @Nullable
    public EventAdapter getParentAdapter();

    /**
     * Fires the given event to all the event listeners and then fires the same event on this adapters parent(given one
     * exists).
     *
     * <p />
     * Implementers should respect the filtering given for event listeners added using
     * {@link #addListener(org.dlect.events.EventListener, java.lang.Class...) }. The implementer should also fire the
     * event to the parent using this method after firing to all of this adapter's event listeners. Implementers should
     * not copy the event as the event itself is immutable.
     *
     * A cycle check should not be required as it is a requirement for implementers to perform this check when their
     * parent is set. This also means that classes should not store the event internally as the event will never pass
     * through this method again.
     *
     * @param e
     */
    public void fireEvent(@Nonnull Event e);

}
