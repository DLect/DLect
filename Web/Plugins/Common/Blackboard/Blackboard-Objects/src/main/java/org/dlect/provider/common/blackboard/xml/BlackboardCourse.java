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
package org.dlect.provider.common.blackboard.xml;

import org.dlect.provider.common.blackboard.xml.adapters.BlackboardXmlDateTypeAdapter;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dlect.helpers.DataHelpers;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BlackboardCourse {

    @XmlAttribute(name = "bbid")
    private String bbid;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "courseid")
    private String courseId;
    @XmlAttribute(name = "enrollmentdate")
    @XmlJavaTypeAdapter(BlackboardXmlDateTypeAdapter.class)
    private Date enrollmentDate;

    public String getBbid() {
        return bbid;
    }

    public void setBbid(String bbid) {
        this.bbid = bbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Date getEnrollmentDate() {
        return DataHelpers.wrapDate(enrollmentDate);
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = DataHelpers.wrapDate(enrollmentDate);
    }

}
