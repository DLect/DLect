/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.ForwardingList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author lee
 */
public class EventFiringList<E> extends ForwardingList<E> {

    private final List<E> delegate;
    private final CollectionEventHelper<E> helper;

    public EventFiringList(List<E> delegate, CollectionEventHelper<E> helper) {
        this.delegate = delegate;
        this.helper = helper;
    }

    @Override
    protected List<E> delegate() {
        return delegate;
    }

    @Override
    public boolean add(E element) {
        boolean a = super.add(element);
        if (a == true) {
            helper.fireAdd(element);
        }
        return a;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        helper.fireAdd(element);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return super.standardAddAll(collection);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> elements) {
        return super.standardAddAll(index, elements); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<E> iterator() {
        return this.standardIterator(); // TODO use the EventFiringIterator or EventFiringListIterator.
    }

    @Override
    public ListIterator<E> listIterator() {
        return super.standardListIterator(); // TODO use the EventFiringListIterator
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return super.standardListIterator(index); // TODO use the EventFiringListIterator
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return super.standardRetainAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return super.standardRemoveAll(collection); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public E remove(int index) {
        E o = get(index);
        super.remove(index);
        helper.fireRemove(o);
        return o;
    }

    @Override
    public boolean remove(Object object) {
        int idx = this.indexOf(object);
        if (idx < 0) {
            return false; // Cannot be found.
        } else {
            remove(idx);
            return true;
        }
    }

    @Override
    public E set(int index, E element) {
        E old = super.set(index, element);
        helper.fireReplace(old, element);
        return old;
    }

}
