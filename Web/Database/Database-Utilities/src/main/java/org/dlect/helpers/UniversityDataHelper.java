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
package org.dlect.helpers;

import javax.inject.Singleton;
import org.dlect.export.Status;
import org.dlect.export.University;
import org.dlect.internal.data.UniversityData;
import org.dlect.object.UniversitySupport;

/**
 * This class is intended for injection into classes that need it.
 *
 * @author lee
 */
@Singleton
public class UniversityDataHelper {
    
    public UniversityDataHelper() {
        
    }

    /**
     * This method will not create a new instance of {@link UniversityData}, so
     * is safe to use with database objects. This method will throw an
     * {@link IllegalArgumentException} if {@code unClean} is {@code null} or if
     * {@code unClean.getCode()} is {@code null}
     *
     * @param unClean The unclean, possibly null object to attempt to clean.
     * @return {@code unClean} with it's name and support set to default values
     * if they are {@code null}, and the retry flag reset to {@code false}
     */
    public UniversityData clean(UniversityData unClean) {
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

    /**
     * This method converts between the database and export University Object.
     * This method will only call {@linkplain University#setTimeout(java.util.Date)
     * } iff the timeout date in the given object is valid(greater than 0).
     *
     * @param data The database object to convert.
     * @return A new {@linkplain University} object with all the information
     * contained in {@code data} and it's status set to {@code null}
     */
    @Deprecated
    public University toExport(UniversityData data) {
        return toExport(data, null);
    }

    /**
     *
     * @param data
     * @param status The status to set to the new {@link University} object.
     * This can be null.
     * @return A new {@linkplain University} object with all the information
     * contained in {@code data} and having it's status set to {@code status}
     */
    @Deprecated
    public University toExport(UniversityData data, Status status) {
        if (data == null) {
            return null;
        }
        
        University u = new University();
        u.setCode(data.getCode());
        u.setName(data.getName());
        u.setUrl(data.getUrl());
        u.setSupport(data.getSupport());
        if (data.getTimeoutLong() > 0) {
            u.setTimeout(DataHelpers.wrapDate(data.getTimeout()));
        }
        
        
        return u;
    }

    /**
     * Sets all the data in {@code currData} to be that of {@code newData} with
     * the exception of the {@code code}.
     *
     * @param currData The data to update with the information in
     * {@code newData}.
     * @param newData The new data with which to update the {@code currData}.
     * @return This method returns {@code currData} allowing for chaining.
     */
    @Deprecated
    public UniversityData setTo(UniversityData currData, UniversityData newData) {
        // Don't change the primary key even if it is different.
        currData.setName(newData.getName());
        currData.setRetry(newData.getRetry());
        currData.setSupport(newData.getSupport());
        currData.setTimeout(newData.getTimeout());
        currData.setUrl(newData.getUrl());
        return currData;
    }
    
}
