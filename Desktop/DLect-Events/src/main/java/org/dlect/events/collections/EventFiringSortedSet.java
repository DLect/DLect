/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.ForwardingSortedSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;

public class EventFiringSortedSet<E> extends ForwardingSortedSet<E> {
    
    private final SortedSet<E> delegate;
    private final CollectionEventHelper<E> helper;

    /**
     * Creates a new set with the ability to fire events through the given helper.
     *
     * @param delegate The base set to delegate method calls to.
     * @param helper   The event helper to fire events through.
     */
    public EventFiringSortedSet(SortedSet<E> delegate, CollectionEventHelper<E> helper) {
        this.delegate = delegate;
        this.helper = helper;
    }
    
    @Override
    protected SortedSet<E> delegate() {
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
    
    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return new EventFiringSortedSet<>(super.tailSet(fromElement), helper);
    }
    
    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return new EventFiringSortedSet<>(super.subSet(fromElement, toElement), helper);
    }
    
    @Override
    public SortedSet<E> headSet(E toElement) {
        return new EventFiringSortedSet<>(super.headSet(toElement), helper);
    }
    
}
