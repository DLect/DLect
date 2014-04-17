/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray.util.prefix;

import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;


public class SubjectMenuPrefixUpdater extends MenuPrefixUpdater {

    private final List<Pair<Lecture, DownloadType>> lecturesDownloading = new ArrayList<Pair<Lecture, DownloadType>>();
    public SubjectMenuPrefixUpdater(MenuItem m, String defaultLabel, Subject s) {
        super(m, defaultLabel, s);
    }
    
    @Override
    public void downloadingFinished(Subject subject) {
        if (subject.equals(getSubject())) {
            resetMenuLabel();
        }
    }

    @Override
    public void downloadStarting(Subject subject, Lecture l, DownloadType t) {
        if (subject.equals(getSubject())) {
            lecturesDownloading.add(new ImmutablePair<Lecture, DownloadType>(l, t));
            appendDownloadPrefix();
        }
    }

    @Override
    public void downloadCompleted(Subject subject, Lecture l, DownloadType t) {
        if (subject.equals(getSubject())) {
            lecturesDownloading.remove(new ImmutablePair<Lecture, DownloadType>(l, t));
            if (lecturesDownloading.isEmpty()) {
                resetMenuLabel();
            }
        }
    }

}
