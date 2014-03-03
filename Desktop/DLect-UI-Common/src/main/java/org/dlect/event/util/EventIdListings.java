/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.event.util;

import com.google.common.collect.ImmutableSet;
import org.dlect.controller.helper.ControllerStateHelper.ControllerStateHelperEventID;
import org.dlect.events.EventID;
import org.dlect.model.Lecture.LectureEventID;
import org.dlect.model.LectureDownload.LectureDownloadEventID;
import org.dlect.model.Stream.StreamEventID;
import org.dlect.model.Subject.SubjectEventID;

/**
 *
 * @author lee
 */
public class EventIdListings {

    public static final ImmutableSet<EventID> DOWNLOAD_UPDATE_EVENT_IDS = ImmutableSet
            .<EventID>builder()
            .add(LectureDownloadEventID.DOWNLOADED, LectureDownloadEventID.DOWNLOAD_ENABLED)
            .add(LectureEventID.ENABLED, LectureEventID.LECTURE_DOWNLOAD)
            .add(SubjectEventID.LECTURE)
            .add(ControllerStateHelperEventID.values())
            .build();
    
    public static final ImmutableSet<EventID> DOWNLOAD_TYPE_UPDATE_EVENT_IDS = ImmutableSet
            .<EventID>builder()
            .add(LectureDownloadEventID.DOWNLOADED, LectureDownloadEventID.DOWNLOAD_ENABLED)
            .add(LectureEventID.LECTURE_DOWNLOAD)
            .add(SubjectEventID.LECTURE)
            .build();
    
    public static final ImmutableSet<EventID> LECTURE_STREAM_UPDATE_EVENT_IDS = ImmutableSet
            .<EventID>builder()
            .add(StreamEventID.NAME)
            .add(LectureEventID.STREAM)
            .add(SubjectEventID.LECTURE, SubjectEventID.STREAM)
            .add(ControllerStateHelperEventID.DOWNLOAD)
            .build();

}
