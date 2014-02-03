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

import com.google.common.collect.ImmutableMap;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.dlect.ejb.internal.provder.impl.blackboard.au.uq.rota.builders.UQRotaStorage;
import org.dlect.except.DLectException;
import org.dlect.log.Customisers;
import org.dlect.object.CommonDataType;
import org.dlect.provider.common.blackboard.xml.BlackboardCourseMapItem;

/**
 *
 * @author lee
 */
public class UQLectureBuilder {
//TODO test.

    public static Map<String, URL> getDataUrlsFor(UQRotaStorage storage, URL rootUrl, BlackboardCourseMapItem item) throws DLectException {

        try {
            URL itemUrl = stripRef(new URL(rootUrl, item.getViewurl()));

            Map<String, URL> datas = storage.getBaseLectureUrl(itemUrl.toString());

            URL dataUrl = datas.get(item.getContentid());

            return ImmutableMap.of(CommonDataType.BASE_URL.toString(), dataUrl);
        } catch (MalformedURLException | ExecutionException ex) {
            Customisers.LOG.error("Error getting data urls from: " + rootUrl + "; and item " + item, ex);
            return ImmutableMap.of();
        }

    }
// TODO test.

    protected static URL stripRef(URL viewurl) throws MalformedURLException {
        return new URL(viewurl, "#");
    }
}
