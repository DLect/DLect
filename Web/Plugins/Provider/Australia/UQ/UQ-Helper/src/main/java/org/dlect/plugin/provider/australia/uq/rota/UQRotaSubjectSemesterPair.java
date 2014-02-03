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
package org.dlect.plugin.provider.australia.uq.rota;

import java.util.Objects;

/**
 *
 * @author lee
 */
public class UQRotaSubjectSemesterPair {
    private final String subjectCode;
    private final int semesterCode;

    public UQRotaSubjectSemesterPair(String subjectCode, int semesterCode) {
        this.subjectCode = subjectCode;
        this.semesterCode = semesterCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UQRotaSubjectSemesterPair other = (UQRotaSubjectSemesterPair) obj;
        return Objects.equals(this.subjectCode, other.subjectCode) && this.semesterCode == other.semesterCode;
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.subjectCode);
        hash = 97 * hash + this.semesterCode;
        return hash;
    }
    
}
