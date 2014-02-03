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
package org.dlect.internal.data.merge.config;

import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.SemesterData_;
import org.dlect.internal.data.UniversityData;

/**
 *
 * @author lee
 */
public class SemesterPartialDataConfig extends PartialDataConfiguratorSingleFK<SemesterData, UniversityData> {

    public SemesterPartialDataConfig(UniversityData subD) {
        super(SemesterData_.universityId, subD);
    }

    @Override
    public SemesterData getObject() {
        SemesterData l = new SemesterData();
        l.setUniversityId(getKey());
        return l;
    }

}
