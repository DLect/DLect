/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.dlect.test.helper.DataGeneratorHelper.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class EventFiringMapKeySetTest {

    @Mock
    private CollectionEventHelper<Entry<String, String>> helper;

    @Mock
    private Set<Entry<String, String>> delegate;

    private Set<Entry<String, String>> set;

    @InjectMocks
    private EventFiringMapKeySet<String, String> testMockSetObject;

    private EventFiringMapKeySet<String, String> testObject;

    @Before
    public void before() {
        this.set = Sets.newHashSet();
        this.testObject = new EventFiringMapKeySet<>(set, helper);
    }

    private void configureAnswer() {
        Answer<Void> helperCheckingAnswer = new AnswerImpl();

        doAnswer(helperCheckingAnswer).when(helper).fireAdd(any(Entry.class));
        doAnswer(helperCheckingAnswer).when(helper).fireRemove(any(Entry.class));
        doAnswer(helperCheckingAnswer).when(helper).fireReplace(any(Entry.class), any(Entry.class));
    }

    /**
     * Test of add method, of class EventFiringMapKeySet.
     */
    @Test
    public void testAdd() {
        try {
            testMockSetObject.add(null);
            fail("No exception");
        } catch (UnsupportedOperationException e) {
            verifyZeroInteractions(helper, delegate);
        }
    }

    /**
     * Test of addAll method, of class EventFiringMapKeySet.
     */
    @Test
    public void testAddAll() {
        try {
            testMockSetObject.addAll(Lists.newArrayList(null, "Hi", "Ohh, A World"));
            fail("No exception");
        } catch (UnsupportedOperationException e) {
            verifyZeroInteractions(helper, delegate);
        }
    }

    /**
     * Test of clear method, of class EventFiringMapKeySet.
     */
    @Test
    public void testClear() {
        Set<Entry<String, String>> toAdd = fillEntrySet(1, 10);

        set.addAll(toAdd);
        configureAnswer();

        testObject.clear();

        for (Entry<String, String> entry : toAdd) {
            verify(helper).fireRemove(entry);
        }
        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of contains method, of class EventFiringMapKeySet.
     */
    @Test
    public void testContains() {
        Set<Entry<String, String>> toAdd = fillEntrySet(1, 10);
        Set<Entry<String, String>> toTestFalse = fillEntrySet(100, 110);
        Set<Entry<String, String>> toTestTrue = fillEntrySet(3, 10);

        set.addAll(toAdd);
        configureAnswer();

        for (Entry<String, String> entry : toTestTrue) {
            assertTrue(testObject.contains(entry.getKey()));
        }
        for (Entry<String, String> entry : toTestFalse) {
            assertFalse(testObject.contains(entry.getKey()));
        }
        verifyZeroInteractions(helper);
    }

    /**
     * Test of containsAll method, of class EventFiringMapKeySet.
     */
    @Test
    public void testContainsAll() {
        Set<Entry<String, String>> toAdd = fillEntrySet(1, 10);
        Set<String> toTestFalse = fillKeySet(5, 20);
        Set<String> toTestTrue = fillKeySet(3, 10);

        set.addAll(toAdd);
        configureAnswer();

        assertTrue(testObject.containsAll(toTestTrue));

        assertFalse(testObject.containsAll(toTestFalse));

        verifyZeroInteractions(helper);
    }

    /**
     * Test of isEmpty method, of class EventFiringMapKeySet.
     */
    @Test
    public void testIsEmpty() {
        configureAnswer();

        assertTrue(testObject.isEmpty());

        set.addAll(fillEntrySet(1, 10));

        assertFalse(testObject.isEmpty());

        verifyZeroInteractions(helper);
    }

    /**
     * Test of iterator method, of class EventFiringMapKeySet.
     */
    @Ignore
    @Test
    public void testIterator() {
    }

    /**
     * Test of remove method, of class EventFiringMapKeySet.
     */
    @Ignore
    @Test
    public void testRemove() {
    }

    /**
     * Test of removeAll method, of class EventFiringMapKeySet.
     */
    @Ignore
    @Test
    public void testRemoveAll() {
    }

    /**
     * Test of retainAll method, of class EventFiringMapKeySet.
     */
    @Ignore
    @Test
    public void testRetainAll() {
    }

    /**
     * Test of size method, of class EventFiringMapKeySet.
     */
    @Ignore
    @Test
    public void testSize() {
    }

    /**
     * Test of toArray method, of class EventFiringMapKeySet.
     */
    @Ignore
    @Test
    public void testToArray_0args() {
    }

    /**
     * Test of toArray method, of class EventFiringMapKeySet.
     */
    @Ignore
    @Test
    public void testToArray_GenericType() {
    }

    private class AnswerImpl implements Answer<Void> {

        public AnswerImpl() {
        }
        Set<Entry<String, String>> prev = Sets.newHashSet(set);

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            String name = invocation.getMethod().getName();
            if (name.contains("Add")) {
                checkAdd(invocation.getArguments()[0]);
            } else if (name.contains("Remove")) {
                checkRemove(invocation.getArguments()[0]);
            } else if (name.contains("Replace")) {
                checkReplace(invocation.getArguments()[0], invocation.getArguments()[1]);
            } else {
                throw new IllegalStateException("Illegal Method Call: " + invocation);
            }
            prev = Sets.newHashSet(set);;
            return null;
        }

        private void checkAdd(Object object) {
            Set<Entry<String, String>> copy = Sets.newHashSet(set);

            copy.removeAll(prev);

            assertEquals(1, copy.size());
            assertEquals(object, copy.iterator().next());
        }

        private void checkRemove(Object object) {
            Set<Entry<String, String>> copy = Sets.newHashSet(prev);

            copy.removeAll(set);

            assertEquals(1, copy.size());
            assertEquals(object, copy.iterator().next());
        }

        private void checkReplace(Object ori, Object repl) {
            assertEquals(prev.size(), set.size());
            Set<Entry<String, String>> copy = Sets.newHashSet(prev);

            copy.removeAll(set);

            assertEquals(1, copy.size());
            assertEquals(ori, copy.iterator().next());

            copy = Sets.newHashSet(set);

            copy.removeAll(prev);

            assertEquals(1, copy.size());
            assertEquals(repl, copy.iterator().next());
        }
    }

}
