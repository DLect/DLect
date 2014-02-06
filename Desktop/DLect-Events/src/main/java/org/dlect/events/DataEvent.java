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
public class DataEvent implements Event {

    private final String name;
    private final Object oldObj;
    private final Object newObj;
    private final Object origin;
    private final Class<? extends Object> originClass;

    protected DataEvent(Object origin, String name, Object oldObj, Object newObj) {
        this.origin = origin;
        this.originClass = origin.getClass();
        this.name = name;
        this.oldObj = oldObj;
        this.newObj = newObj;
    }

    @Override
    public Object getOrigin() {
        return origin;
    }

    @Override
    public Class<?> getOriginClass() {
        return originClass;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataEvent other = (DataEvent) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.oldObj, other.oldObj)) {
            return false;
        }
        if (!Objects.equals(this.newObj, other.newObj)) {
            return false;
        }
        if (!Objects.equals(this.origin, other.origin)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.oldObj);
        hash = 79 * hash + Objects.hashCode(this.newObj);
        hash = 79 * hash + Objects.hashCode(this.origin);
        return hash;
    }

}
