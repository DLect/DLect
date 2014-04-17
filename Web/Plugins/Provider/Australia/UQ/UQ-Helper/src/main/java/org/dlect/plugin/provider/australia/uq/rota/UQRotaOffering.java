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
package org.dlect.plugin.provider.australia.uq.rota;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.helpers.DataHelpers;
import org.eclipse.persistence.oxm.annotations.XmlPath;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "offering")
@XmlAccessorType(XmlAccessType.FIELD)
public class UQRotaOffering {

    @XmlElement(name = "id")
    private int id;

    @XmlPath("semester/id/text()")
    private String semId;

    @XmlElementWrapper(name = "series")
    @XmlElement(name = "series")
    private List<UQRotaSeries> series;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSemId() {
        return Integer.parseInt(semId);
    }
    public String getSemIdString() {
        return semId;
    }

    public void setSemId(int semId) {
        this.semId = "" + semId;
    }

    public List<UQRotaSeries> getSeries() {
        return DataHelpers.wrap(series);
    }
    
    

    public void setSeries(List<UQRotaSeries> series) {
        this.series = DataHelpers.copyReplaceNull(series);
    }

}
