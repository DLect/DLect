/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Sets;
import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 *
 * @author lee
 */
public class EventFiringSetGuavaTest extends TestCase {

    public static Test suite() {
        return SetTestSuiteBuilder
                .using(new TestStringSetGenerator() {

                    @Override
                    protected Set<String> create(String[] entries) {
                        return new EventFiringSet<>(Sets.newHashSet(entries), new NoOpCollectionEventHelper<String>());
                    }

                })
                // You can optionally give a name to your test suite. This
                // name is used by JUnit and other tools during report
                // generation.
                .named("EventFiringSet tests")
                // Guava has a host of "features" in the
                // com.google.common.collect.testing.features package. Use
                // them to specify how the collection should behave, and
                // what operations are supported.
                .withFeatures(
                        CollectionFeature.ALLOWS_NULL_QUERIES,
                        CollectionFeature.ALLOWS_NULL_VALUES,
                        CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                        CollectionFeature.GENERAL_PURPOSE,
                        SetFeature.GENERAL_PURPOSE,
                        CollectionSize.ANY
                ).createTestSuite();
    }
}
