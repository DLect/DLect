/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class EventFiringMapValueSet<K, V> implements Set<V> {

    private final EventFiringMap<K, V> map;

    public EventFiringMapValueSet(EventFiringMap<K, V> map) {
        this.map = map;
    }

    @Override
    public boolean add(V element) {
        throw new UnsupportedOperationException("Can't Add a value from a maps key set");
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        throw new UnsupportedOperationException("Can't Add a value from a maps key set");
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsValue(o);
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
    public Iterator<V> iterator() {
        return Iterators.transform(map.entrySet().iterator(), new Function<Entry<K, V>, V>() {
            @Override
            public V apply(Entry<K, V> input) {
                return input.getValue();
            }
        });
    }

    @Override
    public boolean remove(Object o) {
        return Iterators.removeIf(this.iterator(), Predicates.equalTo(o));
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return Iterators.removeAll(this.iterator(), c);
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
        return map.delegate().values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return map.delegate().values().toArray(a);
    }

}
