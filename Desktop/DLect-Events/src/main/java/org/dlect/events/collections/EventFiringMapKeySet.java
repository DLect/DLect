/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import static com.google.common.base.Predicates.*;

public class EventFiringMapKeySet<K, V> implements Set<K> {

    private final CollectionEventHelper<Entry<K, V>> helper;

    private final Set<Entry<K, V>> delegate;

    private final Function<Entry<K, V>, K> entryToKeyFunction = new Function<Entry<K, V>, K>() {
        @Override
        public K apply(Entry<K, V> input) {
            return input.getKey();
        }
    };

    public EventFiringMapKeySet(Set<Entry<K, V>> delegate, CollectionEventHelper<Entry<K, V>> helper) {
        this.delegate = delegate;
        this.helper = helper;
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
        for (Iterator<Entry<K, V>> it = delegate.iterator(); it.hasNext();) {
            Entry<K, V> entry = it.next();
            it.remove();
            helper.fireRemove(entry);
        }
    }

    @Override
    public boolean contains(Object o) {
        return Iterators.any(this.safeIterator(), equalTo(o));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // If all elements of `c` are in the key set.
        return Iterators.all(c.iterator(), inThis());
    }

    @SuppressWarnings("unchecked")
    private Predicate<Object> inThis() {
        return (Predicate<Object>) in(this);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public Iterator<K> iterator() {
        return Iterators.transform(new EventFiringIterator<>(delegate.iterator(), helper), entryToKeyFunction);
    }

    protected Iterator<K> safeIterator() {
        return new Iterator<K>() {

            private final Iterator<Entry<K, V>> delg = delegate.iterator();

            @Override
            public boolean hasNext() {
                return delg.hasNext();
            }

            @Override
            public K next() {
                return delg.next().getKey();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Do not use `safeIterator` if you need to remove objects from the iterator.");
            }

        };
    }

    @Override
    public boolean remove(Object o) {
        for (Iterator<Entry<K, V>> it = delegate.iterator(); it.hasNext();) {
            Entry<K, V> entry = it.next();
            if (Objects.equal(entry.getKey(), o)) {
                it.remove();
                helper.fireRemove(entry);
                return true;
            }
        }
        return false;
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
        return delegate.size();
    }

    @Override
    public Object[] toArray() {
        return Iterators.toArray(this.safeIterator(), Object.class);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return Lists.newArrayList(this.safeIterator()).toArray(a);
    }

}
