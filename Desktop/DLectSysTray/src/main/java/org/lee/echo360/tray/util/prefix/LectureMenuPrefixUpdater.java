/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray.util.prefix;

import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.List;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;

public class LectureMenuPrefixUpdater extends MenuPrefixUpdater {

    List<DownloadType> downloadTypesDownloading = new ArrayList<DownloadType>();
    private final Lecture l;

    public LectureMenuPrefixUpdater(MenuItem m, String defaultLabel, Subject s, Lecture l) {
        super(m, defaultLabel, s);
        this.l = l;
    }

    @Override
    public void downloadStarting(Subject subject, Lecture lecture, DownloadType t) {
        if (subject.equals(getSubject()) && lecture.equals(l)) {
            downloadTypesDownloading.add(t);
            appendDownloadPrefix();
        }
    }

    @Override
    public void downloadCompleted(Subject subject, Lecture lecture, DownloadType t) {
        if (subject.equals(getSubject()) && lecture.equals(l)) {
            downloadTypesDownloading.remove(t);
            getMenuItem().setEnabled(true);
            if (downloadTypesDownloading.isEmpty()) {
                resetMenuLabel();
            }
        }
    }
}
