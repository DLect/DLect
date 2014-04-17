/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Maps;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.TestStringMapGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.features.SetFeature;
import java.util.Map;
import java.util.Map.Entry;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 *
 * @author lee
 */
public class EventFiringMapGuavaTest extends TestCase {

    public static Test suite() {
        return MapTestSuiteBuilder
                .using(new TestStringMapGenerator() {

                    @Override
                    protected Map<String, String> create(Entry<String, String>[] entries) {
                        EventFiringMap<String, String> m = new EventFiringMap<>(Maps.<String, String>newHashMap(), new NoOpCollectionEventHelper<Entry<String, String>>());
                        for (Entry<String, String> e : entries) {
                            m.put(e.getKey(), e.getValue());
                        }
                        return m;
                    }

                })
                // You can optionally give a name to your test suite. This
                // name is used by JUnit and other tools during report
                // generation.
                .named("EventFiringMap tests")
                // Guava has a host of "features" in the
                // com.google.common.collect.testing.features package. Use
                // them to specify how the collection should behave, and
                // what operations are supported.
                .withFeatures(
                        MapFeature.ALLOWS_NULL_KEYS,
                        MapFeature.ALLOWS_NULL_QUERIES,
                        MapFeature.ALLOWS_NULL_VALUES,
                        MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                        MapFeature.GENERAL_PURPOSE,
                        MapFeature.SUPPORTS_PUT,
                        MapFeature.SUPPORTS_REMOVE,
                        CollectionFeature.SUPPORTS_REMOVE,
                        CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
                        CollectionFeature.ALLOWS_NULL_QUERIES,
                        CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                        CollectionSize.ANY
                ).createTestSuite();
    }
}
