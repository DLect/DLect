/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper;

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
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

    private final Table<Subject, Lecture, DownloadType> currentDownloads = HashBasedTable.create(10, 40);

    public ControllerStateHelper(MainController mc) {
        // TODO add Download Controller to this list.
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

    public ImmutableMap<Lecture, DownloadType> getDownloadingIn(Subject sub) {
        return ImmutableMap.copyOf(currentDownloads.row(sub));
    }

    public boolean isDownloading(Subject sub) {
        return !currentDownloads.row(sub).isEmpty();
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
                    currentDownloads.put(s, l, dt);
                    break;
                case COMPLETED:
                case FAILED:
                    currentDownloads.remove(de, l);
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

        @Override
        public boolean isUniqueId() {
            return false;
        }

    }

}
