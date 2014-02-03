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
package org.dlect.provider.common.blackboard.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import org.dlect.internal.data.UniversityData;
import org.dlect.object.UniversitySupport;

import static org.dlect.provider.common.blackboard.helpers.BlackboardMobileHelper.toSupport;
import static org.dlect.provider.common.blackboard.helpers.BlackboardMobileHelper.toUniversityCode;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BlackboardUniversitySearchResult implements BlackboardUniversityInformation {

    @XmlElement(name = "name", nillable = false, required = true)
    private String name;

    @XmlElement(name = "client_id", nillable = false, required = true)
    private long clientId;

    @XmlElement(name = "http_auth", required = false)
    private int httpAuth;
    @XmlElement(name = "can_has_ssl_login", required = false)
    private boolean canHasSslLogin;
    @XmlElement(name = "b2_url", required = false)
    private String url;

    @Override
    public UniversityData getUniversity() {
        UniversityData data = new UniversityData(toUniversityCode(clientId), name, UniversitySupport.NONE, null);
        if (url != null) {
            data.setUrl(url);
            data.setSupport(toSupport(canHasSslLogin, httpAuth != 0));
        }
        return data;
    }

}
