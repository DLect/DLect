/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.immutable.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import org.dlect.model.Lecture;
import org.dlect.model.Stream;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class ImmutableSubject implements Comparable<ImmutableSubject> {

    private final String bbid;
    private final String name;
    private final ImmutableSortedSet<ImmutableLecture> lectures;
    private final ImmutableSortedSet<ImmutableStream> streams;

    public ImmutableSubject(String bbid, String name, Collection<ImmutableLecture> lectures, Collection<ImmutableStream> streams) {
        this.bbid = bbid;
        this.name = name;
        this.lectures = ImmutableSortedSet.copyOf(lectures);
        this.streams = ImmutableSortedSet.copyOf(streams);
    }

    @Override
    public int compareTo(ImmutableSubject o) {
        if (o == null) {
            // I am always greater than a null value.
            return 1;
        }
        int c_bbid = Ordering.natural().nullsLast().compare(this.getId(), o.getId());
        int c_name = Ordering.natural().nullsLast().compare(this.getName(), o.getName());
        if (c_bbid == 0) {
            // Equals consitent.
            return 0;
        }
        if (c_name == 0) {
            // Names are equal but BBIDs are not; use them to keep equals consitency.
            return c_bbid;
        }
        // Names and BBIDs not equal so use the name to get a nice sort.
        return c_name;
    }

    public void copyTo(Subject s) {
        s.setId(this.getId());
        s.setName(this.getName());
    }

    public Subject copyToNew() {
        Subject s = new Subject();
        copyTo(s);
        return s;
    }

    public String getId() {
        return bbid;
    }

    public String getName() {
        return name;
    }

    public ImmutableSortedSet<ImmutableLecture> getLectures() {
        return ImmutableSortedSet.copyOf(lectures);
    }

    public ImmutableSortedSet<ImmutableStream> getStreams() {
        return ImmutableSortedSet.copyOf(streams);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.bbid);
    }

    @Override
    public String toString() {
        return "ImmutableSubject{" + "bbid=" + bbid + ", name=" + name + ", lectures=" + lectures + ", streams=" + streams + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImmutableSubject other = (ImmutableSubject) obj;
        return Objects.equal(this.getId(), other.getId());
    }

    public static ImmutableSubject from(Subject s) {
        Set<ImmutableStream> streams = Sets.newHashSet();
        for (Stream stream : s.getStreams()) {
            streams.add(ImmutableStream.from(stream));
        }
        Set<ImmutableLecture> lectures = Sets.newHashSet();
        for (Lecture lecture : s.getLectures()) {
            lectures.add(ImmutableLecture.from(lecture));
        }

        return new ImmutableSubject(s.getId(), s.getName(), lectures, streams);

    }

}
