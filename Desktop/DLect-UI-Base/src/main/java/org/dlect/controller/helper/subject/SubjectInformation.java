/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import java.util.Map.Entry;
import org.dlect.controller.helper.SubjectDataHelper.DownloadState;
import org.dlect.helper.LectureHelper;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public class SubjectInformation {

    private Subject subject;

    private int downloadedCount = 0;
    private int downloadsSelected = 0;
    private int notDownloadedCount = 0;
    private Multiset<Stream> streamLectureCount;
    private Multiset<Stream> streamEnabledLectureCount;
    private Multiset<DownloadType> downloadTypeCount;
    private Multiset<DownloadType> enabledDownloadTypeCount;
    private boolean lecturesInit = false;

    public void setSubject(Subject subject) {
        if (subject == this.subject) {
            return;
        }
        this.subject = subject;
        clear();
    }

    public void clear() {
        lecturesInit = false;
        downloadedCount = -1;
        downloadsSelected = -1;
        notDownloadedCount = -1;
        streamLectureCount = null;
    }

    public void itterateOverLectures() {
        if (lecturesInit) {
            return;
        }
        int ldSel = 0;
        int dlCount = 0;
        int notDlCount = 0;
        Multiset<Stream> slc = HashMultiset.create(subject.getStreams().size());
        Multiset<Stream> selc = HashMultiset.create(subject.getStreams().size());
        Multiset<DownloadType> downloadsType = HashMultiset.create(DownloadType.values().length);
        Multiset<DownloadType> enabledDownloadTypes = HashMultiset.create(DownloadType.values().length);
        for (Lecture l : subject.getLectures()) {
            slc.addAll(l.getStreams());
            if (l.isEnabled()) {
                selc.addAll(l.getStreams());
            }
            for (Entry<DownloadType, LectureDownload> e : l.getLectureDownloads().entrySet()) {
                LectureDownload ld = e.getValue();
                downloadsType.add(e.getKey());
                if (ld.isDownloaded() || ld.isDownloadEnabled()) {
                    enabledDownloadTypes.add(e.getKey());
                }
                if (LectureHelper.isLectureDownloadEnabled(l, ld)) {
                    ldSel++;
                    if (ld.isDownloaded()) {
                        dlCount++;
                    } else {
                        notDlCount++;
                    }
                }
            }
        }
        downloadedCount = dlCount;
        downloadsSelected = ldSel;
        notDownloadedCount = notDlCount;
        streamLectureCount = ImmutableMultiset.copyOf(slc);
        streamEnabledLectureCount = ImmutableMultiset.copyOf(selc);
        downloadTypeCount = ImmutableMultiset.copyOf(downloadsType);
        enabledDownloadTypeCount = ImmutableMultiset.copyOf(enabledDownloadTypes);
        lecturesInit = true;
    }

    public int getDownloadedCount() {
        itterateOverLectures();
        return downloadedCount;
    }

    public int getDownloadsSelected() {
        itterateOverLectures();
        return downloadsSelected;
    }

    public int getNotDownloadedCount() {
        itterateOverLectures();
        return notDownloadedCount;
    }

    public ImmutableMultiset<Stream> getStreamLectureCount() {
        itterateOverLectures();
        return ImmutableMultiset.copyOf(streamLectureCount);
    }

    public ImmutableMultiset<Stream> getStreamEnabledLectureCount() {
        itterateOverLectures();
        return ImmutableMultiset.copyOf(streamEnabledLectureCount);
    }

    public boolean isStreamEnabled(Stream s) {
        final int slcC = getStreamLectureCount().count(s);
        final int selcC = getStreamEnabledLectureCount().count(s);
        System.out.println(slcC + " --> " + selcC);
        return slcC == selcC;
    }

    public ImmutableMultiset<DownloadType> getDownloadTypeCount() {
        itterateOverLectures();
        return ImmutableMultiset.copyOf(downloadTypeCount);
    }

    public ImmutableMultiset<DownloadType> getEnabledDownloadTypeCount() {
        itterateOverLectures();
        return ImmutableMultiset.copyOf(enabledDownloadTypeCount);
    }

    public boolean isDownloadTypeEnabled(DownloadType dt) {
        return getDownloadTypeCount().count(dt) == getEnabledDownloadTypeCount().count(dt);
    }

    public DownloadState getDownloadedStatus() {
        itterateOverLectures();
        int notDlCount = getNotDownloadedCount();
        int dlSelected = getDownloadsSelected();
        return getDownloadedStatusFromCounts(notDlCount, dlSelected);
    }

    public static DownloadState getDownloadedStatusFromCounts(int notDlCount, int dlSelected) {
        if (dlSelected == 0) {
            return DownloadState.NONE_SELECTED;
        } else if (notDlCount == 0) {
            return DownloadState.ALL_DOWNLOADED;
        } else {
            return DownloadState.NOT_ALL_DOWNLOADED;
        }
    }

}
