/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import java.util.Objects;

public abstract class DataListEvent extends DataEvent {

    private final Object addedObject;
    private final Type type;

    /**
     *
     * @param origin
     * @param name
     * @param type
     * @param changed
     */
    protected DataListEvent(Object origin, String name, Type type, Object changed) {
        super(origin, name, (type == Type.OBJECT_ADDED ? null : changed), (type == Type.OBJECT_ADDED ? changed : null));
        this.addedObject = changed;
        this.type = type;
    }

    public Object getChangedObject() {
        return addedObject;
    }

    public Type getChangeType() {
        return type;
    }

    public Class<?> getListClass() {
        // TODO change this to get it.
        return addedObject.getClass();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + Objects.hashCode(this.addedObject);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataListEvent)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final DataListEvent other = (DataListEvent) obj;
        if (!Objects.equals(this.getChangedObject(), other.getChangedObject())) {
            return false;
        }
        return this.getChangeType() == other.getChangeType();
    }

    public static enum Type {

        OBJECT_ADDED, OBJECT_REMOVED;
    }
}
