/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.lecture;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public class UpdatingLectureStateUpdateHandler extends LectureStateUpdateHandler {

    private ImmutableSortedSet<Stream> enabledStreams;
    private ImmutableSet<DownloadType> enabledDownloadTypes;

    public UpdatingLectureStateUpdateHandler(Subject d) {
        super(d);
    }

    @Override
    protected void initImpl() {
        Builder<Stream> streamsBuilder = ImmutableSortedSet.naturalOrder();
        Builder<DownloadType> dlTypeBuilder = ImmutableSortedSet.naturalOrder();

        for (Stream stream : getSubject().getStreams()) {
            if (isStreamEnabled(stream)) {
                streamsBuilder.add(stream);
            }
        }
        for (DownloadType dt : DownloadType.values()) {
            if (isDownloadTypeEnabled(dt)) {
                dlTypeBuilder.add(dt);
            }
        }

        this.enabledStreams = streamsBuilder.build();
        this.enabledDownloadTypes = dlTypeBuilder.build();
    }

    @Override
    public void updateLecturesImpl() {
        for (Stream stream : getSubject().getStreams()) {
            if (enabledStreams.contains(stream)) {
                setStreamEnabled(stream, true);
            }
        }
        for (DownloadType dt : enabledDownloadTypes) {
            setDownloadTypeEnabled(dt, true);
        }
    }

}
