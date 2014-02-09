/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.events;

import javax.annotation.Nonnull;

/**
 *
 * @author lee
 * @param <T> The class that this class should use.
 */
public class Listenable<T extends Listenable<T>>{
    
    private final EventAdapter e = EventAdapterBuilder.getNewAdapter();
    
    
    public boolean addListener(@Nonnull EventListener l, Class<?>... listeningClasses) {
        return e.addListener(l, listeningClasses);
    }
    
    
    public boolean removeListener(EventListener l) {
        return e.removeListener(l);
    }
    
    
    protected void addChild(Listenable<?>... listenables) {
        // TODO
    }
    
}
