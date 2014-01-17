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
package org.dlect.export;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.log.Stores;
import org.dlect.object.UniversitySupport;

import static org.dlect.helpers.DataHelpers.copyReplaceNull;
import static org.dlect.helpers.DataHelpers.wrap;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "university")
@XmlAccessorType(XmlAccessType.FIELD)
public class University implements Serializable, Comparable<University> {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "code")
    private String code;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "support")
    private UniversitySupport support;

    @XmlElement(name = "url")
    private String url;

    @XmlElement(name = "timeout")
    private Date timeout;

    @XmlElementWrapper(name="semesters")
    @XmlElement(name="semester")
    private List<Semester> semesters;
    
    public University() {
    }

    public University(String code) {
        this.code = code;
        this.support = UniversitySupport.NONE;
    }

    public University(String code, String name, UniversitySupport support, String url, Date timeout) {
        this.code = code;
        this.name = name;
        this.support = support;
        this.url = url;
        this.timeout = timeout;
    }

    public University(String code, String name, UniversitySupport support) {
        this.code = code;
        this.name = name;
        this.support = support;
    }

    @Deprecated
    public University(String code, String name, UniversitySupport support, Status status) {
        this.code = code;
        this.name = name;
        this.support = support;
    }

    @Deprecated
    public University(String code, String name, UniversitySupport support, String url, Date timeout, Status status) {
        this.code = code;
        this.name = name;
        this.support = support;
        this.url = url;
        this.timeout = timeout;
    }

    @Deprecated
    public University(University uni) {
        this.code = uni.getCode();
        this.name = uni.getName();
        this.support = uni.getSupport();
        this.url = uni.getUrl();
        this.timeout = uni.getTimeout();
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

    public List<Semester> getSemesters() {
        return wrap(semesters);
    }

    public void setSemesters(List<Semester> semesters) {
        this.semesters = copyReplaceNull(semesters);
    }

    public UniversitySupport getSupport() {
        return support;
    }

    public void setSupport(UniversitySupport support) {
        this.support = support;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTimeout() {
        return new Date(timeout.getTime());
    }

    public void setTimeout(Date timeout) {
        this.timeout = new Date(timeout.getTime());
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
        if (object == null || !(object instanceof University)) {
            return false;
        }
        University other = (University) object;
        /**
         * Attempting to compare two universities that have not been fully
         * initilised.
         */
        if (this.getCode() == null || other.getCode() == null) {
            Stores.LOG.warn("Attempting to compare two universities that have not been fully initilised.");
        }

        return Objects.equal(this.getCode(), other.getCode());
    }

    @Override
    public String toString() {
        return "org.dlect.University[ " + "code=" + code + " , name=" + name + " , support=" + support + " , url=" + url + " , timeout=" + timeout + " ]";
    }

    @Override
    public int compareTo(University o) {
        return ComparisonChain.start().compare(this.getCode(), o.getCode(), Ordering.natural().nullsFirst()).result();
    }

    public University copyOf() {
        return new University(this);
    }

}
