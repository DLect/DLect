/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.ForwardingListIterator;
import java.util.ListIterator;

/**
 *
 * @param <E>
 */
public class EventFiringListIterator<E> extends ForwardingListIterator<E> {

    private final ListIterator<E> delegate;
    private final CollectionEventHelper<E> helper;

    private E current;
   
    // TODO test that current is correctly initilised on start.
    
    public EventFiringListIterator(ListIterator<E> delegate, CollectionEventHelper<E> helper) {
        this.delegate = delegate;
        this.helper = helper;
    }

    @Override
    protected ListIterator<E> delegate() {
        return delegate;
    }

    @Override
    public void add(E element) {
        super.add(element);
        helper.fireAdd(element);
    }

    @Override
    public void remove() {
        super.remove(); 
        helper.fireRemove(current);
    }

    @Override
    public void set(E element) {
        super.set(element);
        helper.fireReplace(current, element);
    }

    @Override
    public E next() {
        current = super.next();
        return current;
    }

    @Override
    public E previous() {
        current =  super.previous(); 
        return current;
    }
    
    

}
