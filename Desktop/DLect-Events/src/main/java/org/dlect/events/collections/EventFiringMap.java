/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.base.Function;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
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
        // TODO verify that changing entrys in this fires events.
        return new StandardEntrySet() {

            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new EventFiringIterator<>(
                        Iterators.transform(delegate().entrySet().iterator(),
                                            new EventFiringEntryTransform<>(helper)),
                        helper);
            }

        };
    }

    @Override
    public Set<K> keySet() {
        return new StandardKeySet();
    }

    @Override
    public Collection<V> values() {
        return new StandardValues();
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

    private static class EventFiringEntryTransform<K, V> implements Function<Entry<K, V>, Entry<K, V>> {

        private final CollectionEventHelper<Entry<K, V>> helper;

        public EventFiringEntryTransform(CollectionEventHelper<Entry<K, V>> helper) {
            this.helper = helper;
        }

        @Override
        public Entry<K, V> apply(final Entry<K, V> input) {
            return new EventFiringEntry<>(input, helper);
        }

    }

    private static class EventFiringEntry<K, V> extends ForwardingMapEntry<K, V> {

        private final CollectionEventHelper<Entry<K, V>> helper;
        private final Entry<K, V> input;

        public EventFiringEntry(Entry<K, V> input, CollectionEventHelper<Entry<K, V>> helper) {
            this.input = input;
            this.helper = helper;
        }

        @Override
        protected Entry<K, V> delegate() {
            return input;
        }

        @Override
        public V setValue(V value) {
            V old = super.setValue(value);
            helper.fireReplace(Maps.immutableEntry(getKey(), old), Maps.immutableEntry(getKey(), value));
            return old;
        }
    }

}
