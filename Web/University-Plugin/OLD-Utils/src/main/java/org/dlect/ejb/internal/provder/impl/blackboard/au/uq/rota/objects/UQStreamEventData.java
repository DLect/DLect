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
import java.util.Objects;

import static org.dlect.helpers.DataHelpers.copy;

/**
 *
 * @author lee
 */
public class UQStreamEventData {

    private final UQCampus campus;
    private final String building;
    private final String room;
    private final Date recordStart;

    public UQStreamEventData(UQCampus campus, String building, String room, Date recordStart) {
        this.campus = campus;
        this.building = building;
        this.room = room;
        this.recordStart = recordStart;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UQStreamEventData other = (UQStreamEventData) obj;
        if (this.campus != other.campus) {
            return false;
        }
        if (!Objects.equals(this.building, other.building)) {
            return false;
        }
        if (!Objects.equals(this.room, other.room)) {
            return false;
        }
        if (!Objects.equals(this.recordStart, other.recordStart)) {
            return false;
        }
        return true;
    }

    public String getBuilding() {
        return building;
    }

    public UQCampus getCampus() {
        return campus;
    }

    public Date getRecordStart() {
        return copy(recordStart);
    }

    public String getRoom() {
        return room;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.campus);
        hash = 67 * hash + Objects.hashCode(this.building);
        hash = 67 * hash + Objects.hashCode(this.room);
        hash = 67 * hash + Objects.hashCode(this.recordStart);
        return hash;
    }

    @Override
    public String toString() {
        return "UQStreamEventData{" + "campus=" + campus + ", building=" + building + ", room=" + room + ", recordStart=" + recordStart + '}';
    }

}
