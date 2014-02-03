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
package org.dlect.db;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import org.dlect.export.Lecture;
import org.dlect.export.LectureContent;
import org.dlect.export.Semester;
import org.dlect.export.Stream;
import org.dlect.export.Subject;
import org.dlect.export.University;
import org.dlect.helpers.DataHelpers;
import org.dlect.helpers.ExportIncludes;
import org.dlect.internal.data.LectureContentData;
import org.dlect.internal.data.LectureData;
import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.StreamData;
import org.dlect.internal.data.SubjectData;
import org.dlect.internal.data.UniversityData;
import org.dlect.log.Stores;

import static java.util.EnumSet.of;
import static org.dlect.helpers.EnumSetHelper.*;
import static org.dlect.helpers.ExportIncludes.*;

/**
 *
 * @author lee
 */
public class DatabaseToExport {

    public static EnumSet<ExportIncludes> nothing() {
        return EnumSet.noneOf(ExportIncludes.class);
    }

    public static University export(UniversityData data, EnumSet<ExportIncludes> include) {
        University u = new University();
        u.setCode(data.getCode());
        u.setName(data.getName());
        u.setUrl(data.getUrl());
        u.setSupport(data.getSupport());
        if (data.getTimeoutLong() > 0) {
            u.setTimeout(DataHelpers.wrapDate(data.getTimeout()));
        }

        if (include.contains(SEMESTER)) {
            List<Semester> semesters = export(data.getSemesterList(), without(include, UNIVERSITY));

            u.setSemesters(semesters);
        }
        return u;
    }

    public static List<Semester> export(List<SemesterData> data, EnumSet<ExportIncludes> include) {
        List<Semester> semesters = Lists.newArrayListWithCapacity(data.size());
        for (SemesterData sd : data) {
            semesters.add(export(sd, include));
        }
        return semesters;
    }

    public static Semester export(SemesterData data, EnumSet<ExportIncludes> include) {
        Semester s = new Semester();
        s.setId(data.getSemesterId());
        s.setCode(data.getSemesterCode());
        s.setName(data.getName());
        s.setStartDate(data.getStartDate());
        s.setEndDate(data.getEndDate());

        if (include.contains(SUBJECT)) {
            List<Subject> subjects = Lists.newArrayList();
            for (SubjectData sd : data.getSubjectList()) {
                Subject sub = export(sd, without(include, SEMESTER));
                sub.setSemesterByData(null);
                sub.setSemesterById(null);
                subjects.add(sub);
            }
            s.setSubjectList(subjects);
        }
        return s;
    }

    public static Subject export(SubjectData sd, EnumSet<ExportIncludes> include) {

        Subject s = new Subject();

        s.setSubjectId(sd.getSubjectId());
        s.setName(sd.getName());
        s.setDescription(sd.getDescription());

        if (include.contains(SEMESTER)) {
            Semester sem = export(sd.getSemesterId(), of(SEMESTER));
            s.setSemesterByData(sem);
        }

        Map<LectureData, Lecture> lectures = Maps.newHashMap();
        Map<StreamData, Stream> streams = Maps.newHashMap();
        if (include.contains(LECTURE)) {
            for (LectureData ld : sd.getLectureList()) {
                Lecture l = export(ld, include.clone());
                lectures.put(ld, l);
            }
            s.setLectures(lectures.values());
        }

        if (include.contains(STREAM)) {
            for (StreamData ld : sd.getStreamList()) {
                Stream l = export(ld, include.clone());
                streams.put(ld, l);
            }
            s.setStreams(streams.values());
        } else {
            //Stores.LOG.error("Include Without Stream: " + include, new IllegalStateException());
        }
        if (include.contains(STREAM) && include.contains(LECTURE)) {
            for (Entry<LectureData, Lecture> le : lectures.entrySet()) {
                LectureData lectureData = le.getKey();
                Lecture lecture = le.getValue();
                lectureData.getStreamData().toArray();
                SortedSet<Stream> streamForLec = Sets.newTreeSet();
                for (StreamData streamData : lectureData.getStreamData()) {
                    streamForLec.add(streams.get(streamData));
                }
                lecture.setStream(streamForLec);
            }
        }

        return s;
    }

    public static Lecture export(LectureData ld, EnumSet<ExportIncludes> include) {
        Lecture l = new Lecture();

        l.setRecordDate(ld.getRecordedDate());
        if (include.contains(LECTURE_CONTENT)) {
            List<LectureContent> contents = Lists.newArrayList();
            for (LectureContentData lectureContentData : ld.getLectureContentData()) {
                LectureContent c = export(lectureContentData);
                contents.add(c);
            }
            l.setContents(contents);
        }
        return l;
    }

    public static Stream export(StreamData ld, EnumSet<ExportIncludes> include) {
        Stream s = new Stream();

        s.setName(ld.getName());
        if (include.contains(STREAM_DATES)) {
            // TODO Implement this in the database.
        }
        return s;
    }

    private static LectureContent export(LectureContentData lectureContentData) {
        LectureContent c = new LectureContent();
        c.setType(lectureContentData.getDataType());
        c.setUrl(lectureContentData.getUrl());
        c.setId(lectureContentData.getDataId());

        Stores.LOG.error("R: " + c);
        
        return c;
    }

    private DatabaseToExport() {
    }

}
