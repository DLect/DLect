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
package org.dlect.ejb.internal.provder.impl.blackboard.au.uq.rota.objects;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.helpers.DataHelpers;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "course")
@XmlAccessorType(XmlAccessType.FIELD)
public class UQRotaSubject {

    @XmlElement(name = "name")
    private String name;
    
    @XmlElement(name = "code")
    private String code;
    
    @XmlElementWrapper(name = "offerings")
    @XmlElement(name = "offering")
    private List<UQRotaOffering> offerings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UQRotaOffering> getOfferings() {
        return DataHelpers.wrap(offerings);
    }

    public void setOfferings(List<UQRotaOffering> offerings) {
        this.offerings = DataHelpers.copyReplaceNull(offerings);
    }
    
    
}
