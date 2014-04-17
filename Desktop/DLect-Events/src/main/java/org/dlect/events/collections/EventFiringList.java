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

public class EventFiringList<E> extends ForwardingList<E> {

    private final List<E> delegate;
    private final CollectionEventHelper<E> helper;

    /**
     * Creates a new list with the ability to fire events through the given helper.
     *
     * @param delegate The base list to delegate method calls to.
     * @param helper   The event helper to fire events through.
     */
    public EventFiringList(List<E> delegate, CollectionEventHelper<E> helper) {
        this.delegate = delegate;
        this.helper = helper;
    }

    protected CollectionEventHelper<E> getHelper() {
        return helper;
    }

    @Override
    protected List<E> delegate() {
        return delegate;
    }

    @Override
    public boolean add(E element) {
        if (super.add(element)) {
            helper.fireAdd(element);
            return true;
        } else {
            return false;
        }
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
        return super.standardAddAll(index, elements);
    }

    @Override
    public Iterator<E> iterator() {
        return new EventFiringIterator<>(super.iterator(), helper);
    }

    @Override
    public ListIterator<E> listIterator() {
        return super.standardListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return super.standardListIterator(index);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return this.standardRetainAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return this.standardRemoveAll(collection);
    }

    @Override
    public E remove(int index) {
        E o = super.remove(index);
        helper.fireRemove(o);
        return o;
    }

    @Override
    public boolean remove(Object object) {
        return this.standardRemove(object);
    }

    @Override
    public E set(int index, E element) {
        E old = super.set(index, element);
        helper.fireReplace(old, element);
        return old;
    }

    @Override
    public void clear() {
        while (!this.isEmpty()) {
            remove(0);
        }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new EventFiringList<>(super.subList(fromIndex, toIndex), helper);
    }

}
