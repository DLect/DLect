/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.wrapper;

import java.util.List;
import javax.swing.SwingWorker;
import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventListener;

public class SwingEventListenerWrapper extends WeakEventListenerWrapper {

    private static final SwingWorkerEventProcessor processor = new SwingWorkerEventProcessor();

    public SwingEventListenerWrapper(EventAdapter addedTo, EventListener listener) {
        super(addedTo, listener);
    }

    @Override
    public void processEvent(Event e) {
        if (checkListener()) {
            processor.push(new EventProcess(e, this));
        }
    }

    private static class EventProcess {

        private final Event e;
        private final SwingEventListenerWrapper w;

        public EventProcess(Event e, SwingEventListenerWrapper w) {
            this.e = e;
            this.w = w;
        }

    }

    private static class SwingWorkerEventProcessor extends SwingWorker<Void, EventProcess> {

        public SwingWorkerEventProcessor() {
        }

        @Override
        protected Void doInBackground() throws Exception {
            return null;
        }

        @Override
        protected void process(List<EventProcess> chunks) {
            for (EventProcess eventProcess : chunks) {
                eventProcess.w.processEventImpl(eventProcess.e);
            }
        }

        protected void push(EventProcess e) {
            publish(e);
        }

    }

}
