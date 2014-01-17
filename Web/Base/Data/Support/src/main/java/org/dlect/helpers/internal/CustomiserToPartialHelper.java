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
package org.dlect.helpers.internal;

import java.net.URL;
import java.util.Map.Entry;
import org.dlect.internal.data.merge.PartialLectureContent;

/**
 *
 * @author lee
 */
public class CustomiserToPartialHelper {

    public static PartialLectureContent createLectureData(Entry<String, URL> entry) {
        PartialLectureContent c = new PartialLectureContent();

        c.setDataType(entry.getKey());
        c.setUrl(entry.getValue().toString());

        return c;
    }

    private CustomiserToPartialHelper() {
    }

}
