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
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
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
        for (Listenable<?> l : listenables) {
            EventAdapter adapter = l.getAdapter();
            EventAdapter parent = adapter.getParentAdapter();
            if (parent == null) {
                adapter.setParentAdapter(this.getAdapter());
            } else if (!same(parent, this.getAdapter())) {
                throw new IllegalStateException("Listenable(" + l + ") already has a parent.");
            }
        }
    }

    private boolean same(Object o1, Object o2) {
        return o1 == o2;
    }

    protected <E> List<E> wrapList(List<E> list, EventID listID) {
        return new EventFiringList<>(list, new CollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E> List<E> newWrappedList(EventID eventID) {
        return wrapList(Lists.<E>newArrayList(), eventID);
    }

    protected <E extends Listenable<E>> List<E> wrapListenableList(List<E> list, EventID listID) {
        return new EventFiringList<>(list, new ListenableCollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E extends Listenable<E>> List<E> newWrappedListenableList(EventID eventID) {
        return wrapListenableList(Lists.<E>newArrayList(), eventID);
    }

    protected <E> Set<E> wrapSet(Set<E> set, EventID listID) {
        return new EventFiringSet<>(set, new CollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E> Set<E> newWrappedSet(EventID listID) {
        return wrapSet(Sets.<E>newHashSet(), listID);
    }

    protected <E extends Listenable<E>> Set<E> wrapListenableSet(Set<E> list, EventID listID) {
        return new EventFiringSet<>(list, new ListenableCollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E extends Listenable<E>> Set<E> newWrappedListenableSet(EventID listID) {
        return wrapListenableSet(Sets.<E>newHashSet(), listID);
    }

    protected <E extends Comparable<E>> SortedSet<E> wrapSortedSet(SortedSet<E> set, EventID listID) {
        return new EventFiringSortedSet<>(set, new CollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E extends Comparable<E>> SortedSet<E> newWrappedSortedSet(EventID listID) {
        return wrapSortedSet(Sets.<E>newTreeSet(), listID);
    }

    protected <E extends Listenable<E> & Comparable<E>> SortedSet<E> wrapListenableSortedSet(SortedSet<E> list, EventID listID) {
        return new EventFiringSortedSet<>(list, new ListenableCollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E extends Listenable<E> & Comparable<E>> SortedSet<E> newWrappedListenableSortedSet(EventID listID) {
        return wrapListenableSortedSet(Sets.<E>newTreeSet(), listID);
    }

    /**
     * H.
     *
     * Please note that there is not wrapListenableMap; as it would be unclear as to which component should be
     * listenable.
     *
     * @param <K>
     * @param <V>
     * @param set
     * @param listID
     *
     * @return
     */
    protected <K, V> Map<K, V> wrapMap(Map<K, V> set, EventID listID) {
        return new EventFiringMap<>(set, new CollectionEventHelper<Entry<K, V>>(this, listID, getAdapter()));
    }

    protected <K, V> Map<K, V> newWrappedMap(EventID listID) {
        return wrapMap(Maps.<K, V>newHashMap(), listID);
    }

    protected <T> void setSet(Set<T> eventFiringCollection, Collection<T> toSetTo) {
        if (toSetTo == null) {
            eventFiringCollection.clear();
            return;
        }
        // Remove all that are not in toSetTo
        eventFiringCollection.retainAll(toSetTo);
        // Add a the remaining ones to it.sk
        eventFiringCollection.addAll(toSetTo);

    }

    protected <K, V> void setMap(Map<K, V> eventFiringCollection, Map<K, V> toSetTo) {
        if (toSetTo == null) {
            eventFiringCollection.clear();
            return;
        }
        // Remove all the keys that are not in toSetTo
        eventFiringCollection.keySet().retainAll(toSetTo.keySet());
        // Add a the remaining ones to it.sk
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

    protected void fireEvent(Event e) {
        getAdapter().fireEvent(e);
    }

}
