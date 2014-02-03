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
package org.dlect.internal.data.merge;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;


public class PartialLectureWithStream extends PartialLecture {
    
    Set<String> streamIds;
    
    public Set<String> getStreamIds() {
        return streamIds;
    }

    public void setStreamIds(Collection<String> streamIds) {
        this.streamIds = Sets.newHashSet(streamIds);
    }
}
