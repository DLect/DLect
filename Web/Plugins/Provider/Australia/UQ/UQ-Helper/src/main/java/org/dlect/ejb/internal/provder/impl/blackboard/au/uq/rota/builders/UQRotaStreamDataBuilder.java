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

package org.dlect.ejb.internal.provder.impl.blackboard.au.uq.rota.builders;

import com.google.common.base.CharMatcher;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.dlect.helpers.DataHelpers;
import org.dlect.log.Customisers;
import org.dlect.plugin.provider.australia.uq.rota.UQCampus;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaGroup;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaOffering;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSeries;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSession;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSubjectSemesterPair;
import org.dlect.plugin.provider.australia.uq.rota.UQStream;
import org.dlect.plugin.provider.australia.uq.rota.UQStreamEventData;

/**
 *
 * @author lee
 */
@Stateless
public class UQRotaStreamDataBuilder extends CacheLoader<UQRotaSubjectSemesterPair, Multimap<UQStream, UQStreamEventData>> {

    @Inject
    private Instance<UQRotaStorage> storage;

    private static final CharMatcher m = CharMatcher.is('0');

    public UQRotaStreamDataBuilder() {
        Customisers.LOG.error("UQRotaSubjectBuilder INIT");
    }

    @Override
    public Multimap<UQStream, UQStreamEventData> load(UQRotaSubjectSemesterPair key) throws Exception {
        UQRotaOffering offer = storage.get().getOffering(key.getSubjectCode(), key.getSemesterCode());
        if (offer == null) {
            return ImmutableMultimap.of();
        }

        Multimap<UQStream, UQStreamEventData> maps = HashMultimap.create(10, 20);

        for (UQRotaSeries series : offer.getSeries()) {
            String name = series.getName();

            boolean singleGroup = series.getGroups().isEmpty();

            for (UQRotaGroup group : series.getGroups()) {
                String num = singleGroup ? "" : m.trimLeadingFrom(group.getName());

                UQStream s = new UQStream(name + num);
                List<UQStreamEventData> data = Lists.newArrayList();

                for (UQRotaSession session : group.getSessions()) {
                    String building = session.getBuilding();
                    UQCampus campus = UQCampus.getMatching(session.getCampus());
                    String room = session.getRoom();

                    int start = session.getStartMins();

                    for (Date date : session.getDates()) {
                        Calendar sessionStart = DataHelpers.getZeroCalendar();
                        sessionStart.setTime(date);
                        sessionStart.add(Calendar.MINUTE, start);
                        data.add(new UQStreamEventData(campus, building, room, sessionStart.getTime()));
                    }
                }
                s.setEvents(data);
                maps.putAll(s, data);
            }
        }

        return maps;
    }

}
