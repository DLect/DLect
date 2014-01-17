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

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import static org.dlect.helpers.DataHelpers.copy;
import static org.dlect.helpers.DataHelpers.wrap;
import static org.dlect.helpers.DataHelpers.wrapDate;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "semester")
@XmlAccessorType(XmlAccessType.FIELD)
public class Semester implements Comparable<Semester> {

    @XmlID
    @XmlElement(name = "id", required = true)
    private String id;

    @XmlElement(name = "code", required = true)
    private String code;

    @XmlElement(name = "name", required = true)
    private String name;

    @XmlElement(name = "startDate", required = false)
    private Date startDate;

    @XmlElement(name = "endDate", required = false)
    private Date endDate;

    @XmlElementWrapper(name = "subjects")
    @XmlElement(name = "subject")
    private List<Subject> subjectList;

    public Semester() {
    }

    @Override
    public int compareTo(Semester o) {
        return Integer.compare(Integer.parseInt(code), Integer.parseInt(o.getCode()));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getEndDate() {
        return wrapDate(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = wrapDate(endDate);
    }

    public Date getStartDate() {
        return wrapDate(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = wrapDate(startDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = Long.toString(id, 10);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subject> getSubjectList() {
        return wrap(subjectList);
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = copy(subjectList);
    }

}
