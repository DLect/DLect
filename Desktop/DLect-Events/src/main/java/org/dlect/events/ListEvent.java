/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import java.util.Objects;

public class ListEvent extends Event {

    private final ListEventType listEventType;

    public ListEvent(Object source, EventID eventID, ListEventType listEventType, Object before, Object after) {
        super(source, eventID, before, after);
        this.listEventType = listEventType;
    }

    public ListEventType getListEventType() {
        return listEventType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.listEventType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ListEvent) {
            ListEvent other = (ListEvent) obj;

            return super.equalsImpl(other) && this.getListEventType() == other.getListEventType();
        } else {
            return false;
        }
    }

    public static ListEvent getAddEvent(Object source, EventID eid, Object added) {
        return new ListEvent(source, eid, ListEventType.ADDED, null, added);
    }

    public static ListEvent getRemoveEvent(Object source, EventID eid, Object removed) {
        return new ListEvent(source, eid, ListEventType.REMOVED, removed, null);
    }

    public static ListEvent getReplaceEvent(Object source, EventID eid, Object original, Object replacement) {
        return new ListEvent(source, eid, ListEventType.REPLACED, original, replacement);
    }

}
