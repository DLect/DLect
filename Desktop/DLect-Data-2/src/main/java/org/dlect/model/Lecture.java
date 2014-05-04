/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.dlect.events.EventID;
import org.dlect.events.listenable.EventBuilder;
import org.dlect.model.formatter.DownloadType;
import org.dlect.model.helper.ImmutableDate;
import org.dlect.model.helper.XmlListenable;

import static org.dlect.model.helper.ImmutableDate.of;

/**
 *
 * @author lee
 */
public class Lecture extends XmlListenable<Lecture> implements Comparable<Lecture> {

    @XmlElement(name = "contentid")
    private String contentID;
    @XmlElement(name = "time")
    private Date time;
    @XmlElement(name = "enabled")
    private boolean enabled = false;

    @XmlElementWrapper(name = "streams")
    @XmlElement(name = "stream")
    private final SortedSet<Stream> streams;

    @XmlElementWrapper(name = "downloads")
    @XmlElement(name = "download")
    private final Map<DownloadType, LectureDownload> lectureDownloads;

    public Lecture() {
        streams = newWrappedSortedSet(LectureEventID.STREAM);
        lectureDownloads = newWrappedListenableValueMap(LectureEventID.LECTURE_DOWNLOAD);
    }

    @Override
    public int compareTo(Lecture o) {
        if (o == null) {
            // I am always greater than a null value.
            return 1;
        }
        int c_cid = ordering().compare(this.getContentID(), o.getContentID());
        int c_time = ordering().compare(this.getTime(), o.getTime());
        if (c_cid == 0) {
            // Equals consitent.
            return 0;
        } else if (c_time == 0) {
            // Names are equal but BBIDs are not; use BBID to keep equals consitency.
            return c_cid;
        } else {
            // Names and BBIDs not equal so use the name to get a nice sort.
            return c_time;
        }
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        EventBuilder<String> b = event(LectureEventID.CONTENT_ID).before(getContentID());
        this.contentID = contentID;
        b.after(getContentID()).fire();
    }

    public ImmutableDate getTime() {
        return of(time);
    }

    public void setTime(Date time) {
        EventBuilder<Date> b = event(LectureEventID.TIME).before(getTime());
        this.time = of(time);
        b.after(getTime()).fire();
    }

    public ImmutableSortedSet<Stream> getStreams() {
        return copyOf(streams);
    }

    public void setStreams(Collection<Stream> streams) {
        setSet(this.streams, streams);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        EventBuilder<Boolean> b = event(LectureEventID.ENABLED).before(isEnabled());
        this.enabled = enabled;
        b.after(isEnabled()).fire();
    }

    public ImmutableMap<DownloadType, LectureDownload> getLectureDownloads() {
        return copyOf(lectureDownloads);
    }

    public void setLectureDownloads(Map<DownloadType, LectureDownload> lectureDownloads) {
        setMap(this.lectureDownloads, lectureDownloads);
    }

    @Override
    public String toString() {
        return "Lecture{" + "contentID=" + contentID + ", time=" + time + ", enabled=" + enabled + ", streams=" + streams + ", lectureDownloads=" + lectureDownloads + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.contentID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lecture other = (Lecture) obj;
        if (!Objects.equals(this.contentID, other.contentID)) {
            return false;
        }
        return true;
    }

    public static enum LectureEventID implements EventID {

        CONTENT_ID, TIME, STREAM, ENABLED, LECTURE_DOWNLOAD;

        @Override
        public Class<?> getAppliedClass() {
            return Lecture.class;
        }

        @Override
        public String getName() {
            return name();
        }

    }

}
