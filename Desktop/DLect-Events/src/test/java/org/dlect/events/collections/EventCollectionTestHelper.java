/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.dlect.events.EventAdapter;

import static org.junit.Assert.assertSame;

/**
 *
 * @author lee
 */
public class EventCollectionTestHelper {

    public static <T> CollectionEventHelper<T> verifyEventFiringList(EventFiringList<T> efl, List<T> original, EventAdapter adapter) {
        assertSame(efl.delegate(), original);

        assertSame(efl.getHelper().getAdapter(), adapter);

        return efl.getHelper();
    }

    public static <T> CollectionEventHelper<T> verifyEventFiringSet(EventFiringSet<T> efl, Set<T> original, EventAdapter adapter) {
        assertSame(efl.delegate(), original);

        assertSame(efl.getHelper().getAdapter(), adapter);

        return efl.getHelper();
    }

    public static <K, V> CollectionEventHelper<Entry<K, V>> verifyEventFiringMap(EventFiringMap<K, V> efl, Map<K, V> original, EventAdapter adapter) {
        assertSame(efl.delegate(), original);

        assertSame(efl.getHelper().getAdapter(), adapter);

        return efl.getHelper();
    }

}
