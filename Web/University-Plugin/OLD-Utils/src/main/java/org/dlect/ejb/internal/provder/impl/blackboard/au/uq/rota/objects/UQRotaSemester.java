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

import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.helpers.DataHelpers;
import org.eclipse.persistence.oxm.annotations.XmlPath;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "semester")
@XmlAccessorType(XmlAccessType.FIELD)
public class UQRotaSemester {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "year")
    private int year;

    @XmlPath("weeks/start/text()")
    private int startWeek;

    @XmlPath("weeks/finish/text()")
    private int endWeek;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public Date getStartDate() {
        Calendar c = DataHelpers.getZeroCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, startWeek);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        System.out.println(c.toString());

        return c.getTime();
    }

    public Date getEndDate() {
        Calendar c = DataHelpers.getZeroCalendar();

        // The end week is 'before' the start, so the semester goes over the end of a year - adjust the year to compensate.
        boolean overAYearEnd = endWeek < startWeek;

        c.set(Calendar.YEAR, (overAYearEnd ? year + 1 : year));
        c.set(Calendar.WEEK_OF_YEAR, endWeek);
        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        System.out.println(c.toString());

        return c.getTime();
    }

    @Override
    public String toString() {
        return "UQRotaSemester{" + "name=" + name + ", year=" + year + ", startWeek=" + startWeek + ", endWeek=" + endWeek + '}';
    }

}
