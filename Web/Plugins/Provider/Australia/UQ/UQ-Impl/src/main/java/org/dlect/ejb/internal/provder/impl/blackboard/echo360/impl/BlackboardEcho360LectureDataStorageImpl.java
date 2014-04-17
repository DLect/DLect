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

package org.dlect.ejb.internal.provder.impl.blackboard.echo360.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.dlect.ejb.internal.provder.impl.blackboard.echo360.BlackboardEcho360LectureDataStorage;

import static org.dlect.cache.CommonCacheSpecs.SHORT_TERM_WRITE_SPEC;

@Stateless
public class BlackboardEcho360LectureDataStorageImpl implements BlackboardEcho360LectureDataStorage {


    /**
     * Key: String representing URL; Value: Table between BB Content ID, Data Type and Data URL.
     */
    private LoadingCache<String, Map<String, URL>> lectureData;
    /**
     * Implementation for loading data into {@link #lectureData}.
     */
    @Inject
    private BlackboardEcho360LectureDataBuilder lecDataB;

    @PostConstruct
    public void init() {
        this.lectureData = CacheBuilder
                .from(SHORT_TERM_WRITE_SPEC)
                .build(lecDataB);
    }

    @Override
    public Map<String, URL> getBaseLectureUrl(String lrUrl) throws ExecutionException {
        return this.lectureData.get(lrUrl);
    }

}
