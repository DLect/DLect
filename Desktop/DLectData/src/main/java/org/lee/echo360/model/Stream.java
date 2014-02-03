/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSortedSet;
import java.io.Serializable;
import java.util.SortedSet;
import org.lee.echo360.util.SetUtils;

/**
 *
 * @author Lee Symes
 */
public class Stream implements Comparable<Stream>,
        Serializable {

    static final long serialVersionUID = 6555706723595657456L;
    private final String name;
    private final long number;
    private final SortedSet<Lecture> allLectures;
    private final boolean actualStream;

    protected Stream(String name, int number, SortedSet<Lecture> lectures) {
        this(name, number, true, lectures);
    }

    protected Stream(String name, int number, boolean actualStream, SortedSet<Lecture> lectures) {
        if (name == null) {
            throw new NullPointerException("Given Stream name is null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Given stream name is empty");
        }
        this.allLectures = lectures;
        this.name = name;
        this.number = number;
        this.actualStream = actualStream;
    }

    /**
     * Get the value of selected
     *
     * @return the value of selected
     */
    public boolean isEnabled() {
        for (Lecture l : getLectures()) {
            if (!l.isEnabled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Set the value of selected
     *
     * @param enabled new value of selected
     */
    public void setEnabled(boolean enabled) {
        for (Lecture lecture : getLectures()) {
            lecture.setEnabled(enabled);
        }
    }

    @Override
    public int compareTo(Stream o) {
        if (this.actualStream == o.isActualStream()) {
            int numCmp = Math.round(Math.signum(this.number - o.getNumber()));
            if (numCmp == 0) {
                return this.getName().compareTo(o.getName());
            } else {
                return numCmp;
            }
        } else {
            return (this.actualStream /* == true */ ? -1 : 1);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.name.hashCode();
        hash = 41 * hash + (int) (this.number ^ (this.number >>> 32));
        hash = 41 * hash + (this.actualStream ? 1 : 0);
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
        final Stream other = (Stream) obj;
        if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.number != other.number) {
            return false;
        }
        if (this.actualStream != other.actualStream) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Stream{" + "name=" + name + ", number=" + number + ", enabled=" + isEnabled() + ", actualStream=" + actualStream + '}';
    }

    public int getCount() {
        return getLectures().size();
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return (int) number;
    }

    public boolean isActualStream() {
        return actualStream;
    }

    public SortedSet<Lecture> getLectures() {
        synchronized (allLectures) {
            return ImmutableSortedSet.copyOf(Collections2.filter(allLectures, new SetUtils.Predicate<Lecture>() {
                @Override
                public boolean apply(Lecture input) {
                    return input.getStreams().contains(Stream.this);
                }
            }));
        }
    }
}
