/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.base.Function;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.Maps;
import java.util.Map.Entry;

public class EventFiringEntryFunction<K, V> implements Function<Entry<K, V>, Entry<K, V>> {

    private final CollectionEventHelper<Entry<K, V>> helper;

    public EventFiringEntryFunction(CollectionEventHelper<Entry<K, V>> helper) {
        this.helper = helper;
    }

    @Override
    public Entry<K, V> apply(Entry<K, V> input) {
        return new EventFiringEntry<>(input, helper);
    }

    private static class EventFiringEntry<K, V> extends ForwardingMapEntry<K, V> {

        private final Entry<K, V> input;
        private final CollectionEventHelper<Entry<K, V>> helper;

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
            Entry<K, V> old = Maps.immutableEntry(getKey(), getValue());
            super.setValue(value);
            helper.fireReplace(old, this);

            return old.getValue();
        }

    }

}
