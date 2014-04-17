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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.dlect.annotate.Unique;
import org.dlect.helpers.DataHelpers;
import org.dlect.log.Stores;

import static org.dlect.helpers.StoresHelper.normalSorting;

/**
 *
 * @author lee
 */
@Entity
@Table(name = "semester")
public class SemesterData implements Serializable, Comparable<SemesterData> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "semesterId")
    private Long semesterId;

    @Unique
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "semesterCode")
    private String semesterCode;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Basic(optional = false)
    @NotNull
    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Unique
    @JoinColumn(name = "universityId", referencedColumnName = "universityId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UniversityData universityId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semesterId", fetch = FetchType.LAZY)
    private List<SubjectData> subjectList;

    public SemesterData() {
    }

    public SemesterData(Long semesterId) {
        this.semesterId = semesterId;
    }

    public SemesterData(Long semesterId, Date startDate, Date endDate, String name) {
        this.semesterId = semesterId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
    }

    @Override
    public int compareTo(SemesterData o) {
        if (o == null) {
            return 1; // This is greater than null.
        }
        return ComparisonChain.start().compare(this.getSemesterId(), o.getSemesterId(), normalSorting())
                .compare(this.getUniversityId(), o.getUniversityId(), normalSorting())
                .result();
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Date getStartDate() {
        return DataHelpers.wrapDate(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = DataHelpers.wrapDate(startDate);
    }

    public Date getEndDate() {
        return DataHelpers.wrapDate(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = DataHelpers.wrapDate(endDate);
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

    public List<SubjectData> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectData> subjectList) {
        this.subjectList = subjectList;
    }

    public boolean addSubjectData(SubjectData... toAdd) {
        return this.subjectList.addAll(Arrays.asList(toAdd));
    }

    public boolean removeSubjectData(SubjectData... toAdd) {
        return this.subjectList.removeAll(Arrays.asList(toAdd));
    }

    public UniversityData getUniversityId() {
        return universityId;
    }

    public void setUniversityId(UniversityData universityId) {
        this.universityId = universityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (semesterId != null ? semesterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SemesterData)) {
            return false;
        }
        SemesterData other = (SemesterData) object;
        return (this.getSemesterId() != null || other.getSemesterId() == null)
               && (this.getSemesterId() == null || this.getSemesterId().equals(other.getSemesterId()));
    }

    @Override
    public String toString() {
        Stores.LOG.error("SemesterData{" + "semesterId=" + semesterId + ", semesterCode=" + semesterCode + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + ", universityId=" + universityId + '}');

        return "SemesterData{" + "semesterId=" + semesterId + ", semesterCode=" + semesterCode + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + ", universityId=" + universityId + '}';
    }

}
