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
package org.dlect.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.dlect.db.DataToDatabaseEJBLocal;
import org.dlect.internal.data.UniversityData;
import org.dlect.internal.data.UniversityData_;
import org.dlect.object.UniversitySupport;

/**
 *
 * @author lee
 */
@Stateless
public class UniversityDatabaseEJB implements UniversityDatabaseEJBLocal {

    @EJB
    private DataToDatabaseEJBLocal dbHelper;

    @Override
    public UniversityData updateUniversityInDatabase(UniversityData newD) {
        UniversityData cleanedData = clean(newD);

        cleanedData.setTimeout(cleanedData.getSupport().offsetFromNow());

        UniversityData data = dbHelper.getEqualTo(UniversityData_.code, cleanedData.getCode());
        if (data == null) {
            dbHelper.persist(cleanedData);
            data = cleanedData;
        } else {

            if (cleanedData.getSupport() == UniversitySupport.NONE
                && data.getSupport() != UniversitySupport.NONE
                && !data.getRetry()) {
                data.setRetry(true);
            } else {
                cleanedData.setRetry(false);

                cleanedData.setUniversityId(data.getUniversityId());

                dbHelper.merge(cleanedData);
            }
        }
        return data;
    }

    protected UniversityData clean(UniversityData unClean) {
        if (unClean == null || unClean.getCode() == null) {
            throw new IllegalArgumentException("The University data is invalid.");
        }
        if (unClean.getName() == null) {
            unClean.setName("?");
        }
        if (unClean.getSupport() == null) {
            unClean.setSupport(UniversitySupport.NONE);
        }
        unClean.setRetry(false);
        return unClean;
    }

}
