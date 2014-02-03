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
import org.dlect.ejb.internal.uni.listing.UniversityDataEJBLocal;
import org.dlect.except.DLectException;
import org.dlect.internal.data.UniversityData;
import org.dlect.lock.EquivalenceLock;

/**
 *
 * @author lee
 */
@Stateless
public class UniversityUpdateEJB implements UniversityUpdateEJBLocal {

    private static final EquivalenceLock<String> lock = new EquivalenceLock<>();

    @EJB
    private UniversityDataEJBLocal listings;
    @EJB
    private UniversityDatabaseEJBLocal database;

    @Override
    public UniversityData updateUniversity(String code) throws DLectException {
        try {
            lock.lockSafe(code);
            UniversityData newD = listings.getUniversityForCode(code);
            newD.setCode(code);
            return database.updateUniversityInDatabase(newD);
        } finally {
            lock.release(code);
        }
    }

}
