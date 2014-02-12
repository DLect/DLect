/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EventFiringMap<K, V> extends ForwardingMap<K, V> {

    private final Map<K, V> delegate;
    private final CollectionEventHelper<Map.Entry<K, V>> helper;

    public EventFiringMap(Map<K, V> delegate, CollectionEventHelper<Map.Entry<K, V>> helper) {
        this.delegate = delegate;
        this.helper = helper;
    }

    @Override
    protected Map<K, V> delegate() {
        return delegate;
    }

    @Override
    public void clear() {
        super.standardClear();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EventFiringMapEntrySet<>(super.entrySet(), helper);
    }

    @Override
    public Set<K> keySet() {
        return new EventFiringMapKeySet<>(this);
    }

    @Override
    public Collection<V> values() {
        return new EventFiringMapValueSet<>(this);
    }

    @Override
    public V put(K key, V value) {
        boolean replace = this.containsKey(key);
        V old = super.put(key, value);
        if (replace) {
            helper.fireReplace(Maps.immutableEntry(key, old), Maps.immutableEntry(key, value));
        } else {
            helper.fireAdd(Maps.immutableEntry(key, value));
        }
        return old;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        super.standardPutAll(map);
    }

    @Override
    public V remove(Object key) {
        return super.standardRemove(key);
    }

    private static class EventFiringMapEntrySet<K, V> extends EventFiringSet<Entry<K, V>> {

        public EventFiringMapEntrySet(Set<Entry<K, V>> delegate, CollectionEventHelper<Entry<K, V>> helper) {
            super(delegate, helper);
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return Iterators.transform(super.iterator(), new EventFiringEntryFunction<>(getHelper()));
        }

    }

}
