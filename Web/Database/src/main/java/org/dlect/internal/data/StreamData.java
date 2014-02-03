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
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "stream")
public class StreamData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "streamId")
    private Long streamId;

    @Unique
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "name")
    private String name;

    @Unique
    @JoinColumn(name = "subjectId", referencedColumnName = "subjectId")
    @ManyToOne(optional = false)
    private SubjectData subjectId;

    @JoinTable(name = "streamLecture")
    @ManyToMany(mappedBy = "streamData")
    private Collection<LectureData> lectureData;

    public StreamData() {
    }

    public StreamData(Long streamId) {
        this.streamId = streamId;
    }

    public StreamData(Long streamId, String name) {
        this.streamId = streamId;
        this.name = name;
    }

    public Collection<LectureData> getLectureData() {
        return lectureData;
    }

    public void setLectureData(Collection<LectureData> lectureData) {
        this.lectureData = lectureData;
    }

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubjectData getSubject() {
        return subjectId;
    }

    public void setSubject(SubjectData subjectId) {
        this.subjectId = subjectId;
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
        hash += (streamId != null ? streamId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StreamData)) {
            return false;
        }
        StreamData other = (StreamData) object;
        return Objects.equals(this.streamId, other.streamId);
    }

    @Override
    public String toString() {
        return "org.dlect.internal.data.Stream[ streamId=" + streamId + " ]";
    }

}
