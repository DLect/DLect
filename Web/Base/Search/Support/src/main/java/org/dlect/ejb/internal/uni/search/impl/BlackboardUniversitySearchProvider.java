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
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.http.client.utils.URIBuilder;
import org.dlect.ejb.internal.AbstractPrioritisable;
import org.dlect.ejb.internal.uni.search.UniversitySearchProvider;
import org.dlect.ejb.internal.uni.search.impl.obj.BlackboardUniversitySearchResult;
import org.dlect.ejb.internal.uni.search.impl.obj.BlackboardUniversitySearchResults;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.helpers.JAXBHelper;
import org.dlect.helpers.JAXBHelper.JaxbBindingSet;
import org.dlect.internal.data.UniversityData;
import org.dlect.log.Universitys;
import org.dlect.object.ResultType;

public class BlackboardUniversitySearchProvider extends AbstractPrioritisable<UniversitySearchProvider> implements UniversitySearchProvider {

    private static final int PRIORITY = 0;
    private static final JaxbBindingSet<BlackboardUniversitySearchResults> SEARCH_PROVIDER_BINDINGS
            = JAXBHelper.binding(
                    BlackboardUniversitySearchResults.class
            );

    public BlackboardUniversitySearchProvider() {
        super(PRIORITY);
    }

    @Override
    public Collection<UniversityData> getUniversitiesMatching(String searchTerm) throws DLectException {
        try {
            URIBuilder builder = new URIBuilder(
                    "https://mlcs.medu.com/api/b2_registration/match_schools?carrier_name=&carrier_code=&platform=&device_name=&timestamp=&registration_id=&f=xml&device_id=&android=1&v=&language=en_GB&ver=3.1.2");
            builder.addParameter("q", searchTerm);

            BlackboardUniversitySearchResults result = JAXBHelper.unmarshalFromUri(builder.build(), SEARCH_PROVIDER_BINDINGS);

            if (result == null || result.getUniversitySearchResults() == null) {
                return Lists.newArrayList();
            }

            List<UniversityData> data = Lists.newArrayListWithCapacity(result.getUniversitySearchResults().size());
            for (BlackboardUniversitySearchResult res : result.getUniversitySearchResults()) {
                UniversityData db = res.getUniversity();
                data.add(db);
            }

            return data;
        } catch (URISyntaxException | JAXBException | IOException ex) {
            Universitys.LOG.error("Blackboard University Error", ex);
            throw DLectExceptionBuilder.builder().setResult(ResultType.UNIVERSITY_ERROR).setErrorMessages("Failed to access Blackboard").setCause(ex).build();
        }
    }

}
