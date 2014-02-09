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
            throw new NullPointerException("Trying to fire an event with a null source");
        }
        if (eventID == null) {
            throw new NullPointerException("Trying to fire an event with a null eventID");
        }
        if (!eventID.getAppliedClass().isAssignableFrom(source.getClass())) {
            // If the event ID represents a supertype of or equal to source class; then good. Otherwise error.
            throw new IllegalArgumentException("Source(" + source.getClass() + ") is not a sub-type of the class that the event ID(" + eventID.getAppliedClass() + ") applies to.");
        }
        // TODO consider adding check that getSource().getClass() == getEventID().getAppliedClass()
        this.source = source;
        this.eventID = eventID;
        this.before = before;
        this.after = after;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.source);
        hash = 97 * hash + Objects.hashCode(this.eventID);
        hash = 97 * hash + Objects.hashCode(this.before);
        hash = 97 * hash + Objects.hashCode(this.after);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Event)) {
            return false;
        }
        final Event other = (Event) obj;
        if (!Objects.equals(this.getSource(), other.getSource())) {
            return false;
        }
        if (!Objects.equals(this.getEventID(), other.getEventID())) {
            return false;
        }
        if (!Objects.equals(this.getBefore(), other.getBefore())) {
            return false;
        }
        return Objects.equals(this.getAfter(), other.getAfter());
    }

    public Class<?> getSourceClass() {
        // TODO decide between getSource().getClass() and getEventID().getAppliedClass()
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
