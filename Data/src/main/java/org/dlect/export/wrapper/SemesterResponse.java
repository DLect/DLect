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

import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.export.Semester;

import static org.dlect.helpers.DataHelpers.*;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "semesters")
@XmlAccessorType(XmlAccessType.FIELD)
public class SemesterResponse {

    @XmlElement(name = "semester")
    private List<Semester> semesters;

    public SemesterResponse(Collection<Semester> semesters) {
        this.semesters = copyReplaceNull(semesters);
    }

    public SemesterResponse() {
    }

    public List<Semester> getSemesters() {
        return wrap(semesters);
    }

    public void setSemesters(List<Semester> semesters) {
        this.semesters = copyReplaceNull(semesters);
    }

}
