/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.listenable;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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

    protected <E extends Listenable<E>> List<E> wrapListenableList(List<E> list, EventID listID) {
        return new EventFiringList<>(list, new ListenableCollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E> Set<E> wrapSet(Set<E> set, EventID listID) {
        return new EventFiringSet<>(set, new CollectionEventHelper<E>(this, listID, getAdapter()));
    }

    protected <E extends Listenable<E>> Set<E> wrapListenableSet(Set<E> list, EventID listID) {
        return new EventFiringSet<>(list, new ListenableCollectionEventHelper<E>(this, listID, getAdapter()));
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

    protected void fireEvent(Event e) {
        getAdapter().fireEvent(e);
    }

}
