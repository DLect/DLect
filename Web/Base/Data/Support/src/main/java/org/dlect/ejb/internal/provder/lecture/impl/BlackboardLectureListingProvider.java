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
package org.dlect.ejb.internal.provder.lecture.impl;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.dlect.ejb.internal.provder.impl.blackboard.BlackboardUniversityCustomisation;
import org.dlect.ejb.internal.provder.impl.blackboard.BlackboardUniversityCustomisationProvider;
import org.dlect.ejb.internal.provder.lecture.LectureListingProvider;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.export.University;
import org.dlect.internal.beans.HttpConfigurationBean;
import org.dlect.internal.beans.LoginCredentialBean;
import org.dlect.internal.data.SubjectData;
import org.dlect.internal.data.merge.PartialLecture;
import org.dlect.internal.data.merge.PartialLectureWithStream;
import org.dlect.internal.data.merge.PartialStream;
import org.dlect.internal.object.ProviderLectureData;
import org.dlect.log.Utils;
import org.dlect.object.ResultType;

import static org.dlect.db.DatabaseToExport.*;
import static org.dlect.helpers.JAXBHelper.responseHandlerFor;

@ApplicationScoped
public class BlackboardLectureListingProvider implements LectureListingProvider {

    private static final ResponseHandler<BlackboardCourseMapResponse> CONTENT_HANDLER = responseHandlerFor(BlackboardCourseMapResponse.class);

    @Inject
    private Instance<HttpConfigurationBean> httpBeanInjector;

    @Inject
    private Instance<LoginCredentialBean> loginBeanInjector;

    @Inject
    private Instance<BlackboardUniversityCustomisationProvider> uniCustomiser;

    @Override
    public ProviderLectureData getLecturesIn(SubjectData sub) throws DLectException {

        HttpConfigurationBean httpBean = httpBeanInjector.get();
        LoginCredentialBean loginBean = loginBeanInjector.get();
        final University uni = loginBean.getUniversity();

        BlackboardUniversityCustomisation buc = uniCustomiser.get().getCustomiserFor(uni);

        final URI url;
        try {
            url = new URL(sub.getUrl()).toURI();
        } catch (MalformedURLException | URISyntaxException ex) {
            throw CommonExceptionBuilder.getIllegalReturnTypeException("Subject was stored with an invalid url", ex, sub, sub.getUrl());
        }

        Utils.LOG.error("Accessing URL: {}", url);

        BlackboardCourseMapResponse bbcl;
        try {
            bbcl = httpBean.getClient().execute(new HttpGet(url), CONTENT_HANDLER);
        } catch (IOException ex) {
            throw DLectExceptionBuilder.build("Failed to get or parse content of " + url, ex, ResultType.UNIVERSITY_ERROR, "Failed to access university");
        }

        if (bbcl.getMap() == null) {
            // Nothing found, respond to the user with no lists.
            return new ProviderLectureData();
        }

        URL rootUrl;
        try {
            rootUrl = new URL(bbcl.getRootUrl());
        } catch (MalformedURLException ex) {
            throw DLectExceptionBuilder.build("Unparsable root url: " + bbcl.getRootUrl(), ex, ResultType.UNIVERSITY_ERROR, "Failed to access university");
        }

        List<BlackboardCourseMapItem> allItems = unwrapCourseTree(bbcl.getMap());

        Multimap<PartialLecture, PartialStream> data = HashMultimap.create();
        Map<String, PartialStream> streamIds = Maps.newHashMap();
        {
            List<PartialStream> streamData = buc.getAllStreamData(export(sub.getSemesterId(), nothing()), export(sub, nothing()));
            if (streamData != null) {
                for (PartialStream ps : streamData) {
                    streamIds.put(ps.getName(), ps);
                }
            }
        }

        List<PartialLecture> lecData = Lists.newArrayList();

        for (BlackboardCourseMapItem item : allItems) {
            Optional<PartialLectureWithStream> lectureDataFor = buc.getLectureDataFor(export(sub.getSemesterId(), nothing()), export(sub, nothing()), rootUrl, item);

            if (!lectureDataFor.isPresent()) {
                continue;
            }
            PartialLectureWithStream d = lectureDataFor.get();

            for (String sId : d.getStreamIds()) {
                PartialStream stream = streamIds.get(sId);
                if (stream == null) {
                    Optional<PartialStream> getStream = buc.getStreamDataFor(export(sub.getSemesterId(), nothing()), export(sub, nothing()), sId);

                    if (!getStream.isPresent()) {
                        continue;
                    }

                    streamIds.put(sId, getStream.get());

                }
                data.put(d, stream);
            }

            lecData.add(d);
        }

        return new ProviderLectureData(lecData, streamIds.values(), data);
    }
// TODO test.

    protected List<BlackboardCourseMapItem> unwrapCourseTree(List<BlackboardCourseMapItem> map) {
        List<BlackboardCourseMapItem> returnList = Lists.newArrayList();

        List<BlackboardCourseMapItem> toParseList = Lists.newArrayList(map);

        while (!toParseList.isEmpty()) {
            BlackboardCourseMapItem currentItem = toParseList.remove(0);
            returnList.add(currentItem);
            if (currentItem.getChildren() != null) {
                toParseList.addAll(currentItem.getChildren());
            }
        }

        return returnList;
    }

}
