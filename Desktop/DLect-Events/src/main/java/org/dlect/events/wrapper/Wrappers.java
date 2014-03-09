/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.wrapper;

import org.dlect.events.EventAdapter;
import org.dlect.events.EventListener;
import org.dlect.events.listenable.Listenable;
import org.dlect.events.listenable.ListenableEventAdapterWrapper;

/**
 *
 * @author lee
 */
public class Wrappers {

    public static void addSwingListenerTo(EventListener el, Listenable<?> l, Class<?>... on) {
        addSwingListenerTo(el, new ListenableEventAdapterWrapper(l), on);
    }

    public static void addSwingListenerTo(EventListener el, EventAdapter ea, Class<?>... on) {
        ea.addListener(new SwingEventListenerWrapper(ea, el), on);
    }

    public static void removeSwingListenerFrom(EventListener el, Listenable<?> l) {
        removeSwingListenerFrom(el, new ListenableEventAdapterWrapper(l));
    }

    public static void removeSwingListenerFrom(EventListener el, EventAdapter ea) {
        // TODO Implement removing a swing listener.
    }

}
