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

    public static  void addSwingListenerTo(EventListener el, Listenable<?> l, Class<?>... on) {
        addSwingListenerTo(el, new ListenableEventAdapterWrapper(l), on);
    }

    public static  void addSwingListenerTo(EventListener el, EventAdapter a, Class<?>... on) {
        a.addListener(new SwingEventListenerWrapper(a, el), on);
    }

}
