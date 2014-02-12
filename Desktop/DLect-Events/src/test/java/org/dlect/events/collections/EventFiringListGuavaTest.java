/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Lists;
import com.google.common.collect.testing.ListTestSuiteBuilder;
import com.google.common.collect.testing.TestStringListGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import junit.framework.Test;
import org.junit.Ignore;

/**
 *
 * @author lee
 */
@Ignore
public class EventFiringListGuavaTest extends TestCase {

    public static Test suite() {
        return ListTestSuiteBuilder
                .using(new TestStringListGenerator() {

                    @Override
                    protected List<String> create(String[] elements) {
                        EventFiringList<String> l = new EventFiringList<>(Lists.<String>newArrayList(), new NoOpCollectionEventHelper<String>());
                        l.addAll(Arrays.asList(elements));
                        return l;
                    }

                })
                // You can optionally give a name to your test suite. This
                // name is used by JUnit and other tools during report
                // generation.
                .named("EventFiringList tests")
                // Guava has a host of "features" in the
                // com.google.common.collect.testing.features package. Use
                // them to specify how the collection should behave, and
                // what operations are supported.
                .withFeatures(
                        ListFeature.GENERAL_PURPOSE,
                        ListFeature.SUPPORTS_ADD_WITH_INDEX,
                        ListFeature.SUPPORTS_REMOVE_WITH_INDEX,
                        ListFeature.SUPPORTS_SET,
                        CollectionFeature.SUPPORTS_ADD,
                        CollectionFeature.SUPPORTS_REMOVE,
                        CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
                        CollectionFeature.ALLOWS_NULL_VALUES,
                        CollectionFeature.ALLOWS_NULL_QUERIES,
                        CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                        CollectionFeature.GENERAL_PURPOSE,
                        CollectionFeature.SUBSET_VIEW,
                        CollectionSize.ANY
                ).createTestSuite();
    }
}
