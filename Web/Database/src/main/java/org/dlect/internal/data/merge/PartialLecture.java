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

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.persistence.metamodel.SingularAttribute;
import org.dlect.internal.data.LectureData;
import org.dlect.internal.data.LectureData_;

import static org.dlect.helpers.DataHelpers.copy;
import static org.dlect.helpers.DataHelpers.wrap;

/**
 *
 * @author lee
 */
public class PartialLecture implements PartialData<LectureData> {

    private Date recordedDate;

    private String location;

    private Set<PartialLectureContent> lectureContent;

    @Override
    public void assignTo(LectureData data) {
        data.setLocation(location);
        data.setRecordedDate(recordedDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartialLecture other = (PartialLecture) obj;
        return Objects.equals(this.recordedDate, other.recordedDate) && Objects.equals(this.location, other.location);
    }

    @Override
    public Map<SingularAttribute<LectureData, ?>, Object> fillUniqueData(Map<SingularAttribute<LectureData, ?>, Object> obj) {
        obj.put(LectureData_.recordedDate, recordedDate);
        obj.put(LectureData_.location, location);
        return obj;
    }

    public Collection<PartialLectureContent> getLectureContent() {
        return wrap(lectureContent);
    }

    public void setLectureContent(Set<PartialLectureContent> lectureContent) {
        this.lectureContent = lectureContent;
    }

    public void setLectureContent(Collection<PartialLectureContent> lectureDataCollection) {
        this.lectureContent = Sets.newHashSet(lectureDataCollection);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getRecordedDate() {
        return copy(recordedDate);
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = copy(recordedDate);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.recordedDate);
        hash = 29 * hash + Objects.hashCode(this.location);
        return hash;
    }

}
