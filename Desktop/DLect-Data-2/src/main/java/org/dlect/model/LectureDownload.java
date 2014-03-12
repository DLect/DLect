/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import java.net.URI;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import org.dlect.events.EventID;
import org.dlect.events.listenable.EventBuilder;
import org.dlect.model.helper.XmlListenable;

/**
 *
 * @author lee
 */
public class LectureDownload extends XmlListenable<LectureDownload> {

    @XmlElement(name = "url")
    private URI downloadURL;
    @XmlElement(name = "extension")
    private String downloadExtension;
    @XmlElement(name = "downloaded")
    private boolean downloaded;
    @XmlElement(name = "enabled")
    private boolean downloadEnabled;

    public LectureDownload() {
    }

    public LectureDownload(URI downloadURL, String downloadExtension, boolean downloaded, boolean downloadEnabled) {
        this.downloadURL = downloadURL;
        this.downloadExtension = downloadExtension;
        this.downloaded = downloaded;
        this.downloadEnabled = downloadEnabled;
    }

    public URI getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(URI downloadURL) {
        EventBuilder<URI> evt = event(LectureDownloadEventID.DOWNLOAD_URL).before(this.downloadURL);
        this.downloadURL = downloadURL;
        evt.after(this.downloadURL).fire();
    }

    public String getDownloadExtension() {
        return downloadExtension;
    }

    public void setDownloadExtension(String downloadExtension) {
        EventBuilder<String> evt = event(LectureDownloadEventID.DOWNLOAD_EXTENSION).before(this.downloadExtension);
        this.downloadExtension = downloadExtension;
        evt.after(this.downloadExtension).fire();
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        EventBuilder<Boolean> evt = event(LectureDownloadEventID.DOWNLOADED).before(this.downloaded);
        this.downloaded = downloaded;
        evt.after(this.downloaded).fire();
    }

    public boolean isDownloadEnabled() {
        return downloadEnabled;
    }

    public void setDownloadEnabled(boolean downloadEnabled) {
        EventBuilder<Boolean> evt = event(LectureDownloadEventID.DOWNLOAD_ENABLED).before(this.downloadEnabled);
        this.downloadEnabled = downloadEnabled;
        evt.after(this.downloadEnabled).fire();
    }

    @Override
    public String toString() {
        return "LectureDownload{" + "downloadURL=" + downloadURL + ", downloadExtension=" + downloadExtension + ", downloaded=" + downloaded + ", downloadEnabled=" + downloadEnabled + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.downloadURL);
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
        final LectureDownload other = (LectureDownload) obj;
        if (!Objects.equals(this.downloadURL, other.downloadURL)) {
            return false;
        }
        return true;
    }

    public static enum LectureDownloadEventID implements EventID {

        DOWNLOAD_URL, DOWNLOAD_EXTENSION, DOWNLOADED, DOWNLOAD_ENABLED;

        @Override
        public Class<?> getAppliedClass() {
            return LectureDownload.class;
        }

        @Override
        public String getName() {
            return name();
        }

    }

}
