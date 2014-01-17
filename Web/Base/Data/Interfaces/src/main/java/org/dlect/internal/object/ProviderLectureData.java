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
package org.dlect.internal.object;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.dlect.internal.data.merge.PartialLecture;
import org.dlect.internal.data.merge.PartialStream;

import static org.dlect.helpers.DataHelpers.wrap;

/**
 *
 * @author lee
 */
public class ProviderLectureData {

    private Set<PartialLecture> lectures;
    private Set<PartialStream> streams;
    private Multimap<PartialLecture, PartialStream> lectureStreamMapping;

    public ProviderLectureData() {
        this.lectureStreamMapping = HashMultimap.create();
        this.lectures = Sets.newHashSet();
        this.streams = Sets.newHashSet();
    }

    public ProviderLectureData(Collection<PartialLecture> lectures, Collection<PartialStream> streams,
                               Multimap<PartialLecture, PartialStream> lectureStreamMapping) {
        this.lectures = Sets.newHashSet(lectures);
        this.streams = Sets.newHashSet(streams);
        this.lectureStreamMapping = HashMultimap.create(lectureStreamMapping);
    }

    public boolean addLectureStreamMapping(PartialLecture ld, PartialStream sd) {
        lectures.add(ld);
        streams.add(sd);
        return lectureStreamMapping.put(ld, sd);
    }

    public Multimap<PartialLecture, PartialStream> getLectureStreamMapping() {
        return lectureStreamMapping;
    }

    public void setLectureStreamMapping(Multimap<PartialLecture, PartialStream> lectureStreamMapping) {
        this.lectureStreamMapping = lectureStreamMapping;
    }

    public void setLectures(Set<PartialLecture> lectures) {
        this.lectures = lectures;
    }

    public Set<PartialLecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<PartialLecture> lectures) {
        this.lectures = Sets.newHashSet(lectures);
    }

    public Set<PartialStream> getStreams() {
        return streams;
    }

    public void setStreams(Set<PartialStream> streams) {
        this.streams = Sets.newHashSet(streams);
    }

}
