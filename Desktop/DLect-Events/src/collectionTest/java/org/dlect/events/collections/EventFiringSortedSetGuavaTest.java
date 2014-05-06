/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Sets;
import com.google.common.collect.testing.SortedSetTestSuiteBuilder;
import com.google.common.collect.testing.TestStringSortedSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 *
 * @author lee
 */
public class EventFiringSortedSetGuavaTest extends TestCase {

    public static Test suite() {
        return SortedSetTestSuiteBuilder
                .using(new TestStringSortedSetGenerator() {

                    @Override
                    protected SortedSet<String> create(String[] entries) {
                        TreeSet<String> s = Sets.newTreeSet();
                        s.addAll(Arrays.asList(entries));
                        return new EventFiringSortedSet<>(s, new NoOpCollectionEventHelper<String>());
                    }

                })
                // You can optionally give a name to your test suite. This
                // name is used by JUnit and other tools during report
                // generation.
                .named("EventFiringSortedSet tests")
                // Guava has a host of "features" in the
                // com.google.common.collect.testing.features package. Use
                // them to specify how the collection should behave, and
                // what operations are supported.
                .withFeatures(
                        CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                        CollectionFeature.GENERAL_PURPOSE,
                        CollectionFeature.KNOWN_ORDER,
                        CollectionFeature.DESCENDING_VIEW,
                        CollectionFeature.SUBSET_VIEW,
                        SetFeature.GENERAL_PURPOSE,
                        CollectionSize.ANY
                ).createTestSuite();
    }
}
