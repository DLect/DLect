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
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;

public class StreamMenuPrefixUpdater extends MenuPrefixUpdater {

    private final Stream st;
    private final List<Pair<Lecture, DownloadType>> lecturesDownloading = new ArrayList<Pair<Lecture, DownloadType>>();

    public StreamMenuPrefixUpdater(MenuItem m, String defaultLabel, Subject s, Stream st) {
        super(m, defaultLabel, s);
        this.st = st;
    }

    @Override
    public void downloadStarting(Subject subject, Lecture l, DownloadType t) {
        if (confirmValues(subject, l)) {
            lecturesDownloading.add(new ImmutablePair<Lecture, DownloadType>(l, t));
            appendDownloadPrefix();
        }
    }

    @Override
    public void downloadCompleted(Subject subject, Lecture l, DownloadType t) {
        if (confirmValues(subject, l)) {
            lecturesDownloading.remove(new ImmutablePair<Lecture, DownloadType>(l, t));
            if (lecturesDownloading.isEmpty()) {
                resetMenuLabel();
            }
        }
    }

    private boolean confirmValues(Subject subject, Lecture l) {
        if (!subject.equals(getSubject())) {
            return false;
        } else if (st == null) {
            for (Stream stream : l.getStreams()) {
                if (!stream.isActualStream()) {
                    return true;
                }
            }
            return false;
        } else {
            return l.getStreams().contains(st);
        }
    }
}
