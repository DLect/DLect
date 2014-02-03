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

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.dlect.annotate.Unique;
import org.dlect.object.UniversitySupport;

import static org.dlect.helpers.DataHelpers.*;

/**
 *
 * @author lee
 */
@Entity
@Table(name = "university")
public class UniversityData implements Serializable, Comparable<UniversityData> {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "universityId", fetch = FetchType.LAZY)
    private List<SemesterData> semesterList;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "universityId")
    private Long universityId;

    @Unique
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "code")
    private String code;

    @Basic(optional = false)
    @NotNull
    //@Size(max = 20)
    @Column(name = "Support")
    @Enumerated(EnumType.STRING)
    private UniversitySupport support;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "Name")
    private String name;

    @Size(max = 400)
    @Column(name = "URL")
    private String url;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Timeout")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeout;

    @Basic(optional = false)
    @NotNull
    @Column(name = "retry")
    private boolean retry;

    public UniversityData() {
    }

    public UniversityData(String code) {
        this.code = code;
    }

    public UniversityData(Long universityId) {
        this.universityId = universityId;
    }

    public UniversityData(String code, String name, UniversitySupport support) {
        this.code = code;
        this.name = name;
        this.support = support;
    }

    public UniversityData(String code, String name, Date timeout) {
        this.code = code;
        this.name = name;
        this.timeout = timeout;
    }

    public UniversityData(String code, String name, UniversitySupport support, String url) {
        this.code = code;
        this.name = name;
        this.support = support;
        this.url = url;
    }

    public UniversityData(String code, String name, UniversitySupport support, String url, long timeout) {
        this.code = code;
        this.name = name;
        this.support = support;
        this.url = url;
        this.timeout = new Date(timeout);
    }

    public UniversityData(UniversityData d) {
        this.code = d.getCode();
        this.name = d.getName();
        this.support = d.getSupport();
        this.url = d.getUrl();
        this.timeout = wrapDate(d.getTimeout());
        this.retry = d.getRetry();
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTimeout() {
        return wrapDate(timeout, 0L);
    }

    public long getTimeoutLong() {
        return getTimeout().getTime();
    }

    public void setTimeout(Date timeout) {
        this.timeout = wrapDate(timeout, 0L);
    }

    public void setTimeout(long timeout) {
        this.timeout = new Date(timeout);
    }

    public boolean getRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniversityData)) {
            return false;
        }
        UniversityData other = (UniversityData) object;
        return Objects.equal(this.getCode(), other.getCode());
    }

    @Override
    public String toString() {
        return "org.dlect.University[ " + "universityId=" + universityId + "code=" + code + " , name=" + name + " , support=" + support + " , url=" + url + " , timeout=" + timeout + " ]";
    }

    @Override
    public int compareTo(UniversityData o) {
        if (o == null) {
            return 1; // This is greater than null.
        }
        return ComparisonChain.start().compare(this.getCode(), o.getCode(), Ordering.natural().nullsFirst()).result();
    }

    public UniversitySupport getSupport() {
        return support;
    }

    public void setSupport(UniversitySupport support) {
        this.support = support;
    }

    public List<SemesterData> getSemesterList() {
        return semesterList;
    }

    public void setSemesterList(List<SemesterData> semesterList) {
        this.semesterList = semesterList;
    }

}
