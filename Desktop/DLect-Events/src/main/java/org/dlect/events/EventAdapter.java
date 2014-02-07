/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import javax.annotation.Nonnull;

/**
 * An adaptor that allows for events to be fired to listeners.
 *
 * Note that all event listeners are compared using the {@linkplain EventListener#equals(java.lang.Object)} method not
 * object identity.
 *
 * @author lee
 */
public interface EventAdapter {

    /**
     * Adds the given event listener to this class, with an optional filtering by event origin classes.
     * <p>
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
     * Removes the event listener from this adaptor.
     *
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
     * If this adaptor is not {@code null} then this method will check for a cycle in the parent tree; throwing an
     * exception if a cycle is found.
     *
     * @param e The adaptor to be set as parent adapter. If this is null then it will remove the parent.
     *
     * @throws IllegalArgumentException If a cycle(an {@link EventAdapter} is a parent of itself) is detected.
     */
    public void setParentAdapter(EventAdapter e);

    public EventAdapter getParentAdapter();

    public void fireEvent(Event e);

}
