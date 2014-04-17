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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.dlect.except.DLectException;
import org.dlect.internal.data.UniversityData;
import org.dlect.log.Universitys;

/**
 *
 * @author lee
 */
@Singleton
public class UniversitySearchImplEJB implements UniversitySearchImplEJBLocal {

    // TODO

    //@Inject
    //private UniversitySearchProducer producer;

    @Inject
    private AsynchronousUniversitySearchEJBLocal asynchronousSearch;

    private SortedSet<UniversitySearchProvider> providers;

    @Override
    public Set<UniversityData> getUniversitiesMatching(String term) throws DLectException{
        List<Future<Collection<UniversityData>>> futures = Lists.newArrayList();
        for (UniversitySearchProvider prov : getProviders()) {
            futures.add(asynchronousSearch.getUniversitiesMatching(term, prov));
        }
        SortedSet<UniversityData> s = Sets.newTreeSet();
        for (int i = 0; i < futures.size(); i++) {
            try {
                // A university is not overwritten by lower priority providers.
                Collection<UniversityData> get = futures.get(i).get();
                for (UniversityData dat : get) {
                    if (!s.contains(dat)) {
                        
                        s.add(dat);
                    }
                }
                
            } catch (InterruptedException | CancellationException ex) {
                Universitys.LOG.error("Interrupted Exception", ex);
            } catch (ExecutionException ex) {
                Universitys.LOG.error("Execution error for term: " + term, ex);
            }
        }
        return s;
    }

    private SortedSet<UniversitySearchProvider> getProviders() {
        if (providers == null) {
        //    providers = producer.create();
        }
        
        return providers;
    }

}
