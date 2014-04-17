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

import com.google.common.collect.ComparisonChain;
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
import javax.xml.bind.annotation.XmlIDREF;

import static org.dlect.helpers.StoresHelper.normalSorting;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Lecture implements Comparable<Lecture> {

    @XmlID
    @XmlElement(name="id")
    private String id;
    
    @XmlElement(name = "recorded")
    private Date recordDate;

    @XmlElement(name="location")
    private String location;
    
    @XmlElementWrapper(name="contents")
    @XmlElement(name="content")
    private List<LectureContent> contents;

    @XmlElementWrapper(name = "streams")
    @XmlElement(name = "stream")
    @XmlIDREF
    private SortedSet<Stream> stream;

    public List<LectureContent> getContents() {
        return contents;
    }

    public void setContents(List<LectureContent> contents) {
        this.contents = contents;
    }

    
    public Date getRecordDate() {
        return recordDate == null ? null : new Date(recordDate.getTime());
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate == null ? null : new Date(recordDate.getTime());
    }

    public SortedSet<Stream> getStream() {
        return stream == null ? null : ImmutableSortedSet.copyOf(stream);
    }

    public void setStream(SortedSet<Stream> stream) {
        this.stream = stream == null ? null : Sets.newTreeSet(stream);
    }

    
    @Override
    public int compareTo(Lecture o) {
        return ComparisonChain.start().compare(recordDate, o.getRecordDate(), normalSorting()).result();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Lecture) {
            return this.compareTo((Lecture) obj) == 0;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.recordDate);
        return hash;
    }

    @Override
    public String toString() {
        return "Lecture{" + "recordDate=" + recordDate + ", contents=" + contents + ", stream=" + stream + '}';
    }


}
