/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;
import org.dlect.events.EventID;
import org.dlect.events.listenable.EventBuilder;
import org.dlect.model.helper.XmlListenable;

/**
 *
 * @author lee
 */
public class Stream extends XmlListenable<Stream> implements Comparable<Stream> {

    private String name;

    public Stream() {
    }

    @Override
    public int compareTo(Stream o) {
        if (o == null) {
            return 1;
        }
        return Ordering.natural().nullsLast().compare(this.getName(), o.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        EventBuilder<String> b = event(StreamEventID.NAME).before(getName());
        this.name = name;
        b.after(getName()).fire();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stream other = (Stream) obj;
        return this.getName().equalsIgnoreCase(other.getName());
    }

    @Override
    public String toString() {
        return "Stream{" + "name=" + getName() + '}';
    }

    public static enum StreamEventID implements EventID {

        NAME;

        @Override
        public Class<?> getAppliedClass() {
            return Stream.class;
        }

        @Override
        public String getName() {
            return name();
        }


    }

}
