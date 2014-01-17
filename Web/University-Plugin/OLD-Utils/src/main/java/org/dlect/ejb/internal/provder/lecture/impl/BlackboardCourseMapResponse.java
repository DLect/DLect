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
package org.dlect.ejb.internal.provder.lecture.impl;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import static org.dlect.helpers.DataHelpers.copyReplaceNull;
import static org.dlect.helpers.DataHelpers.wrap;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mobileresponse")
public class BlackboardCourseMapResponse {

    @XmlAttribute(name = "rooturl")
    private String rootUrl;

    @XmlElementWrapper(name = "map")
    @XmlElement(name = "map-item")
    private List<BlackboardCourseMapItem> map;

    public List<BlackboardCourseMapItem> getMap() {
        return wrap(map);
    }

    public void setMap(List<BlackboardCourseMapItem> map) {
        this.map = copyReplaceNull(map);
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

}
