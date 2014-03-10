/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.lecture;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import java.util.Comparator;
import java.util.SortedSet;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public class EmptyLectureStateUpdateHandler extends LectureStateUpdateHandler {

    private static final Comparator<Entry<Stream>> ENTRY_ORDERING = new Comparator<Entry<Stream>>() {

        @Override
        public int compare(Entry<Stream> o1, Entry<Stream> o2) {
            return Ints.compare(o1.getCount(), o2.getCount());
        }
    };

    public EmptyLectureStateUpdateHandler(Subject d) {
        super(d);
    }

    private SortedSet<Entry<Stream>> getStreamsByCount() {
        return ImmutableSortedSet
                .orderedBy(ENTRY_ORDERING)
                .addAll(getSubjectInformation().getStreamLectureCount().entrySet())
                .build();
    }

    @Override
    protected void initImpl() {
        // No op.
    }

    @Override
    public void updateLecturesImpl() {
        setDownloadTypeEnabled(DownloadType.VIDEO, true);

        SortedSet<Entry<Stream>> streamsByCount = getStreamsByCount();
        if (streamsByCount.isEmpty()) {
            return;
        }
        int maxCount = streamsByCount.last().getCount();

        for (Entry<Stream> entry : streamsByCount) {
            if (entry.getCount() >= maxCount) {
                setStreamEnabled(entry.getElement(), true);
            }
        }
    }

}
