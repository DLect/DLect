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
package org.dlect.internal.data.merge;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.persistence.metamodel.SingularAttribute;
import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.SemesterData_;


/**
 *
 * @author lee
 */
public class PartialSemester implements PartialData<SemesterData> {

    private String semesterCode;

    private String name;

    private Date startDate;

    private Date endDate;
    
    @Override
    public void assignTo(SemesterData data) {
        data.setEndDate(endDate);
        data.setStartDate(startDate);
        data.setName(name);
        data.setSemesterCode(semesterCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartialSemester other = (PartialSemester) obj;
        if (!Objects.equals(this.semesterCode, other.semesterCode)) {
            return false;
        }
        return true;
    }

    @Override
    public Map<SingularAttribute<SemesterData, ?>, Object> fillUniqueData(Map<SingularAttribute<SemesterData, ?>, Object> obj) {
        obj.put(SemesterData_.semesterCode, semesterCode);
        return obj;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(String semesterCode) {
        this.semesterCode = semesterCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.semesterCode);
        return hash;
    }

}
