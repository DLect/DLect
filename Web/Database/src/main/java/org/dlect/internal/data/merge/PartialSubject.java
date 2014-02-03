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

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.persistence.metamodel.SingularAttribute;
import org.dlect.internal.data.LectureData;
import org.dlect.internal.data.StreamData;
import org.dlect.internal.data.SubjectData;
import org.dlect.internal.data.SubjectData_;

/**
 *
 * @author lee
 */
public class PartialSubject implements PartialData<SubjectData> {

    private String name;

    private String url;

    private String description;

    private Set<LectureData> lectureList;

    private Set<StreamData> streamList;

    @Override
    public void assignTo(SubjectData data) {
        data.setDescription(description);
        data.setName(name);
        data.setUrl(url);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartialSubject other = (PartialSubject) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public Map<SingularAttribute<SubjectData, ?>, Object> fillUniqueData(Map<SingularAttribute<SubjectData, ?>, Object> obj) {
        obj.put(SubjectData_.name, name);
        return obj;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<LectureData> getLectureList() {
        return lectureList;
    }

    public void setLectureList(Set<LectureData> lectureList) {
        this.lectureList = lectureList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<StreamData> getStreamList() {
        return streamList;
    }

    public void setStreamList(Set<StreamData> streamList) {
        this.streamList = streamList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

}
