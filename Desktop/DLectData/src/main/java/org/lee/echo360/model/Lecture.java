/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import com.google.common.collect.ImmutableList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author lee
 */
public class Lecture implements Comparable<Lecture>, Serializable {

    static final long serialVersionUID = 0xd774666;
    public static final String PROP_VIDEO_DOWNLOAD_ENABLED = Lecture.class + "PROP_ENABLEVIDEODOWNLOAD";
    public static final String PROP_AUDIO_DOWNLOAD_ENABLED = Lecture.class + "PROP_ENABLEAUDIODOWNLOAD";
    public static final String PROP_ENABLED = Lecture.class + "PROP_ENABLED";
    public static final String PROP_AUDIO_FILES_PRESENT = Lecture.class + "audioFilesPresent";
    public static final String PROP_VIDEO_FILES_PRESENT = Lecture.class + "videoFilesPresent";
    private final URI url;
    private final String subjectCode;
    private final String contentID;
    private final Date time;
    private final List<Stream> streams;
    private final EnumMap<DownloadType, FileStore> lectureFiles;
    private AtomicBoolean enabled = new AtomicBoolean(false);
    private transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Lecture(URI url, String subjCode, String contentID, Date time, List<Stream> stream, String videoFileName, String audioFileName) {
        if (url == null || time == null || stream == null || videoFileName == null || audioFileName == null) {
            throw new IllegalArgumentException("An Argument was null");
        }
        this.url = url;
        this.subjectCode = subjCode;
        this.contentID = contentID;
        this.time = new Date(time.getTime());
        this.streams = new ArrayList<Stream>(stream);
        this.lectureFiles = new EnumMap<DownloadType, FileStore>(DownloadType.class);
        lectureFiles.put(DownloadType.VIDEO, new FileStore(videoFileName, false, false));
        lectureFiles.put(DownloadType.AUDIO, new FileStore(audioFileName, false, false));
    }

    private void initPCS() {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);
        }
    }

    public Date getTime() {
        return new Date(time.getTime());
    }

    public URI getUrl() {
        return url;
    }

    public List<Stream> getStreams() {
        synchronized (streams) {
            return ImmutableList.copyOf(streams);
        }
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getContentID() {
        return contentID;
    }

    @Override
    public int compareTo(Lecture o) {
        return this.getTime().compareTo(o.getTime());
    }

    @Override
    public String toString() {
        return "Lecture{" + "url=" + url + ", time=" + time + ", streams=" + getStreams() + ", fileSettings" + lectureFiles + ", enabled=" + enabled + "}";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.time.hashCode();
        hash = 53 * hash + this.getStreams().hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Lecture other = (Lecture) obj;
        if (!this.time.equals(other.getTime())) {
            return false;
        }
        if (!this.getStreams().equals(other.getStreams())) {
            return false;
        }
        return true;
    }

    public boolean isDownloadEnabled(DownloadType dt) {
        if (dt == null || lectureFiles.get(dt) == null) {
            return false;
        }
        final FileStore get = lectureFiles.get(dt);
        return get.downloadEnabled;
    }

    public boolean isDownloadEnabledOrPresent(DownloadType dt) {
        if (dt == null || lectureFiles.get(dt) == null) {
            return false;
        }
        final FileStore get = lectureFiles.get(dt);
        return get.exists || get.downloadEnabled;
    }

    public boolean isDownloadEnabledOrPresentAndEnabled(DownloadType dt) {
        if (dt == null || lectureFiles.get(dt) == null) {
            return false;
        }
        final FileStore get = lectureFiles.get(dt);
        return get.exists || (this.enabled.get() && get.downloadEnabled);
    }

    public boolean setDownloadEnabled(final DownloadType type, boolean downloadEnabled) {
        if (type == null) {
            return false;
        }
        FileStore f = lectureFiles.get(type);
        if (f != null) {
            boolean oldDownloadEnabled = f.downloadEnabled;
            f.downloadEnabled = downloadEnabled;
            initPCS();
            pcs.firePropertyChange(type.toString() + "_ENABLED", oldDownloadEnabled, downloadEnabled);
            return true;
        }
        return false;
    }

    public boolean isFilePresent(DownloadType downloadType) {
        if (downloadType == null || lectureFiles.get(downloadType) == null) {
            return false;
        }
        return lectureFiles.get(downloadType).exists;
    }

    public String getFileName(final DownloadType type) {
        return lectureFiles.get(type).file;
    }

    public boolean setFilePresent(final DownloadType type, boolean audioFilesPresent) {
        if (type == null) {
            return false;
        }
        FileStore f = lectureFiles.get(type);
        if (f != null) {
            boolean oldAudioFilesPresent = f.exists;
            f.exists = audioFilesPresent;
            initPCS();
            pcs.firePropertyChange(type.toString() + "_PRESENT", oldAudioFilesPresent, audioFilesPresent);
            return true;
        }
        return false;
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

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled.get();
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        boolean oldEnabled = this.enabled.get();
        this.enabled.set(enabled);
        initPCS();
        pcs.firePropertyChange(PROP_ENABLED, oldEnabled, enabled);
    }

    private static class FileStore implements Serializable {

        static final long serialVersionUID = 0x17e236;
        private String file;
        private boolean exists;
        private boolean downloadEnabled;

        public FileStore(String file, boolean exists, boolean downloadEnabled) {
            this.file = file;
            this.exists = exists;
            this.downloadEnabled = downloadEnabled;
        }

        @Override
        public String toString() {
            return "FileStore{" + "file=" + file + ", exists=" + exists + '}';
        }
    }
}
