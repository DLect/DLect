/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import com.google.common.base.Objects;
import java.util.List;

/**
 *
 * @author lee
 * @param <T>
 */
public class Listenable<T extends Listenable<T>> {

    
    /**
     * TODO consider making this more fluent.
     * if old == new then fire will do nothing.
     *
     * Event e = changeEvent(EVENT_NAME).old(object).new(newObject);
     * object = newObject
     * e.fire();
     *
     * @param <O>
     * @param name
     * @param oldObj
     * @param newObj
     */
    protected <O> void fireChangeEvent(String name, O oldObj, O newObj) {
        if (Objects.equal(oldObj, newObj)) {
            return;
        }
        if (name == null) {
            throw new IllegalArgumentException("Null Event Name");
        }

        DataEvent e = new DataEvent(this, name, oldObj, newObj);
        // TODO include  re-entrant read/write lock.

    }

    /**
     *
     * @param <T>
     * @param name
     * @param eventType
     * @param object    The object added or removed before this event fired.
     */
    protected <T> void fireListChangeEvent(String name, DataListEvent.Type eventType, T object) {

    }
    
    /**
     *
     * @param <E>
     * @param listName
     * @param list
     *
     * @return
     */
    protected <E> List<E> wrapList(String listName, List<E> list) {
        // TODO wrap this so that the application doesn't need to handle these events. E.G. custom implementation of
        //  add/remove so that it calls `fireListChangeEvent` for the changes.
        return list;
    }

    /**
     *
     * @param <E>
     * @param listName
     * @param list
     *
     * @return
     */
    protected <E> List<E> wrapListenableList(String listName, List<E> list) {
        // TODO wrap this so that the application doesn't need to handle these events. E.G. custom implementation of
        //  add/remove so that it calls `fireListChangeEvent` for the changes.
        // This method will attach this listenable to new listenables.
        return list;
    }
}
