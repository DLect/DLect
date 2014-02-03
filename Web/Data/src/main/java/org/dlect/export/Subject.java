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
import java.util.Collection;
import java.util.SortedSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import static org.dlect.helpers.StoresHelper.normalSorting;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "subject")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Subject implements Comparable<Subject> {

    @XmlElement(name = "id")
    @XmlID
    private String subjectId;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "description")
    private String description;

    @XmlIDREF
    @XmlElement(name = "semester")
    private Semester semesterById;

    @XmlElement(name = "semester")
    private Semester semesterByData;

    @XmlElementWrapper(name = "lectures")
    @XmlElement(name = "lecture")
    private SortedSet<Lecture> lectures;

    @XmlElementWrapper(name = "streams")
    @XmlElement(name = "stream")
    private SortedSet<Stream> streams;

    @Override
    public int compareTo(Subject o) {
        return ComparisonChain.start().compare(getSemester(), o.getSemester()).compare(name, o.getName(), normalSorting()).result();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SortedSet<Lecture> getLectures() {
        return ImmutableSortedSet.copyOf(lectures);
    }

    public void setLectures(Collection<Lecture> lectures) {
        this.lectures = Sets.newTreeSet(lectures);
    }

    public Semester getSemester() {
        return getSemesterById();
    }

    public void setSemester(Semester semester) {
        setSemesterById(semester);
    }

    public Semester getSemesterByData() {
        return semesterByData;
    }

    public void setSemesterByData(Semester semesterByData) {
        this.semesterByData = semesterByData;
    }

    public Semester getSemesterById() {
        return semesterById;
    }

    public void setSemesterById(Semester semesterById) {
        this.semesterById = semesterById;
    }

    public SortedSet<Stream> getStreams() {
        return ImmutableSortedSet.copyOf(streams);
    }

    public void setStreams(Collection<Stream> streams) {
        this.streams = Sets.newTreeSet(streams);
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = Long.toString(subjectId, 10);
    }

}
