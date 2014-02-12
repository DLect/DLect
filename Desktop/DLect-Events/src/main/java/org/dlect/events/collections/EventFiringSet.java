/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.ForwardingSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class EventFiringSet<E> extends ForwardingSet<E> {

    private final Set<E> delegate;
    private final CollectionEventHelper<E> helper;

    public EventFiringSet(Set<E> delegate, CollectionEventHelper<E> helper) {
        this.delegate = delegate;
        this.helper = helper;
    }

    @Override
    protected Set<E> delegate() {
        return delegate;
    }

    protected CollectionEventHelper<E> getHelper() {
        return helper;
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
    public boolean addAll(Collection<? extends E> collection) {
        return super.standardAddAll(collection);
    }

    @Override
    public void clear() {
        this.standardClear();
    }

    @Override
    public Iterator<E> iterator() {
        return new EventFiringIterator<>(super.iterator(), helper);
    }

    @Override
    public boolean remove(Object object) {
        return super.standardRemove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return super.standardRemoveAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return super.standardRetainAll(collection);
    }

}
