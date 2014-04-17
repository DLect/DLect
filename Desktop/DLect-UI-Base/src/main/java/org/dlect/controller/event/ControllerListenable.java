/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.event;

import org.dlect.controller.download.event.DownloadParameter;
import org.dlect.controller.helper.Controller;
import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.listenable.Listenable;
import org.dlect.model.Lecture;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 * @param <T>
 */
public abstract class ControllerListenable<T extends ControllerListenable<T>> extends Listenable<T> implements Controller {

    protected ControllerEventBuilder event(ControllerType eid) {
        return new ControllerEventBuilder(this, eid, getAdapter());
    }

    public static class ControllerEventBuilder {

        private final EventAdapter adapter;
        private final ControllerType eid;
        private final Object source;
        private ControllerState state;
        private Object param;

        public ControllerEventBuilder(Object source, ControllerType eid, EventAdapter adapter) {
            this.source = source;
            this.eid = eid;
            this.adapter = adapter;
        }

        public ControllerEventBuilder parameter(Subject s) {
            param = s;
            return this;
        }

        public ControllerEventBuilder parameter(Subject s, Lecture l, DownloadType dt) {
            param = new DownloadParameter(s, l, dt);
            return this;
        }

        public ControllerEventBuilder state(ControllerState s) {
            this.state = s;
            return this;
        }

        public void fire() {
            if (state == null) {
                throw new IllegalStateException("Before or after has not been set.");
            }
            Event fire = new ControllerEvent(source, eid, param, state);
            adapter.fireEvent(fire);
        }

    }

}
