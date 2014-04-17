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
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.export.Status;
import org.dlect.export.University;
import org.dlect.object.ResultType;
import org.dlect.object.StatusBuilder;

/**
 *
 * @author Lee
 */
@XmlRootElement(name = "universities")
@XmlAccessorType(XmlAccessType.FIELD)
public class UniversityList  {

    @XmlElement(name = "university")
    private SortedSet<University> universities;

    @XmlElement(name = "status")
    private Status status;

    public UniversityList() {
        this.universities = Sets.newTreeSet();
        this.status = StatusBuilder.builder(ResultType.OK).build();
    }

    public UniversityList(Collection<University> unis) {
        this.universities = Sets.newTreeSet(unis);
        this.status = StatusBuilder.builder(ResultType.OK).build();
    }

    public UniversityList(Collection<University> unis, Status s) {
        this.universities = Sets.newTreeSet(unis);
        this.status = s;
    }

    public UniversityList(Status s) {
        this.universities = Sets.newTreeSet();
        this.status = s;
    }

    public SortedSet<University> getUniversities() {
        return ImmutableSortedSet.copyOf(universities);
    }

    public void setUniversities(Collection<University> unis) {
        this.universities = Sets.newTreeSet(unis);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
