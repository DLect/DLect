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
package org.dlect.ejb.internal.uni.search.impl;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import org.dlect.ejb.internal.AbstractPrioritisable;
import org.dlect.ejb.internal.uni.search.UniversitySearchProvider;
import org.dlect.db.DataToDatabaseEJB;
import org.dlect.internal.data.UniversityData;
import org.dlect.internal.data.UniversityData_;
import org.dlect.log.Universitys;

import static org.dlect.helpers.CommonDatabaseWhereOp.like;

public class DatabaseUniversitySearchProvider extends AbstractPrioritisable<UniversitySearchProvider> implements UniversitySearchProvider {

    // Always
    private static final int PRIORITY = Integer.MIN_VALUE;

    @EJB
    private DataToDatabaseEJB manager;

    public DatabaseUniversitySearchProvider() {
        super(PRIORITY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<UniversityData> getUniversitiesMatching(String searchTerm) {
        Collection<UniversityData> data = manager.getAllWhere(UniversityData_.name, like(), "%" + searchTerm + "%");

        List<UniversityData> copies = Lists.newArrayListWithExpectedSize(data.size());

        for (UniversityData d : data) {
            copies.add(new UniversityData(d));
        }
        return copies;

    }

}
