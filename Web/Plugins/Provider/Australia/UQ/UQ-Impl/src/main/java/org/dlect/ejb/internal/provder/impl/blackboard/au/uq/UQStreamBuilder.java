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
package org.dlect.ejb.internal.provder.impl.blackboard.au.uq;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.dlect.ejb.internal.provder.impl.blackboard.au.uq.rota.builders.UQRotaStorage;
import org.dlect.internal.data.merge.PartialStream;
import org.dlect.log.Customisers;
import org.dlect.plugin.provider.australia.uq.rota.UQCampus;
import org.dlect.plugin.provider.australia.uq.rota.UQStream;
import org.dlect.plugin.provider.australia.uq.rota.UQStreamEventData;

/**
 *
 * @author lee
 */
public class UQStreamBuilder {

    public static List<String> getStreamsFor(UQRotaStorage storage, String semCode, String subName, Date d, UQCampus campus, String building,
                                             String room) {
        try {
            ImmutableMultimap<UQStream, UQStreamEventData> streamData = storage.getStreamData(subName, Integer.parseInt(semCode));
            ImmutableMultimap<UQStreamEventData, UQStream> inverse = streamData.inverse();

            UQStreamEventData data = new UQStreamEventData(campus, building, room, d);

            ImmutableCollection<UQStream> streams = inverse.get(data);
            List<String> stream = Lists.newArrayList();
            Customisers.LOG.error("||\t{}", data);
            for (UQStream s : streams) {
                Customisers.LOG.error("||\t\t\t{}", s.getName());
                stream.add(s.getName());
            }
            Customisers.LOG.error("||\t{}", stream);

            return stream;
        } catch (ExecutionException | NumberFormatException e) {
            Customisers.LOG.error("Error getting semesters for: " + semCode + "; " + subName + "; on " + d + "; campus: " + campus + "; bldg:" + building + "; rm:" + room);
            return ImmutableList.of();
        }
    }

    public static List<PartialStream> getAllStreamsFor(UQRotaStorage storage, String semesterId, String subjectId) {
        ImmutableMultimap<UQStream, UQStreamEventData> streamData;
        try {
            streamData = storage.getStreamData(subjectId, Integer.parseInt(semesterId));
        } catch (ExecutionException ex) {
            Customisers.LOG.error("Failed to load Stream data", ex);
            return ImmutableList.of();
        }
        List<PartialStream> ret = Lists.newArrayList();
        for (UQStream uQStream : streamData.keySet()) {
            ret.add(toPartialStream(uQStream));
        }
        Customisers.LOG.error("SSSSS: {}", ret);
        return ret;
    }

    public static Optional<PartialStream> getStreamFor(UQRotaStorage storage, String semesterId, String subjectId, String streamId) {
        ImmutableMultimap<UQStream, UQStreamEventData> streamData;
        try {
            streamData = storage.getStreamData(subjectId, Integer.parseInt(semesterId));
        } catch (ExecutionException ex) {
            Customisers.LOG.error("Failed to load Stream data", ex);
            return Optional.absent();
        }

        UQStream stream = null;

        for (UQStream s : streamData.keySet()) {
            if (streamId.equals(s.getName())) {
                stream = s;
                break;
            }
        }

        if (stream == null) {
            return Optional.absent();
        }
        PartialStream st = toPartialStream(stream);

        return Optional.of(st);
    }

    public static PartialStream toPartialStream(UQStream stream) {
        PartialStream st = new PartialStream();
        st.setName(stream.getName());

//
//        List<Date> events = Lists.newArrayList();
//
//        for (UQStreamEventData event : stream.getEvents()) {
//            events.add(event.getRecordStart());
//        }
//        st.setEvents(events);
        return st;
    }

    private UQStreamBuilder() {
    }

}
