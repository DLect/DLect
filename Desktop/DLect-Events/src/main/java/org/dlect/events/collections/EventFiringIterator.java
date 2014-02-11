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
    private final CollectionEventHelper<E> helper;

    private E next;

    public EventFiringIterator(Iterator<E> delegate, CollectionEventHelper<E> helper) {
        this.delegate = delegate;
        this.helper = helper;
    }

    @Override
    protected Iterator<E> delegate() {
        return delegate;
    }

    @Override
    public E next() {
        next = super.next();
        return next;
    }

    @Override
    public void remove() {
        super.remove();
        // If remove does not error, then next must have been called.
        helper.fireRemove(next);
    }

}
