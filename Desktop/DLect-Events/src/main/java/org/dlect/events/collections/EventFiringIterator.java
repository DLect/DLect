/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.ForwardingIterator;
import java.util.Iterator;

/**
 *
 * @author lee
 */
public class EventFiringIterator<E> extends ForwardingIterator<E> {

    private final Iterator<E> delegate;
    private final CollectionEventHelper helper;

    public EventFiringIterator(Iterator<E> delegate, CollectionEventHelper helper) {
        this.delegate = delegate;
        this.helper = helper;
    }

    @Override
    protected Iterator<E> delegate() {
        return delegate;
    }

    @Override
    public void remove() {
        super.remove(); //To change body of generated methods, choose Tools | Templates.
        helper.fireRemove(delegate);
    }

    
}
