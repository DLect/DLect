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
package org.dlect.ejb.internal.uni.search;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import org.dlect.except.DLectException;
import org.dlect.internal.data.UniversityData;
import org.dlect.log.Universitys;

/**
 *
 * @author lee
 */
@Stateless
public class AsynchronousUniversitySearchEJB implements AsynchronousUniversitySearchEJBLocal {

    @Asynchronous
    @Override
    public Future<Collection<UniversityData>> getUniversitiesMatching(String term, UniversitySearchProvider prov) {
        Collection<UniversityData> ret = ImmutableList.of();
        try {
            Collection<UniversityData> res = prov.getUniversitiesMatching(term);
            if (res == null) {
                Universitys.LOG.error("The provider {} returned null data when it said it supported it. Term: {}", prov.getClass(), term);
            } else {
                ret = res;
            }
        } catch (DLectException ex) {
            Universitys.LOG.error("prov " + prov.getClass() + " caused an error on " + term, ex);
        }

        return new AsyncResult<>(ret);
    }

}
