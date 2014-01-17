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
import javax.persistence.metamodel.SingularAttribute;
import org.dlect.internal.data.LectureContentData;
import org.dlect.internal.data.LectureContentData_;

/**
 *
 * @author lee
 */
public class PartialLectureContent implements PartialData<LectureContentData> {

    private String dataType;
    
    private String url;
    
    @Override
    public void assignTo(LectureContentData data) {
        data.setDataType(dataType);
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
        final PartialLectureContent other = (PartialLectureContent) obj;
        if (!Objects.equals(this.dataType, other.dataType)) {
            return false;
        }
        return true;
    }

    @Override
    public Map<SingularAttribute<LectureContentData, ?>, Object> fillUniqueData(Map<SingularAttribute<LectureContentData, ?>, Object> obj) {
        obj.put(LectureContentData_.dataType, dataType);
        return obj;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.dataType);
        return hash;
    }
    
    
    
}
