/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import com.google.common.base.Objects;
import org.dlect.events.EventID;
import org.dlect.events.listenable.EventBuilder;
import org.dlect.model.helper.XmlListenable;

/**
 *
 * @author lee
 */
public class Stream extends XmlListenable<Stream> implements Comparable<Stream> {

    private String name;
    private long number;

    public Stream() {
    }

    @Override
    public int compareTo(Stream o) {
        if (o == null) {
            return 1;
        }
        return Long.compare(this.getNumber(), o.getNumber());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        EventBuilder<String> b = event(StreamEventID.NAME).before(getName());
        this.name = name;
        b.after(getName()).fire();
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        EventBuilder<Long> b = event(StreamEventID.NUMBER).before(getNumber());
        this.number = number;
        b.after(getNumber()).fire();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
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
        return this.getNumber() == other.getNumber();
    }

    @Override
    public String toString() {
        return "Stream{" + "name=" + name + ", number=" + number + '}';
    }
    

    public static enum StreamEventID implements EventID {

        NAME, NUMBER;

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
