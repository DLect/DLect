/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.listenable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Nonnull;
import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventAdapterBuilder;
import org.dlect.events.EventID;
import org.dlect.events.EventListener;
import org.dlect.events.collections.CollectionEventHelper;
import org.dlect.events.collections.EventFiringList;
import org.dlect.events.collections.EventFiringMap;
import org.dlect.events.collections.EventFiringSet;
import org.dlect.events.collections.EventFiringSortedSet;

/**
 *
 * @author lee
 * @param <T> The class that this class should use.
 */
public class Listenable<T extends Listenable<T>> {

    protected <S extends Comparable> Ordering<S> ordering() {
        return Ordering.natural().nullsLast();
    }

    private final EventAdapter e = EventAdapterBuilder.getNewAdapter();

    protected EventAdapter getAdapter() {
        return e;
    }

    public boolean addListener(@Nonnull EventListener l, Class<?>... listeningClasses) {
        return getAdapter().addListener(l, listeningClasses);
    }

    public boolean removeListener(EventListener l) {
        return getAdapter().removeListener(l);
    }

    protected void addChild(Listenable<?>... listenables) {
        EventAdapter thisAdapter = this.getAdapter();
        for (Listenable<?> l : listenables) {
            EventAdapter adapter = l.getAdapter();
            EventAdapter parent = adapter.getParentAdapter();
            if (parent == null) {
                adapter.setParentAdapter(thisAdapter);
            } else if (parent != this.getAdapter()) {
                throw new IllegalStateException("Listenable(" + l + ") already has a parent.");
            }
        }
    }

    protected void fireEvent(Event e) {
        getAdapter().fireEvent(e);
    }

    /**
     * Fires an event that does not have values associated with it. For example to indicate that an item has completed
     * processing.
     *
     * @param e
     */
    protected void fireEvent(EventID e) {
        getAdapter().fireEvent(new Event(this, e, null, null));
    }

    protected <T> EventBuilder<T> event(EventID eid) {
        return new EventBuilder<>(this, eid, getAdapter());
    }

    //<editor-fold defaultstate="collapsed" desc=" COLLECTIONS HELPER METHODS ">
    protected <E> List<E> newWrappedList(EventID eventID) {
        return new EventFiringList<>(Lists.<E>newArrayList(), new CollectionEventHelper<E>(this, eventID, getAdapter()));
    }

    protected <E extends Listenable<E>> List<E> newWrappedListenableList(EventID eventID) {
        return new EventFiringList<>(Lists.<E>newArrayList(), new ListenableCollectionEventHelper<E>(this, eventID, getAdapter()));
    }

    protected <E> Set<E> newWrappedSet(EventID listID) {
        return new EventFiringSet<>(Sets.<E>newHashSet(), new CollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E extends Listenable<E>> Set<E> newWrappedListenableSet(EventID listID) {
        return new EventFiringSet<>(Sets.<E>newHashSet(), new ListenableCollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E extends Comparable<E>> SortedSet<E> newWrappedSortedSet(EventID listID) {
        return new EventFiringSortedSet<>(Sets.<E>newTreeSet(), new CollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E extends Listenable<E> & Comparable<E>> SortedSet<E> newWrappedListenableSortedSet(EventID listID) {
        return new EventFiringSortedSet<>(new TreeSet<E>(), new ListenableCollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <K, V> Map<K, V> newWrappedMap(EventID listID) {
        return new EventFiringMap<>(Maps.<K, V>newHashMap(), new CollectionEventHelper<Entry<K, V>>(this, listID, getAdapter()));
    }

    protected <K, V extends Listenable<V>> Map<K, V> newWrappedListenableValueMap(EventID listID) {
        return new EventFiringMap<>(Maps.<K, V>newHashMap(), new MapValueListenableCollectionEventHelper<K, V>(this, listID, getAdapter()));
    }

    protected <T> void setSet(Set<T> eventFiringCollection, Collection<T> toSetTo) {
        if (toSetTo == null) {
            eventFiringCollection.clear();
            return;
        }
        // Remove all that are not in toSetTo
        eventFiringCollection.retainAll(toSetTo);
        // Add a the remaining ones to it.
        eventFiringCollection.addAll(toSetTo);

    }

    protected <K, V> void setMap(Map<K, V> eventFiringCollection, Map<K, V> toSetTo) {
        if (toSetTo == null) {
            eventFiringCollection.clear();
            return;
        }
        // Remove all the keys that are not in toSetTo
        eventFiringCollection.keySet().retainAll(toSetTo.keySet());
        // Add a the remaining ones to it.
        eventFiringCollection.putAll(toSetTo);
    }

    protected <T> void setList(List<T> eventFiringCollection, Collection<T> toSetTo) {
        eventFiringCollection.clear();
        eventFiringCollection.addAll(toSetTo);
    }

    protected <T> ImmutableSet<T> copyOf(Set<T> set) {
        return ImmutableSet.copyOf(set);
    }

    protected <K, V> ImmutableMap<K, V> copyOf(Map<K, V> map) {
        return ImmutableMap.copyOf(map);
    }

    protected <T> ImmutableList<T> copyOf(List<T> map) {
        return ImmutableList.copyOf(map);
    }

    protected <T> ImmutableSortedSet<T> copyOf(SortedSet<T> set) {
        return ImmutableSortedSet.copyOf(set);
    }
//</editor-fold>
}
