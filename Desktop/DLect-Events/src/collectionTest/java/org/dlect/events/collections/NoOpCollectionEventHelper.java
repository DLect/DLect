/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventID;
import org.dlect.events.EventListener;

/**
 *
 * @author lee
 */
class NoOpCollectionEventHelper<T> extends CollectionEventHelper<T> {

    public NoOpCollectionEventHelper() {
        super(new Object(), NoOpEventID.ID, new NoOpAdapter());
    }

    @Override
    public void fireAdd(T addedElement) {
    }

    @Override
    public void fireRemove(T removedElement) {
    }

    @Override
    public void fireReplace(T original, T replacement) {
    }

    private static class NoOpAdapter implements EventAdapter {

        public NoOpAdapter() {
        }

        @Override
        public boolean addListener(EventListener l, Class<?>... listeningClasses) {
            return false;
        }

        @Override
        public void fireEvent(Event e) {
        }

        @Override
        public EventAdapter getParentAdapter() {
            return null;
        }

        @Override
        public void setParentAdapter(EventAdapter e) {
        }

        @Override
        public boolean removeListener(EventListener l) {
            return false;
        }
    }

    private static enum NoOpEventID implements EventID {

        ID;

        @Override
        public Class<?> getAppliedClass() {
            return Object.class;
        }

        @Override
        public String getName() {
            return name();
        }

    }

}
