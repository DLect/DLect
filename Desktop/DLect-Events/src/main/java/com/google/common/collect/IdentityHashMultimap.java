/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 *
 * @author lee
 */
public class IdentityHashMultimap<K, V> extends AbstractSetMultimap<K, V> {


    /**
     * Creates a new, empty {@code IdentityHashMultimap} with the default initial
     * capacities.
     *
     * @param <K>
     * @param <V>
     * @return 
     */
    public static <K, V> IdentityHashMultimap<K, V> create() {
        return new IdentityHashMultimap<>();
    }

    /**
     * Constructs a {@code HashMultimap} with the same mappings as the specified
     * multimap. If a key-value mapping appears multiple times in the input
     * multimap, it only appears once in the constructed multimap.
     *
     * @param <K>
     * @param <V>
     * @param multimap the multimap whose contents are copied to this multimap
     *
     * @return
     */
    public static <K, V> IdentityHashMultimap<K, V> create(
            Multimap<? extends K, ? extends V> multimap) {
        return new IdentityHashMultimap<>(multimap);
    }

    private IdentityHashMultimap() {
        super(new IdentityHashMap<K, Collection<V>>());
    }

    private IdentityHashMultimap(Multimap<? extends K, ? extends V> multimap) {
        super(new IdentityHashMap<K, Collection<V>>());
        putAll(multimap);
    }

    @Override
    Set<V> createCollection() {
        return  Sets.newIdentityHashSet();
    }

}
