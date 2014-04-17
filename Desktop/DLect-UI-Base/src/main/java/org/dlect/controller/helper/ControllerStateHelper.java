/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Table;
import java.util.Collections;
import org.dlect.controller.MainController;
import org.dlect.controller.download.event.DownloadEvent;
import org.dlect.controller.download.event.DownloadParameter;
import org.dlect.controller.download.event.DownloadStatus;
import org.dlect.controller.event.ControllerEvent;
import org.dlect.controller.event.ControllerListenable;
import org.dlect.controller.event.ControllerState;
import org.dlect.controller.event.ControllerType;
import org.dlect.events.Event;
import org.dlect.events.EventID;
import org.dlect.events.EventListener;
import org.dlect.model.Lecture;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public class ControllerStateHelper extends ControllerListenable<ControllerStateHelper> implements EventListener {

    private final Table<ControllerType, Optional<Subject>, ControllerState> currentControllerActions = HashBasedTable.create(5, 50);

    private final Multimap<Lecture, DownloadType> currentDownloads = HashMultimap.create(100, 2);

    public ControllerStateHelper(MainController mc) {
        mc.addListener(this, Controller.class);
    }

    @Override
    public void init() {
        // No Op
    }

    public boolean hasCompleted(ControllerType type) {
        return hasCompleted(type, null);
    }

    public boolean hasCompleted(ControllerType type, Subject onSubject) {
        ControllerState s = currentControllerActions.get(type, Optional.fromNullable(onSubject));
        return s == ControllerState.COMPLETED;
    }

    public ImmutableMultimap<Lecture, DownloadType> getDownloadingIn(Subject sub) {
        return ImmutableMultimap.copyOf(Multimaps.filterKeys(currentDownloads, Predicates.in(sub.getLectures())));
    }

    public boolean isDownloading(Subject sub) {
        return !Collections.disjoint(sub.getLectures(), currentDownloads.keySet());
    }

    public boolean isDownloading(Lecture l) {
        return currentDownloads.containsKey(l);
    }

    public boolean isDownloading(Lecture l, DownloadType lt) {
        return currentDownloads.containsEntry(l, lt);
    }

    @Override
    public void processEvent(Event e) {
        if (e instanceof ControllerEvent) {
            ControllerEvent ce = (ControllerEvent) e;

            ControllerState state = ce.getAfter();
            ControllerType type = ce.getEventID();

            Object parameter = ce.getParameter();

            Optional<Subject> s = Optional.absent();
            if (parameter instanceof Subject) {
                s = Optional.of((Subject) parameter);
            }

            currentControllerActions.put(type, s, state);
            fireEvent(ControllerStateHelperEventID.CONTROLLER);
        } else if (e instanceof DownloadEvent) {
            DownloadEvent de = (DownloadEvent) e;

            DownloadStatus eid = de.getEventID();
            DownloadParameter p = de.getParameter();
            Subject s = p.getSubject();
            Lecture l = p.getLecture();
            DownloadType dt = p.getDownloadType();

            switch (eid) {
                case STARTING:
                case PROGRESS:
                    currentDownloads.put(l, dt);
                    break;
                case COMPLETED:
                case FAILED:
                    currentDownloads.remove(l, dt);
                    break;
            }
            fireEvent(ControllerStateHelperEventID.DOWNLOAD);
        }
    }

    public static enum ControllerStateHelperEventID implements EventID {

        CONTROLLER, DOWNLOAD;

        @Override
        public Class<?> getAppliedClass() {
            return ControllerStateHelper.class;
        }

        @Override
        public String getName() {
            return name();
        }

    }

}
