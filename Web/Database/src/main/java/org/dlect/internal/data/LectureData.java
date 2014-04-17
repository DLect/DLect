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
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.dlect.annotate.RecurseForMerge;
import org.dlect.annotate.Unique;

import static org.dlect.helpers.StoresHelper.normalSorting;

/**
 *
 * @author lee
 */
@Entity
@Table(name = "lecture", uniqueConstraints = @UniqueConstraint(columnNames = {"recordedDate", "location", "subjectId"}))
public class LectureData implements Serializable, Comparable<LectureData> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lectureId")
    private Long lectureId;

    @Unique
    @Basic(optional = false)
    @NotNull
    @Column(name = "recordedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordedDate;

    @Unique
    @Basic(optional = false)
    @NotNull
    @Column(name = "location")
    private String location;

    @RecurseForMerge
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lectureId")
    private Collection<LectureContentData> lectureDataCollection;
    
    @Unique
    @JoinColumn(name = "subjectId", referencedColumnName = "subjectId")
    @ManyToOne(optional = false)
    private SubjectData subjectId;

    @JoinTable(name = "streamLecture")
    @ManyToMany
    private Collection<StreamData> streamData;

    public LectureData() {
    }

    public LectureData(Long lectureId) {
        this.lectureId = lectureId;
    }

    public LectureData(Long lectureId, Date recordedDate) {
        this.lectureId = lectureId;
        this.recordedDate = recordedDate;
    }

    @Override
    public int compareTo(LectureData o) {
        if (o == null) {
            return 1; // This is greater than null.
        }
        return ComparisonChain.start().compare(this.getLocation(), o.getLocation(), normalSorting())
                .compare(this.getRecordedDate(), o.getRecordedDate(), normalSorting())
                .compare(this.getSubjectId(), o.getSubjectId(), normalSorting()).result();
    }

    public Long getLectureId() {
        return lectureId;
    }

    public void setLectureId(Long lectureId) {
        this.lectureId = lectureId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }
    
    public Collection<LectureContentData> getLectureContentData() {
        return lectureDataCollection;
    }
    
    public void setLectureContentData(Collection<LectureContentData> lectureDataCollection) {
        this.lectureDataCollection = lectureDataCollection;
    }

    public Collection<StreamData> getStreamData() {
        return streamData;
    }

    public void setStreamData(Collection<StreamData> streamData) {
        this.streamData = streamData;
    }

    public SubjectData getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(SubjectData subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lectureId != null ? lectureId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LectureData)) {
            return false;
        }
        LectureData other = (LectureData) object;

        return Objects.equals(this.lectureId, other.lectureId);
    }

    @Override
    public String toString() {
        return "org.dlect.internal.data.Lecture[ lectureId=" + lectureId + " ]";
    }

}
