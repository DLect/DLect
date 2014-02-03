/*
 * This file is part of DLect. DLect is a suite of code that facilitates the downloading of lecture recordings.
 *
 * Copyright © 2014 Lee Symes.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dlect.export;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Stream implements Comparable<Stream> {

    @XmlID
    @XmlElement(name = "name")
    String name;

    @XmlElementWrapper(name = "at")
    @XmlElement(name = "time")
    SortedSet<Date> times;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SortedSet<Date> getTimes() {
        return ImmutableSortedSet.copyOf(times);
    }

    public void setTimes(List<Date> times) {
        this.times = Sets.newTreeSet(times);
    }

    @Override
    public int compareTo(Stream o) {
        return name.compareToIgnoreCase(o.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Stream) {
            return this.compareTo((Stream) obj) == 0;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public String toString() {
        return "Stream{" + "name=" + name + ", times=" + times + '}';
    }

}
