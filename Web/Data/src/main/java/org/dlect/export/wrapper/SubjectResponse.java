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
package org.dlect.export.wrapper;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.SortedSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.export.Semester;
import org.dlect.export.Subject;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "subjects")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubjectResponse {

    @XmlElement(name = "subject")
    private SortedSet<Subject> subjects;

    @XmlElementWrapper(name = "semesters")
    @XmlElement(name = "semester")
    private SortedSet<Semester> semesters;

    public SubjectResponse() {
    }
    
    public SubjectResponse(Collection<Subject> subjects, Collection<Semester> semesters) {
        this.subjects = Sets.newTreeSet(subjects);
        this.semesters = Sets.newTreeSet(semesters);
    }

    public SortedSet<Subject> getSubjects() {
        return ImmutableSortedSet.copyOf(subjects);
    }

    public void setSubjects(Collection<Subject> subjects) {
        this.subjects = Sets.newTreeSet(subjects);
    }

    public SortedSet<Semester> getSemesters() {
        return ImmutableSortedSet.copyOf(semesters);
    }

    public void setSemesters(Collection<Semester> semesters) {
        this.semesters = Sets.newTreeSet(semesters);
    }

}
