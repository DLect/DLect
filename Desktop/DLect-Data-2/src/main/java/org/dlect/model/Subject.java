/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.dlect.events.EventID;
import org.dlect.events.listenable.EventBuilder;
import org.dlect.model.formatter.PlaylistFormat;
import org.dlect.model.formatter.TagFormat;
import org.dlect.model.helper.XmlListenable;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Subject extends XmlListenable<Subject> implements Comparable<Subject> {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "tagformat")
    private final TagFormat tagFormat;

    @XmlElement(name = "playlistformat")
    private final PlaylistFormat playlistFormat;

    @XmlElementWrapper(name = "lectures")
    @XmlElement(name = "lecture")
    private final SortedSet<Lecture> lectures;

    @XmlElementWrapper(name = "streams")
    @XmlElement(name = "stream")
    private final SortedSet<Stream> streams;

    public Subject() {
        lectures = newWrappedListenableSortedSet(SubjectEventID.LECTURE);
        streams = newWrappedListenableSortedSet(SubjectEventID.STREAM);
        tagFormat = new TagFormat();
        playlistFormat = new PlaylistFormat();
    }

    @Override
    public int compareTo(Subject o) {
        if (o == null) {
            // I am always greater than a null value.
            return 1;
        }
        int c_bbid = ordering().compare(this.getId(), o.getId());
        int c_name = ordering().compare(this.getName(), o.getName());
        if (c_bbid == 0) {
            return 0; // Equals consitent.
        }
        if (c_name == 0) {
            // Names are equal but BBIDs are not; use them to keep equals consitency.
            return c_bbid;
        }
        // Names and BBIDs not equal so use the name to get a nice sort.
        return c_name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Subject other = (Subject) obj;
        return Objects.equal(this.getId(), other.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String bbid) {
        EventBuilder<String> b = event(SubjectEventID.ID).before(getId());
        this.id = bbid;
        b.after(getId()).fire();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        EventBuilder<String> b = event(SubjectEventID.NAME).before(getName());
        this.name = name;
        b.after(getName()).fire();
    }

    public SortedSet<Lecture> getLectures() {
        return copyOf(lectures);
    }

    public void addLecture(Lecture l) {
        this.lectures.add(l);
    }

    public void setLectures(Collection<Lecture> lectures) {
        setSet(this.lectures, lectures);
    }

    public SortedSet<Stream> getStreams() {
        return copyOf(streams);
    }

    public void addStream(Stream s) {
        this.streams.add(s);
    }

    public void setStreams(Collection<Stream> streams) {
        setSet(this.streams, streams);
    }

    @Nonnull
    public TagFormat getTagFormat() {
        return tagFormat;
    }

    public void setTagFormat(TagFormat format) {
        this.tagFormat.setFormats(format);
    }

    @Nonnull
    public PlaylistFormat getPlaylistFormat() {
        return playlistFormat;
    }

    public void setPlaylistFormat(PlaylistFormat format) {
        this.playlistFormat.setFormats(format);
    }

    public static enum SubjectEventID implements EventID {

        ID, NAME, LECTURE, STREAM;

        @Override
        public Class<?> getAppliedClass() {
            return Subject.class;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public boolean isUniqueId() {
            return this == ID;
        }

    }

}
