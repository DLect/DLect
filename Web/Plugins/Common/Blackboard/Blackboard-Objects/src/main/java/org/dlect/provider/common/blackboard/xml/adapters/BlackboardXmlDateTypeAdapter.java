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
package org.dlect.provider.common.blackboard.xml.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author lee
 */
public class BlackboardXmlDateTypeAdapter extends XmlAdapter<String, Date> {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    @Override
    public String marshal(Date v) throws Exception {
        DateFormat f = new SimpleDateFormat(DATE_FORMAT);
        return f.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        DateFormat f = new SimpleDateFormat(DATE_FORMAT);
        return f.parse(v);
    }

}
