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

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.object.ResultType;

import static org.dlect.helpers.DataHelpers.copy;
import static org.dlect.helpers.DataHelpers.wrap;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "status")
@XmlAccessorType(XmlAccessType.FIELD)
@Deprecated
public class Status implements Serializable {

    @XmlElement(name = "value")
    private ResultType status;
    @XmlElementWrapper(name = "errors", required = false)
    @XmlElement(name = "error")
    private List<String> errors;
    @XmlElementWrapper(name = "warnings", required = false)
    @XmlElement(name = "warning")
    private List<String> warnings;

    public Status() {
    }

    public Status(ResultType status) {
        this.status = status;
        this.errors = null;
        this.warnings = null;
    }

    public Status(ResultType status, List<String> errors, List<String> warnings) {
        this.status = status;
        this.errors = errors == null ? null : Lists.newArrayList(errors);
        this.warnings = warnings == null ? null : Lists.newArrayList(warnings);
    }

    public ResultType getStatus() {
        return status;
    }

    public List<String> getErrors() {
        return wrap(errors);
    }

    public List<String> getWarnings() {
        return wrap(warnings);
    }

    public void setStatus(ResultType status) {
        this.status = status;
    }

    public void setErrors(List<String> errors) {
        this.errors = copy(errors);
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = copy(warnings);
    }

    @Override
    public String toString() {
        return "Status{" + "status=" + status + "\n\t\t errors=" + errors + "\n\t\t warnings=" + warnings + "\n}";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.status);
        hash = 41 * hash + Objects.hashCode(this.errors);
        hash = 41 * hash + Objects.hashCode(this.warnings);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Status other = (Status) obj;
        if (this.status != other.status) {
            return false;
        }
        if (!Objects.equals(this.errors, other.errors)) {
            return false;
        }
        if (!Objects.equals(this.warnings, other.warnings)) {
            return false;
        }
        return true;
    }

}
