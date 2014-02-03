/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import com.google.common.collect.ImmutableSortedSet;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class Subject implements Comparable<Subject>, PropertyChangeListener,
        Serializable {

    static final long serialVersionUID = 0x20def4;
    public static final String PROP_ADDED_LECTURE = Subject.class + " Added Lecure";
    public static final String PROP_CLEARED_LECTURES = Subject.class + " Cleared Lecures";
    public static final String PROP_DOWNLOAD_ENABLED = Subject.class + " displayed";
    private transient PropertyChangeSupport pcs;
    private final String bbid;
    private final String name;
    private final int semesterCode;
    private final String courseID;
    private final String folderName;
    private final Semester semester;
    private final SortedSet<Lecture> lectures;
    private final SortedSet<Stream> streams;
    private final PlaylistFormatter plFormatter = new PlaylistFormatter();
    private final TagFormatter tagFormatter = new TagFormatter();
    private boolean downloadEnabled = false;

    public Subject(String name, String bbid, String fileName, String courseID, int semesterCode, Semester semester) {
        this.bbid = bbid;
        this.name = name;
        this.semesterCode = semesterCode;
        this.lectures = new TreeSet<Lecture>();
        this.streams = new TreeSet<Stream>();
        this.folderName = fileName;
        this.courseID = courseID;
        this.semester = semester;
    }

    private void initPCS() {
        if (pcs == null) {
            initPCS_();
        }
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public Semester getSemester() {
        return semester;
    }

    public String getBlackboardId() {
        return bbid;
    }

    public String getName() {
        return name;
    }

    public String getFolderName() {
        return folderName;
    }

    /**
     * This returns an immutable <b>COPY</b> of the lectures list.
     *
     * @return
     */
    public SortedSet<Lecture> getLectures() {
        synchronized (lectures) {
            return ImmutableSortedSet.copyOf(lectures);
        }
    }

    /**
     * Get the value of displayed
     *
     * @return the value of displayed
     */
    public boolean isDownloadEnabled() {
        return downloadEnabled;
    }

    /**
     * Set the value of displayed
     *
     * @param downloadEnabled new value of displayed
     */
    public void setDownloadEnabled(boolean downloadEnabled) {
        initPCS();
        boolean oldDownloadEnabled = this.downloadEnabled;
        this.downloadEnabled = downloadEnabled;
        pcs.firePropertyChange(PROP_DOWNLOAD_ENABLED, oldDownloadEnabled, downloadEnabled);
    }

    public String getCourseID() {
        return courseID;
    }

    public SortedSet<Stream> getStreams() {
        synchronized (streams) {
            return ImmutableSortedSet.copyOf(streams);
        }
    }

    public Stream getStream(String name, int i) {
        return getStream(name, i, true);
    }

    public Stream getStream(String name, int i, boolean actualStream) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty. THIS IS A BUG. FIX IT.");
        }
        synchronized (streams) {
            for (Stream stream : streams) {
                if (stream.getName().equals(name) && stream.getNumber() == i && stream.isActualStream() == actualStream) {
                    return stream;
                }
            }
            Stream s = new Stream(name, i, actualStream, lectures);
            if (!streams.add(s)) {
                throw ExceptionReporter.reportException(new IllegalStateException("The streams list is inconsistent.\n"
                        + "The Stream [" + s + "] is already in the list\n\t[" + streams + "]"));
            }
            return s;
        }
    }

    public void setDownloadTypeEnabled(DownloadType dt, boolean en) {
        synchronized (lectures) {
            for (Lecture lecture : lectures) {
                lecture.setDownloadEnabled(dt, en);
            }
        }
    }

    public void addLecture(Lecture l) {
        initPCS();
        synchronized (lectures) {
            int len = lectures.size();
            if (l != null && !lectures.contains(l)) {
                l.addPropertyChangeListener(this);
                for (DownloadType dt : DownloadType.values()) {
                    l.setDownloadEnabled(dt, isDownloadTypeEnabled(dt));
                }
                for (Stream stream : l.getStreams()) {
                    boolean enable = true;
                    for (Lecture lecture : stream.getLectures()) {
                        if (!lecture.equals(l)) {
                            enable &= lecture.isEnabled();
                        }
                    }
                    if (enable) {
                        l.setEnabled(true);
                    }
                }
                if (!lectures.add(l)) {
                    System.out.println("Readding2 lecture. FAIL: " + l);
                }

                pcs.firePropertyChange(PROP_ADDED_LECTURE, null, l);
            } else {
                System.out.println("Readding lecture. FAIL: " + l);
            }
            if (len == lectures.size()) {
                System.out.println("LECTURE SIZE HASN'T CHANGED - SEE OUTPUT ABOVE FOR REASON. FAIL: " + l);
            }
        }
    }

    public boolean isDownloadTypeEnabled(DownloadType dt) {
        if (lectures.isEmpty()) {
            return dt.getDefault();
        }
        synchronized (lectures) {
            for (Lecture lecture : lectures) {
                if (!lecture.isDownloadEnabled(dt)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clearLectures() {
        initPCS();
        synchronized (lectures) {
            lectures.clear();
        }
        synchronized (lectures) {
            streams.clear();
        }
        pcs.firePropertyChange(PROP_CLEARED_LECTURES, null, null);
    }

    public PlaylistFormatter getPlaylistFormatter() {
        return plFormatter;
    }

    public TagFormatter getTagFormatter() {
        return tagFormatter;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        initPCS();
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        initPCS();
        pcs.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        initPCS();
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        initPCS();
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        initPCS();
        pcs.firePropertyChange(evt);
    }

    private void initPCS_() {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);
            synchronized (lectures) {
                for (Lecture lecture : lectures) {
                    lecture.addPropertyChangeListener(this);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Subject{" + "bbid=" + bbid + ", name=" + name + ", semesterCode=" + semesterCode + ", DL enabled=" + downloadEnabled + ", courseID=" + courseID + ", lectures=" + lectures + ", streams=" + streams + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.bbid != null ? this.bbid.hashCode() : 0);
        hash = 97 * hash + (this.courseID != null ? this.courseID.hashCode() : 0);
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
        final Subject other = (Subject) obj;
        if ((this.bbid == null) ? (other.getBlackboardId() != null) : !this.bbid.equals(other.getBlackboardId())) {
            return false;
        }
        if ((this.courseID == null) ? (other.getCourseID() != null) : !this.courseID.equals(other.getCourseID())) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Subject o) {
        int semCmp = o.getSemesterCode() - this.getSemesterCode();
        // the `larger` subject is the one whose semester is later(larger).
        return (semCmp == 0 ? this.getName().compareToIgnoreCase(o.getName()) : semCmp);
    }
}
