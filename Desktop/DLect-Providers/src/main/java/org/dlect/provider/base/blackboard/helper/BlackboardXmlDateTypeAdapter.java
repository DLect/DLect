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
package org.dlect.provider.base.blackboard.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.dlect.model.helper.ImmutableDate;
import org.dlect.model.helper.ThreadLocalDateFormat;

/**
 *
 * @author lee
 */
public class BlackboardXmlDateTypeAdapter extends XmlAdapter<String, ImmutableDate> {

    /**
     * TODO(Later) check that this applies to universities in GMT+0.
     */
    public static final ThreadLocalDateFormat DATE_FORMAT = new ThreadLocalDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Override
    public String marshal(ImmutableDate v) throws Exception {
        return DATE_FORMAT.format(v);
    }

    @Override
    public ImmutableDate unmarshal(String v) throws Exception {
        return ImmutableDate.of(DATE_FORMAT.parse(v));
    }

}
