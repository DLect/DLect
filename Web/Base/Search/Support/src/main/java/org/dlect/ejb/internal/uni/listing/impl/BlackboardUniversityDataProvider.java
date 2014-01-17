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
package org.dlect.ejb.internal.uni.listing.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import org.apache.http.client.utils.URIBuilder;
import org.dlect.ejb.internal.AbstractPrioritisable;
import org.dlect.ejb.internal.uni.listing.UniversityDataProvider;
import org.dlect.ejb.internal.uni.listing.impl.obj.BlackboardUniversityInformation;
import org.dlect.ejb.internal.uni.listing.impl.obj.BlackboardUniversityWithInformation;
import org.dlect.ejb.internal.uni.listing.impl.obj.BlackboardUniversityWithoutInformation;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.except.UnknownInternalDLectException;
import org.dlect.helpers.JAXBHelper;
import org.dlect.helpers.JAXBHelper.JaxbBindingSet;
import org.dlect.helpers.internal.impl.BlackboardMobileHelper;
import org.dlect.internal.data.UniversityData;
import org.dlect.log.Universitys;
import org.dlect.object.ResultType;

@Singleton
public class BlackboardUniversityDataProvider extends AbstractPrioritisable<UniversityDataProvider> implements UniversityDataProvider {

    private static final int PRIORITY = 0;
    private static final JaxbBindingSet<BlackboardUniversityInformation> REFRESH_PROVIDER_BINDINGS
            = JAXBHelper.bindingInterface(
                    BlackboardUniversityWithInformation.class,
                    BlackboardUniversityWithoutInformation.class);

    public BlackboardUniversityDataProvider() {
        super(PRIORITY);
    }

    @Override
    public UniversityData getUniversityForCode(String code) throws DLectException {
        
        try {
           URI updateURL = getUpdateURL(code);
            BlackboardUniversityInformation ui = JAXBHelper.unmarshalFromUri(updateURL, REFRESH_PROVIDER_BINDINGS);

            UniversityData uni = ui.getUniversity();
            if (uni == null) {
                throw CommonExceptionBuilder.getIllegalReturnTypeException("BB University Information Provider returned a null University class.", code, uni);
            }

            return uni;
        } catch (IOException | JAXBException ex) {
            throw DLectExceptionBuilder.builder().setResult(ResultType.UNIVERSITY_ERROR).setMessage("Failed to access University").setCause(ex).build();
        }

    }

    @Override
    public boolean supports(String code) {
        return BlackboardMobileHelper.isCodeSupported(code);
    }

    /**
     *
     * @param code
     *
     * @return A new university data object; or null if an error occured
     */
    private URI getUpdateURL(String code) throws IOException {
        try {
            URIBuilder builder = new URIBuilder("https://mlcs.medu.com/api/b2_registration/"
                                                + "refresh_info?q=&carrier_code=&carrier_name=&device_name=&"
                                                + "platform=&timestamp=&registration_id=&f=xml&device_id=&"
                                                + "android=1&v=1&language=en_GB&ver=3.1.2");
            builder.addParameter("client_id", BlackboardMobileHelper.toClientId(code));
            return builder.build();
        } catch (URISyntaxException ex) {
            Universitys.LOG.error("URL Exception that should never happen.", ex);
            throw new IOException("URL Exception caught", ex);
        }
    }

}
