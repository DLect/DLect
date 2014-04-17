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
package org.dlect.ejb.internal.provder.impl.blackboard.echo360;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.ejb.Local;
import org.dlect.object.CommonDataType;

/**
 *
 * @author lee
 */
@Local
public interface BlackboardEcho360LectureDataStorage {

    /**
     * This method gets the {@linkplain CommonDataType#BASE_URL} for each Blackboard ID on the page given. It is
     * recommended that the result
     * is cached for a short amount of time, as the methods that use this method do not perform any caching.
     *
     * @param lrUrl The plain lecture recording URL(Without anchor). If this is not a URL then an implementation may
     *              throw an
     *              ExecutionException.
     *
     * @return A Mapping between Blackboard IDs and the Base URL for the associated lecture recording.
     *
     * @throws java.util.concurrent.ExecutionException In the event of invalid parameters, or any problem loading the
     *                                                 data. Use
     *                                                 {@linkplain ExecutionException#getCause() } to obtain the true cause for the failure.
     */
    public Map<String, URL> getBaseLectureUrl(String lrUrl) throws ExecutionException;

}
