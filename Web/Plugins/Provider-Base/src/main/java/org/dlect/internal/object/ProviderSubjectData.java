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
package org.dlect.internal.object;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Objects;
import org.dlect.internal.data.merge.PartialSemester;
import org.dlect.internal.data.merge.PartialSubject;

/**
 *
 * @author lee
 */
public class ProviderSubjectData {

    private Multimap<PartialSemester, PartialSubject> mapping;

    public ProviderSubjectData(Multimap<PartialSemester, PartialSubject> mapping) {
        this.mapping = HashMultimap.create(mapping);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProviderSubjectData other = (ProviderSubjectData) obj;
        if (!Objects.equals(this.mapping, other.mapping)) {
            return false;
        }
        return true;
    }

    public Multimap<PartialSemester, PartialSubject> getMapping() {
        return mapping;
    }

    public void setMapping(Multimap<PartialSemester, PartialSubject> mapping) {
        this.mapping = mapping;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.mapping);
        return hash;
    }

    @Override
    public String toString() {
        return "ProviderSubjectData{" + "mapping=" + mapping + '}';
    }

}
