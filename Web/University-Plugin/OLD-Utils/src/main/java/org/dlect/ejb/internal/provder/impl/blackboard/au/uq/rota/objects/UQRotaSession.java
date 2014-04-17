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

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dlect.helpers.DataHelpers;
import org.eclipse.persistence.oxm.annotations.XmlPath;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UQRotaSession {

    @XmlElement(name = "startmins")
    private int startMins;
    @XmlElement(name = "finishmins")
    private int endMins;

    @XmlElement(name = "room")
    private String room;
    @XmlPath("building/campus/text()")
    private String campus;
    @XmlPath("building/number/text()")
    private String building;

    @XmlPath("events/event[@taught=\"true\"]/@date")
    @XmlJavaTypeAdapter(UQRotaSessionDateTypeAdapter.class)
    private List<Date> dates;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getStartMins() {
        return startMins;
    }

    public void setStartMins(int startMins) {
        this.startMins = startMins;
    }

    public int getEndMins() {
        return endMins;
    }

    public void setEndMins(int endMins) {
        this.endMins = endMins;
    }

    public List<Date> getDates() {
        return DataHelpers.wrapReplaceNull(dates);
    }

    public void setDates(List<Date> dates) {
        this.dates = DataHelpers.copyReplaceNull(dates);
    }

}
