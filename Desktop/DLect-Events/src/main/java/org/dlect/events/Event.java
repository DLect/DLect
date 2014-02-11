/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import java.util.Objects;
import javax.annotation.Nonnull;

/**
 *
 * @author lee
 */
public class Event {

    private final Object source;
    private final EventID eventID;
    private final Object before;
    private final Object after;

    public Event(@Nonnull Object source, @Nonnull EventID eventID, Object before, Object after) {
        if (source == null) {
            throw new IllegalArgumentException("Trying to fire an event with a null source");
        }
        if (eventID == null) {
            throw new IllegalArgumentException("Trying to fire an event with a null eventID");
        }
        if (!eventID.getAppliedClass().isAssignableFrom(source.getClass())) {
            // If the event ID represents a supertype of or equal to source class; then good. Otherwise error.
            throw new IllegalArgumentException("Source(" + source.getClass() + ") is not a sub-type of the class that the event ID(" + eventID.getAppliedClass() + ") applies to.");
        }
        this.source = source;
        this.eventID = eventID;
        this.before = before;
        this.after = after;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getSource(),
                            this.getEventID(),
                            this.getBefore(),
                            this.getAfter());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Event) {
            final Event other = (Event) obj;
            return equalsImpl(other);
        } else {
            return false;
        }
    }

    protected <T extends Event> boolean equalsImpl(T other) {
        return Objects.equals(this.getSource(), other.getSource())
               && Objects.equals(this.getEventID(), other.getEventID())
               && Objects.equals(this.getBefore(), other.getBefore())
               && Objects.equals(this.getAfter(), other.getAfter());
    }

    public Class<?> getSourceClass() {
        return eventID.getAppliedClass();
    }

    public Object getSource() {
        return source;
    }

    public EventID getEventID() {
        return eventID;
    }

    public Object getBefore() {
        return before;
    }

    public Object getAfter() {
        return after;
    }

}
