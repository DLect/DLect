/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.wrapper;

import java.lang.ref.WeakReference;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventListener;

public class WeakEventListenerWrapper extends EventListenerWrapper {

    private final WeakReference<EventListener> listener;

    public WeakEventListenerWrapper(EventAdapter addedTo, EventListener listener) {
        super(addedTo);
        this.listener = new WeakReference<>(listener);
    }

    @Override
    public EventListener getWrappedListener() {
        return listener.get();
    }

}
