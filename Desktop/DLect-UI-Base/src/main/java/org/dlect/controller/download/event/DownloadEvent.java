/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.download.event;

import javax.annotation.Nonnull;
import org.dlect.events.Event;

import static org.dlect.helper.Conditions.*;

/**
 *
 * @author lee
 */
public class DownloadEvent extends Event {

    private final DownloadParameter parameter;
    private final DownloadState before;
    private final DownloadState after;
    private final DownloadStatus eventID;

    public DownloadEvent(@Nonnull Object source, @Nonnull DownloadStatus state, @Nonnull DownloadParameter downloadParam, DownloadState before, DownloadState after) {
        super(source, state, before, after);
        checkNonNull(state, "Download State");
        checkNonNull(downloadParam, "Download Parameters");
        switch (state) {
            case COMPLETED:
            case FAILED:
                checkNonNull(before, "Before");
                checkNull(after, "After");
                break;
            case PROGRESS:
                checkNonNull(before, "Before");
                checkNonNull(after, "After");
                break;
            case STARTING:
                checkNull(before, "Before");
                checkNonNull(after, "After");
                break;
            default:
                throw new AssertionError();
        }
        this.parameter = downloadParam;
        this.before = before;
        this.after = after;
        this.eventID = state;
    }

    @Override
    public DownloadState getAfter() {
        return after;
    }

    @Nonnull
    public DownloadParameter getParameter() {
        return parameter;
    }

    @Override
    public DownloadState getBefore() {
        return before;
    }

    @Override
    public DownloadStatus getEventID() {
        return eventID;
    }

}
