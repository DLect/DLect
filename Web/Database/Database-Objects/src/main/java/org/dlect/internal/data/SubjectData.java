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

import com.google.common.collect.ComparisonChain;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.dlect.annotate.Unique;
import org.dlect.helpers.DataHelpers;

import static org.dlect.helpers.StoresHelper.normalSorting;

/**
 *
 * @author lee
 */
@Entity
@Table(name = "subject")
public class SubjectData implements Serializable, Comparable<SubjectData> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "subjectId")
    private Long subjectId;

    @Unique
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "url")
    private String url;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "description")
    private String description;

    @Column(name = "dataLastUpdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataLastUpdated;

    @Unique
    @JoinColumn(name = "semesterId", referencedColumnName = "semesterId")
    @OneToOne(optional = false)
    private SemesterData semesterId;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "subjectId")
    private Set<LectureData> lectureList;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "subjectId")
    private Set<StreamData> streamList;

    public SubjectData() {
    }

    public SubjectData(Long subjectId) {
        this.subjectId = subjectId;
    }

    public SubjectData(Long subjectId, String name, String description) {
        this.subjectId = subjectId;
        this.name = name;
        this.description = description;
    }

    @Override
    public int compareTo(SubjectData o) {
        if (o == null) {
            return 1; // This is greater than null.
        }
        return ComparisonChain.start().compare(this.getName(), o.getName(), normalSorting())
                .compare(this.getSemesterId(), o.getSemesterId(), normalSorting())
                .result();
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDataLastUpdated() {
        return DataHelpers.wrapDate(dataLastUpdated);
    }

    public void setDataLastUpdated(Date dataLastUpdated) {
        this.dataLastUpdated = DataHelpers.wrapDate(dataLastUpdated);
    }

    public SemesterData getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(SemesterData semesterId) {
        this.semesterId = semesterId;
    }

    public Set<LectureData> getLectureList() {
        return lectureList;
    }

    public void setLectureList(Set<LectureData> lectureList) {
        this.lectureList = lectureList;
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
        int hash = 0;
        hash += (subjectId != null ? subjectId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubjectData)) {
            return false;
        }
        SubjectData other = (SubjectData) object;
        if ((this.subjectId == null && other.subjectId != null) || (this.subjectId != null && !this.subjectId.equals(other.subjectId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SubjectData{" + "subjectId=" + subjectId + ", name=" + name + ", description=" + description + ", dataLastUpdated=" + dataLastUpdated + ", semesterId=" + semesterId + ", lectureList=" + lectureList + ", streamDataList=" + streamList + '}';
    }

}
