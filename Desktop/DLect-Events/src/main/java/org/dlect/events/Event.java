/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import java.util.Objects;

/**
 *
 * @author lee
 */
public class Event {

    private final Object source;
    private final EventID eventID;
    private final Object before;
    private final Object after;

    public Event(Object source, EventID eventID, Object before, Object after) {
        if (source == null) {
            throw new IllegalArgumentException("Trying to fire an event with a null source");
        }
        if (eventID == null) {
            throw new IllegalArgumentException("Trying to fire an event with a null eventID");
        }
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
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
        if (!Objects.equals(this.getAfter(), other.getAfter())) {
            return false;
        }
        return true;
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
