/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.immutable.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Stream;
import org.dlect.model.formatter.DownloadType;
import org.dlect.model.helper.ImmutableDate;

/**
 *
 * @author lee
 */
public class ImmutableLecture implements Comparable<ImmutableLecture> {

    private final String contentID;
    private final ImmutableDate time;
    private final boolean enabled;

    private final ImmutableSortedSet<ImmutableStream> streams;
    private final ImmutableMap<DownloadType, ImmutableLectureDownload> lectureDownloads;

    public ImmutableLecture(String contentID, Date time, boolean enabled, Collection<ImmutableStream> streams, Map<DownloadType, ImmutableLectureDownload> lectureDownloads) {
        this.contentID = contentID;
        this.time = ImmutableDate.of(time);
        this.enabled = enabled;
        this.streams = ImmutableSortedSet.copyOf(streams);
        this.lectureDownloads = ImmutableMap.copyOf(lectureDownloads);
    }

    public String getContentID() {
        return contentID;
    }

    public ImmutableDate getTime() {
        return ImmutableDate.of(time);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ImmutableSortedSet<ImmutableStream> getStreams() {
        return ImmutableSortedSet.copyOf(streams);
    }

    public ImmutableMap<DownloadType, ImmutableLectureDownload> getLectureDownloads() {
        return ImmutableMap.copyOf(lectureDownloads);
    }

    @Override
    public int compareTo(ImmutableLecture o) {
        if (o == null) {
            // I am always greater than a null value.
            return 1;
        }
        int c_cid = Ordering.natural().nullsLast().compare(this.getContentID(), o.getContentID());
        int c_time = Ordering.natural().nullsLast().compare(this.getTime(), o.getTime());
        if (c_cid == 0) {
            // Equals consitent.
            return 0;
        } else if (c_time == 0) {
            // Names are equal but BBIDs are not; use them to keep equals consitency.
            return c_cid;
        } else {
            // Names and BBIDs not equal so use the name to get a nice sort.
            return c_time;
        }
    }

    @Override
    public String toString() {
        return "ImmutableLecture{" + "contentID=" + contentID + ", time=" + time + ", enabled=" + enabled + ", streams=" + streams + '}';
    }

    public static ImmutableLecture from(Lecture lecture) {
        Set<ImmutableStream> stream = Sets.newHashSet();
        for (Stream s : lecture.getStreams()) {
            stream.add(ImmutableStream.from(s));
        }

        Map<DownloadType, ImmutableLectureDownload> ldMap = Maps.newEnumMap(DownloadType.class);
        for (Entry<DownloadType, LectureDownload> e : lecture.getLectureDownloads().entrySet()) {
            ldMap.put(e.getKey(), ImmutableLectureDownload.from(e.getValue()));
        }

        return new ImmutableLecture(lecture.getContentID(), lecture.getTime(), lecture.isEnabled(), stream, ldMap);

    }

}
