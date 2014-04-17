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
package org.dlect.ejb.internal.uni.listing.impl.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.internal.data.UniversityData;
import org.dlect.object.UniversitySupport;

import static org.dlect.helpers.internal.impl.BlackboardMobileHelper.toUniversityCode;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "s")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlackboardUniversityWithInformation implements BlackboardUniversityInformation {

    @XmlElement(name = "client_id", required = true)
    private long clientId;
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "b2_url", required = false, nillable = true)
    private String b2Url;
    @XmlElement(name = "can_has_ssl_login", required = false, nillable = true)
    private Boolean canHasSslLogin;
    @XmlElement(name = "http_auth", required = false, nillable = true)
    private Integer httpAuth;

    @Override
    public UniversityData getUniversity() {
        boolean http = httpAuth == null ? false : httpAuth.intValue() != 0;
        boolean ssl = canHasSslLogin == null ? false : canHasSslLogin.booleanValue();

        UniversitySupport us = (b2Url == null ? UniversitySupport.NONE : UniversitySupport.toSupport(ssl, http));

        return new UniversityData(toUniversityCode(clientId), name, us, b2Url);
    }
}
