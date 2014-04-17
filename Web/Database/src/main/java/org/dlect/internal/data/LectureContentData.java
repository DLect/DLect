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
package org.dlect.internal.data;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.dlect.annotate.Unique;

/**
 *
 * @author lee
 */
@Entity
@Table(name = "lectureContent")
public class LectureContentData implements Serializable {

    /**
     * TODO: FIX WARNING
     *
     * WARNING: THIS WILL NOT WORK WITH MULTIPLE PEOPLE USING THE SERVICE.
     * The service uses a database which, when another person adds their data, will update all the objects; even those
     * of people without access. Either a list of subjects must be stored; or the objects detached from the database.
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dataId")
    private Long dataId;

    @Unique
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "dataType")
    private String dataType;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "url")
    private String url;

    @Unique
    @JoinColumn(name = "lectureId", referencedColumnName = "lectureId")
    @ManyToOne(optional = false)
    private LectureData lectureId;

    public LectureContentData() {
    }

    public LectureContentData(Long dataId) {
        this.dataId = dataId;
    }

    public LectureContentData(Long dataId, String dataType, String url) {
        this.dataId = dataId;
        this.dataType = dataType;
        this.url = url;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
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

    public LectureData getLectureId() {
        return lectureId;
    }

    public void setLectureId(LectureData lectureId) {
        this.lectureId = lectureId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataId != null ? dataId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LectureContentData)) {
            return false;
        }
        LectureContentData other = (LectureContentData) object;

        return Objects.equals(this.dataId, other.dataId);
    }

    @Override
    public String toString() {
        return "org.dlect.internal.data.LectureData[ dataId=" + dataId + " ]";
    }

}
