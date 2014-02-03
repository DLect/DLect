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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.export.Lecture;
import org.dlect.export.LectureContent;
import org.dlect.export.Semester;
import org.dlect.export.Subject;
import org.dlect.internal.data.merge.PartialLectureContent;
import org.dlect.internal.data.merge.PartialLectureWithStream;
import org.dlect.log.Customisers;
import org.dlect.object.CommonDataType;
import org.dlect.object.Pair;

import static org.dlect.helpers.RegularExpressionHelper.findGroup;

/**
 *
 * @author lee
 */
public class Echo360BlackboardHelper {

    private static final String LINK_TYPE = "resource/x-apreso";
    /**
     * Cannot create a {@link SimpleDateFormat} object as they are not thread safe.
     */
    private static final String DATE_PARSE = "yyyy-MM-dd HH:mm:ss";
    private static final Pattern DATE_REGEXP = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");

    private static final List<Pair<String, CommonDataType>> UQ_LECTOPIA_DATA_TYPES = ImmutableList.of(Pair.of("m4v", CommonDataType.VIDEO),
                                                                                                      Pair.of("mp3", CommonDataType.AUDIO));

    private final BlackboardEcho360LectureDataStorage storage;

    private final BlackboardEcho360LectureDataCustomiser customiser;

    public Echo360BlackboardHelper(BlackboardEcho360LectureDataStorage storage, BlackboardEcho360LectureDataCustomiser customiser) {
        this.storage = storage;
        this.customiser = customiser;
    }

    public Set<PartialLectureContent> getDownloadsFor(Semester semester, Subject subject, Lecture l) {
        Set<PartialLectureContent> content = Sets.newHashSet();
        if (l.getContents() == null) {
            Customisers.LOG.error("Lecture has no Data Contents.\nLecture: " + l + "\nSubject: " + subject);
            return content;
        }
        String baseUrl = null;
        for (LectureContent lc : l.getContents()) {
            if (CommonDataType.BASE_URL.sameAs(lc.getType())) {
                baseUrl = lc.getUrl();
                break;
            }
        }

        if (baseUrl == null) {
            Customisers.LOG.error("Lecture does not contain a BASE_URL.\nContents: " + l.getContents() + "\nLecture: " + l + "\nSubject: " + subject);
            return content;
        }

        String s;
        //s = getBaseDownloadUrlFrom(baseUrl);

        for (Pair<String, CommonDataType> pair : UQ_LECTOPIA_DATA_TYPES) {
        }

        return null;
    }

    public Optional<PartialLectureWithStream> getLectureDataFor(Semester semester, Subject subject, URL rootUrl,
                                                                BlackboardCourseMapItem item) {
        if (LINK_TYPE.equals(item.getLinktype())) {
            String dateString;
            final String name = item.getName();
            try {
                dateString = findGroup(DATE_REGEXP, name, 1);
            } catch (IllegalStateException e) {
                Customisers.LOG.error("Error getting data from \"{}\" using regex \"{}\"", name, DATE_REGEXP);
                return Optional.absent();
            }
            try {
                Date d = new SimpleDateFormat(DATE_PARSE).parse(dateString);

                List<PartialLectureContent> lectureContent = getDataUrlsFor(rootUrl, item);

                List<String> streamIds = customiser.getStreamsFor(semester, subject, d, name);
                String location = customiser.getLectureLocation(semester, subject, d, name);

                PartialLectureWithStream customiserLecture = new PartialLectureWithStream();
                customiserLecture.setLectureContent(lectureContent);
                customiserLecture.setLocation(location);

                customiserLecture.setRecordedDate(d);
                customiserLecture.setStreamIds(streamIds);

                return Optional.of(customiserLecture);
            } catch (ParseException | DLectException ex) {
                Customisers.LOG.error("Error parsing date: " + dateString, ex);
                return Optional.absent();
            }
        }
        return Optional.absent();
    }

    public List<PartialLectureContent> getDataUrlsFor(URL rootUrl, BlackboardCourseMapItem item) {
        try {
            URL itemUrl = stripRef(new URL(rootUrl, item.getViewurl()));

            Map<String, URL> datas = storage.getBaseLectureUrl(itemUrl.toString());

            URL dataUrl = datas.get(item.getContentid());
            if (dataUrl == null) {
                throw CommonExceptionBuilder.getIllegalReturnTypeException("getBaseLectureUrl does not contain BBID: '" + item.getContentid() + "'", storage, datas);
            }

            PartialLectureContent plc = new PartialLectureContent();
            plc.setDataType(CommonDataType.BASE_URL.toString());
            plc.setUrl(dataUrl.toString());

            return ImmutableList.of(plc);
        } catch (DLectException | MalformedURLException | ExecutionException ex) {
            Customisers.LOG.error("Error getting data urls from: " + rootUrl + "; and item " + item, ex);
            return ImmutableList.of();
        }

    }

    protected static URL stripRef(URL viewurl) throws MalformedURLException {
        return new URL(viewurl, "#");
    }

}
