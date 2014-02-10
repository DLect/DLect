/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.events;

import org.dlect.events.collections.EventFiringList;
import java.util.List;
import javax.annotation.Nonnull;
import org.dlect.events.collections.CollectionEventHelper;

/**
 *
 * @author lee
 * @param <T> The class that this class should use.
 */
public class Listenable<T extends Listenable<T>>{
    
    private final EventAdapter e = EventAdapterBuilder.getNewAdapter();
    
    protected EventAdapter getAdapter() {
        return e;
    }
    
    public boolean addListener(@Nonnull EventListener l, Class<?>... listeningClasses) {
        return e.addListener(l, listeningClasses);
    }
    
    
    public boolean removeListener(EventListener l) {
        return e.removeListener(l);
    }
    
    
    protected void addChild(Listenable<?>... listenables) {
        for (Listenable<?> l : listenables) {
            l.getAdapter().setParentAdapter(this.getAdapter());
        }
    }
    
    protected <E> List<E> wrapList(List<E> list, EventID listID) {
        return new EventFiringList<>(list, new CollectionEventHelper<E>(listID, this, getAdapter()));
    }
    
    protected <E extends Listenable<E>> List<E> wrapListenableList(List<E> list, EventID listID) {
        throw new UnsupportedOperationException();
        //return new ListenableListWrapper<E>(list);
    }
    
    protected void fireEvent(Event e) {
        getAdapter().fireEvent(e);
    }
    
   
}
