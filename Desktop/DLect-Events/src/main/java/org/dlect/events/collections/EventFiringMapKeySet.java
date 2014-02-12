/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class EventFiringMapKeySet<K, V> implements Set<K> {

    private final EventFiringMap<K, V> map;

    public EventFiringMapKeySet(EventFiringMap<K, V> map) {
        this.map = map;
    }

    @Override
    public boolean add(K element) {
        throw new UnsupportedOperationException("Can't Add a value from a maps key set");
    }

    @Override
    public boolean addAll(Collection<? extends K> c) {
        throw new UnsupportedOperationException("Can't Add a value from a maps key set");
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Iterator<K> iterator() {
        return Iterators.transform(map.entrySet().iterator(), new Function<Entry<K, V>, K>() {
            @Override
            public K apply(Entry<K, V> input) {
                return input.getKey();
            }
        });
    }

    @Override
    public boolean remove(Object o) {
        boolean contains = map.containsKey(o);
        map.remove(o);
        return contains;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= this.remove(o);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return Iterators.retainAll(this.iterator(), c);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Object[] toArray() {
        return map.delegate().keySet().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return map.delegate().keySet().toArray(a);
    }

}
