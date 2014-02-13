/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ForwardingIterator;
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
                return new EventFiringEntrySetIterator<>(delegate().entrySet().iterator(),
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
        if (replace && !Objects.equal(old, value)) { // Replacing and value has changed.
            helper.fireReplace(imEntry(key, old), imEntry(key, value));
        } else if (!replace) {
            helper.fireAdd(imEntry(key, value));
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
            if (!Objects.equal(old, value)) { // Changed
                helper.fireReplace(imEntry(getKey(), old), imEntry(getKey(), value));
            }
            return old;
        }
    }
    
    private static class EventFiringEntrySetIterator<K, V> extends ForwardingIterator<Entry<K, V>> {
        
        private final Iterator<Entry<K, V>> delegate;
        private final CollectionEventHelper<Entry<K, V>> helper;
        
        private Entry<K, V> current;
        
        public EventFiringEntrySetIterator(Iterator<Entry<K, V>> delegate, CollectionEventHelper<Entry<K, V>> helper) {
            this.delegate = delegate;
            this.helper = helper;
        }
        
        @Override
        protected Iterator<Entry<K, V>> delegate() {
            return delegate;
        }
        
        @Override
        public Entry<K, V> next() {
            current = super.next();
            return new EventFiringEntry<>(current, helper);
        }
        
        @Override
        public void remove() {
            super.remove();
            helper.fireRemove(imEntry(current));
        }
    }
    
    protected static <K0, V0> Entry<K0, V0> imEntry(K0 key, V0 value) {
        return Maps.immutableEntry(key, value);
    }
    
    protected static <K0, V0> Entry<K0, V0> imEntry(Entry<K0, V0> entry) {
        return Maps.immutableEntry(entry.getKey(), entry.getValue());
    }
    
}
