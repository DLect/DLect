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

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Maps;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.apache.http.client.methods.HttpGet;
import org.dlect.internal.beans.HttpConfigurationBean;
import org.dlect.log.Customisers;

/**
 *
 * @author lee
 */
@Stateless
public class BlackboardEcho360LectureDataBuilder extends CacheLoader<String, Map<String, URL>> {

    /**
     * This RegExp extracts the Blackboard ID and the Echo360 URL from a page.
     *
     * Groups:
     * 1: Blackboard Content ID;
     * 2: Base URL for Echo360 Data;
     *
     */
    protected static final Pattern LECTURE_DATA_REGEXP = Pattern.compile("<li id=\"contentListItem:([^\"]+)\".*?<a .*?href=\"([^\"]+)\"", Pattern.DOTALL);

    @Inject
    private Instance<HttpConfigurationBean> httpBean;

    @Override
    public Map<String, URL> load(String key) throws Exception {
        URL u = new URL(key);

        HttpConfigurationBean hcb = httpBean.get();

        String htmlString = hcb.getAsString(new HttpGet(u.toURI()));

        Map<String, URL> dataTable = Maps.newHashMap();

        Matcher m = LECTURE_DATA_REGEXP.matcher(htmlString);

        while (m.find()) {
            String bbid = m.group(1);
            String baseUrl = m.group(2);
            try {
                URL audioUrl = new URL(u, baseUrl);
                dataTable.put(bbid, audioUrl);
            } catch (MalformedURLException e) {
                Customisers.LOG.error("Failed to create url for BBCID:" + bbid + " and url: \"" + baseUrl + "\"", e);
            }
        }

        return dataTable;
    }

}
