/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.immutable.model;

import java.net.URI;
import java.util.Objects;
import org.dlect.model.LectureDownload;

/**
 *
 * @author lee
 */
public class ImmutableLectureDownload {

    private final URI downloadURL;
    private final String downloadExtension;
    private final boolean downloaded;
    private final boolean downloadEnabled;

    public ImmutableLectureDownload(URI downloadURL, String downloadExtension, boolean downloaded, boolean downloadEnabled) {
        this.downloadURL = downloadURL;
        this.downloadExtension = downloadExtension;
        this.downloaded = downloaded;
        this.downloadEnabled = downloadEnabled;
    }

    public ImmutableLectureDownload(URI downloadURL, String downloadExtension) {
        this.downloadURL = downloadURL;
        this.downloadExtension = downloadExtension;
        this.downloaded = false;
        this.downloadEnabled = false;
    }

    public URI getDownloadURL() {
        return downloadURL;
    }

    public String getDownloadExtension() {
        return downloadExtension;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public boolean isDownloadEnabled() {
        return downloadEnabled;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.downloadURL);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImmutableLectureDownload other = (ImmutableLectureDownload) obj;
        return Objects.equals(this.getDownloadURL(), other.getDownloadURL());
    }

    @Override
    public String toString() {
        return "ImmutableLectureDownload{" + "downloadURL=" + downloadURL + ", downloadExtension=" + downloadExtension + ", downloaded=" + downloaded + ", downloadEnabled=" + downloadEnabled + '}';
    }

    public static ImmutableLectureDownload from(LectureDownload value) {
        return new ImmutableLectureDownload(value.getDownloadURL(), value.getDownloadExtension(), value.isDownloaded(), value.isDownloadEnabled());
    }

}
