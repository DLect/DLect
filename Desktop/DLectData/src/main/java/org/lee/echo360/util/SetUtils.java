/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.util;

import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

/**
 *
 * @author lee
 */
public class SetUtils {

    public static <T> Set<T> filter(Set<T> all, SetUtils.Predicate<T> filter) {
        return new FilteredSet<T>(all, filter);
    }

    public static <T> SortedSet<T> filter(SortedSet<T> all, SetUtils.Predicate<T> filter) {
        return new FilteredSortedSet<T>(all, filter);
    }

    private static class FilteredSet<T> implements Set<T>, Serializable {

        static final long serialVersionUID = 9451397051349708L;
        private final Predicate<T> filter;
        private final Set<T> all;
        private transient Set<T> filteredSet;

        public FilteredSet(Set<T> all, Predicate<T> filter) {
            this.filter = filter;
            this.all = all;
        }

        private void initFiltered() {
            if (filteredSet == null) {
                filteredSet = Sets.filter(all, filter);
            }
        }

        @Override
        public int size() {
            initFiltered();
            return filteredSet.size();
        }

        @Override
        public boolean isEmpty() {
            initFiltered();
            return filteredSet.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            initFiltered();
            return filteredSet.contains(o);
        }

        @Override
        public Iterator<T> iterator() {
            initFiltered();
            return filteredSet.iterator();
        }

        @Override
        public Object[] toArray() {
            initFiltered();
            return filteredSet.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            initFiltered();
            return filteredSet.toArray(a);
        }

        @Override
        public boolean add(T e) {
            initFiltered();
            return filteredSet.add(e);
        }

        @Override
        public boolean remove(Object o) {
            initFiltered();
            return filteredSet.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            initFiltered();
            return filteredSet.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            initFiltered();
            return filteredSet.addAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            initFiltered();
            return filteredSet.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            initFiltered();
            return filteredSet.removeAll(c);
        }

        @Override
        public void clear() {
            initFiltered();
            filteredSet.clear();
        }

        @Override
        public boolean equals(Object o) {
            initFiltered();
            if (o instanceof Collection) {
                return filteredSet.equals(o);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            initFiltered();
            return filteredSet.hashCode();
        }
    }

    private static class FilteredSortedSet<T> implements SortedSet<T>, Serializable {

        static final long serialVersionUID = 534789023789234507L;
        private final Predicate<T> filter;
        private final SortedSet<T> all;
        private transient SortedSet<T> filteredSet;

        public FilteredSortedSet(SortedSet<T> all, Predicate<T> filter) {
            this.filter = filter;
            this.all = all;
        }

        private void initFiltered() {
            if (filteredSet == null) {
                filteredSet = Sets.filter(all, filter);
            }
        }

        @Override
        public Comparator<? super T> comparator() {
            initFiltered();
            return filteredSet.comparator();
        }

        @Override
        public SortedSet<T> subSet(T fromElement, T toElement) {
            initFiltered();
            return filteredSet.subSet(fromElement, toElement);
        }

        @Override
        public SortedSet<T> headSet(T toElement) {
            initFiltered();
            return filteredSet.headSet(toElement);
        }

        @Override
        public SortedSet<T> tailSet(T fromElement) {
            initFiltered();
            return filteredSet.tailSet(fromElement);
        }

        @Override
        public T first() {
            initFiltered();
            return filteredSet.first();
        }

        @Override
        public T last() {
            initFiltered();
            return filteredSet.last();
        }

        @Override
        public int size() {
            initFiltered();
            return filteredSet.size();
        }

        @Override
        public boolean isEmpty() {
            initFiltered();
            return filteredSet.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            initFiltered();
            return filteredSet.contains(o);
        }

        @Override
        public Iterator<T> iterator() {
            initFiltered();
            return filteredSet.iterator();
        }

        @Override
        public Object[] toArray() {
            initFiltered();
            return filteredSet.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            initFiltered();
            return filteredSet.toArray(a);
        }

        @Override
        public boolean add(T e) {
            initFiltered();
            return filteredSet.add(e);
        }

        @Override
        public boolean remove(Object o) {
            initFiltered();
            return filteredSet.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            initFiltered();
            return filteredSet.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            initFiltered();
            return filteredSet.addAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            initFiltered();
            return filteredSet.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            initFiltered();
            return filteredSet.removeAll(c);
        }

        @Override
        public void clear() {
            initFiltered();
            filteredSet.clear();
        }
    }

    public static interface Predicate<T> extends com.google.common.base.Predicate<T>, Serializable {

        static final long serialVersionUID = 2348792347234897L;
    }
}
