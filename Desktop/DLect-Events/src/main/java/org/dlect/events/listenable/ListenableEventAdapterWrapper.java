/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.events.listenable;

import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventListener;

/**
 *
 * @author lee
 */
public class ListenableEventAdapterWrapper implements EventAdapter {

    private final Listenable<?> l;
    
    public ListenableEventAdapterWrapper(Listenable<?> l) {
        this.l = l;
    }
    
    public boolean addListener(EventListener l, Class<?>... listeningClasses) {
        return this.l.addListener(l, listeningClasses);
    }
    
    @Override
    public void fireEvent(Event e) {
        l.fireEvent(e);
    }
    
    public boolean removeListener(EventListener l) {
        return this.l.removeListener(l);
    }
    
    @Override
    public EventAdapter getParentAdapter() {
        throw new UnsupportedOperationException("Get Parent Adapter Not supported. This is only a wrapper.");
    }
    
    @Override
    public void setParentAdapter(EventAdapter e) {
        throw new UnsupportedOperationException("Set Parent Adapter Not supported. This is only a wrapper.");
    }
    
}
