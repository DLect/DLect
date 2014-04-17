/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import com.google.common.base.Objects;
import org.dlect.controller.MainController;
import org.dlect.controller.event.ControllerListenable;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public class SubjectDataHelper extends ControllerListenable<SubjectDataHelper> implements EventListener {

//    private final Multiset<Subject> downloadedCount = HashMultiset.create(10);
//    private final Multiset<Stream> streamsLectureCount = HashMultiset.create(10);
//    private final Multiset<SubjectDownloadTypePair> subjectDownloadTypeCount = HashMultiset.create(10);
//
//    private final LoadingCache<Event, SubjectInformation> subjectInformationCache = CacheBuilder.newBuilder().maximumSize(10).build(new CacheLoader<Event, SubjectInformation>() {
//
//        @Override
//        public SubjectInformation load(Event key) throws Exception {
//            return new SubjectInformation();
//        }
//    });
    public SubjectDataHelper(MainController mc) {
        //mc.addListener(this, Database.class, Semester.class, Subject.class, Lecture.class, LectureDownload.class);
    }

    public SubjectInformation getSubjectInformation(Event e) {
        return new SubjectInformation();
    }

    @Override
    public void init() {
    }

    @Override
    public void processEvent(Event e) {
        // TODO(Later) make this entire file work better.
//        if(e.getEventID().equals(LectureDownloadEventID.DOWNLOADED)) {
//            if(Boolean.TRUE.equals(e.getBefore())) {
//                
//            }
//        }
    }

    private static class SubjectDownloadTypePair {

        private final Subject subject;
        private final DownloadType dt;

        public SubjectDownloadTypePair(Subject subject, DownloadType dt) {
            this.subject = subject;
            this.dt = dt;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.subject, this.dt);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SubjectDownloadTypePair other = (SubjectDownloadTypePair) obj;
            return Objects.equal(this.subject, other.subject) && Objects.equal(this.dt, other.dt);
        }

    }

    public static enum DownloadState {

        ALL_DOWNLOADED, NONE_SELECTED, NOT_ALL_DOWNLOADED;
    }
}
