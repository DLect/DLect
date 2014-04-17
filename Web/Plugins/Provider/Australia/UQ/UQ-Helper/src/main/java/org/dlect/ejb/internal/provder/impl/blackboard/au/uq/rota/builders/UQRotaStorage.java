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

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.dlect.ejb.internal.provder.impl.blackboard.echo360.BlackboardEcho360LectureDataStorage;
import org.dlect.ejb.internal.provder.impl.blackboard.echo360.impl.BlackboardEcho360LectureDataStorageImpl;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaOffering;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSemester;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSubject;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSubjectSemesterPair;
import org.dlect.plugin.provider.australia.uq.rota.UQStream;
import org.dlect.plugin.provider.australia.uq.rota.UQStreamEventData;

import static org.dlect.cache.CommonCacheSpecs.SHORT_TERM_WRITE_SPEC;

/**
 *
 * @author lee
 */
@Stateless
public class UQRotaStorage implements BlackboardEcho360LectureDataStorage {

    /**
     * This value is pre-parsed to save an expensive operation on each creation. The object is thread-safe and
     * immutable.
     */
    private LoadingCache<Integer, UQRotaSemester> semesters;

    private LoadingCache<String, UQRotaSubject> subjects;

    private LoadingCache<UQRotaSubjectSemesterPair, Optional<UQRotaOffering>> offerings;

    /**
     * Key: String representing URL; Value: Table between BB Content ID, Data Type and Data URL.
     */
    private LoadingCache<UQRotaSubjectSemesterPair, Multimap<UQStream, UQStreamEventData>> streamData;

    @Inject
    private UQRotaSemesterBuilder semB;
    @Inject
    private UQRotaSubjectBuilder subB;
    @Inject
    private UQRotaOfferingBuilder offerB;
    @Inject
    private BlackboardEcho360LectureDataStorageImpl lectureData;
    @Inject
    private UQRotaStreamDataBuilder streamDataB;

    @PostConstruct
    protected void init() {
        this.streamData = CacheBuilder
                .from(SHORT_TERM_WRITE_SPEC)
                .build(streamDataB);

        this.offerings = CacheBuilder
                .from(SHORT_TERM_WRITE_SPEC)
                .build(offerB);

        this.subjects = CacheBuilder
                .from(SHORT_TERM_WRITE_SPEC)
                .build(subB);

        this.semesters = CacheBuilder
                .from(SHORT_TERM_WRITE_SPEC)
                .build(semB);

    }

    public UQRotaSemester getSemester(int semester) throws ExecutionException {
        return semesters.get(semester);
    }

    public UQRotaSubject getSubject(String subjectCode) throws ExecutionException {
        return subjects.get(subjectCode);
    }

    public UQRotaOffering getOffering(String subjectCode, int semesterCode) throws ExecutionException {
        return offerings.get(new UQRotaSubjectSemesterPair(subjectCode, semesterCode)).orNull();
    }

    public ImmutableMultimap<UQStream, UQStreamEventData> getStreamData(String subjectCode, int semesterCode) throws ExecutionException {
        return ImmutableMultimap.copyOf(streamData.get(new UQRotaSubjectSemesterPair(subjectCode, semesterCode)));
    }

    @Override
    public Map<String, URL> getBaseLectureUrl(String lrUrl) throws ExecutionException {
        return lectureData.getBaseLectureUrl(lrUrl);
    }

}
