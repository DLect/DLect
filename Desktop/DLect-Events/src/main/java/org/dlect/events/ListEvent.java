/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import com.google.common.base.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is a specific type of event for changes to lists. This class provides an additional method to determine
 * the type of list change that this event represents.
 *
 */
public class ListEvent extends Event {

    private final ListEventType listEventType;

    /**
     * Creates a new List event.
     *
     * @param source        The source of the event.
     * @param eventID       The ID of the fired event.
     * @param listEventType The type of list event being fired.
     * @param before        The object before the change(conventionally {@code null} in the case of an addition).
     * @param after         The object after the change(conventionally {@code null} in the case of a removal).
     *
     * @see #getAddEvent(java.lang.Object, org.dlect.events.EventID, java.lang.Object)
     * @see #getRemoveEvent(java.lang.Object, org.dlect.events.EventID, java.lang.Object)
     * @see #getReplaceEvent(java.lang.Object, org.dlect.events.EventID, java.lang.Object, java.lang.Object)
     */
    public ListEvent(@Nonnull Object source, @Nonnull EventID eventID, @Nonnull ListEventType listEventType,
                     @Nullable Object before, @Nullable Object after) {
        super(source, eventID, before, after);
        if (listEventType == null) {
            throw new IllegalArgumentException("Null list type given for event");
        }
        this.listEventType = listEventType;
    }

    /**
     * Gets the type of collection event that this event object represents.
     *
     * @return The type of event that this represents.
     */
    public ListEventType getListEventType() {
        return listEventType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), this.getListEventType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (Objects.equal(obj.getClass(), this.getClass())) {
            ListEvent other = (ListEvent) obj;

            return super.equalsImpl(other) && this.getListEventType() == other.getListEventType();
        } else {
            return false;
        }
    }

    /**
     * Creates a new addition list event with the given source and event ID.
     *
     * @param source The source of the event.
     * @param eid    The event ID of the event.
     * @param added  The, possibly null, object that was added.
     *
     * @return The new list event.
     *
     * @see Event#Event(java.lang.Object, org.dlect.events.EventID, java.lang.Object, java.lang.Object)
     */
    public static ListEvent getAddEvent(@Nonnull Object source, @Nonnull EventID eid, @Nullable Object added) {
        return new ListEvent(source, eid, ListEventType.ADDED, null, added);
    }

    /**
     * Creates a new removal list event with the given source and event ID.
     *
     * @param source  The source of the event.
     * @param eid     The event ID of the event.
     * @param removed The, possibly null, object that was removed.
     *
     * @return The new list event.
     *
     * @see Event#Event(java.lang.Object, org.dlect.events.EventID, java.lang.Object, java.lang.Object)
     */
    public static ListEvent getRemoveEvent(@Nonnull Object source, @Nonnull EventID eid, @Nullable Object removed) {
        return new ListEvent(source, eid, ListEventType.REMOVED, removed, null);
    }

    /**
     * Creates a new replacement list event with the given source and event ID. This method does not check if the
     * original and replacement are the same.
     *
     * @param source      The source of the event.
     * @param eid         The event ID of the event.
     * @param original    The, possibly null, object that originally in the location.
     * @param replacement The, possibly null, object that replaced the original object.
     *
     * @return The new list event.
     *
     * @see Event#Event(java.lang.Object, org.dlect.events.EventID, java.lang.Object, java.lang.Object)
     */
    public static ListEvent getReplaceEvent(@Nonnull Object source, @Nonnull EventID eid,
                                            @Nullable Object original, @Nullable Object replacement) {
        return new ListEvent(source, eid, ListEventType.REPLACED, original, replacement);
    }

    @Override
    public String toString() {
        return "ListEvent{" + super.debugVars() + ", listEventType=" + listEventType + '}';
    }

}
