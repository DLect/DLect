/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
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
    @Test
    public void testIterator() {
        Map<String, String> map = fillMap(1, 10);
        Set<Entry<String, String>> toAdd = map.entrySet();

        List<String> removed = Lists.newArrayList();

        set.addAll(toAdd);

        configureAnswer();

        Iterator<String> it = testObject.iterator();
        for (; it.hasNext();) {
            String entry = it.next();

            it.remove();
            removed.add(entry);
        }

        InOrder inOrder = inOrder(helper);
        for (String rem : removed) {
            inOrder.verify(helper).fireRemove(Maps.immutableEntry(rem, map.get(rem)));
        }

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of remove method, of class EventFiringMapKeySet.
     */
    @Test
    public void testRemove() {
        Map<String, String> map = fillMap(1, 10);
        Set<Entry<String, String>> toAdd = map.entrySet();

        List<String> removed = Lists.newArrayList();

        set.addAll(toAdd);

        configureAnswer();

        for (Entry<String, String> entry : toAdd) {
            String key = entry.getKey();

            testObject.remove(key);

            removed.add(key);
        }

        InOrder inOrder = inOrder(helper);
        for (String rem : removed) {
            inOrder.verify(helper).fireRemove(Maps.immutableEntry(rem, map.get(rem)));
        }

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of remove method, of class EventFiringMapKeySet.
     */
    @Test
    public void testRemove_No_Elements() {
        configureAnswer();

        testObject.remove("I don't exist");

        verifyZeroInteractions(helper);
    }

    /**
     * Test of removeAll method, of class EventFiringMapKeySet.
     */
    @Test
    public void testRemoveAll() {
        Map<String, String> map = fillMap(1, 10);
        Set<Entry<String, String>> toAdd = map.entrySet();

        Set<String> toRemove = fillKeySet(1, 5);

        set.addAll(toAdd);

        configureAnswer();

        testObject.removeAll(toRemove);

        for (String rem : toRemove) {
            verify(helper).fireRemove(Maps.immutableEntry(rem, map.get(rem)));
        }

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of retainAll method, of class EventFiringMapKeySet.
     */
    @Test
    public void testRetainAll() {
        Map<String, String> map = fillMap(1, 10);
        Set<Entry<String, String>> toAdd = map.entrySet();

        Set<String> toRetain = fillKeySet(1, 5);
        Set<String> removed = Sets.newHashSet(map.keySet());
        removed.removeAll(toRetain);

        System.out.println("Removed List: " + removed);
        
        set.addAll(toAdd);

        configureAnswer();

        System.out.println("Set Before: " + set);

        testObject.retainAll(toRetain);

        System.out.println("Set After: " + set);
        
        for (String rem : removed) {
            System.out.println("Removed: " + rem);
            verify(helper).fireRemove(Maps.immutableEntry(rem, map.get(rem)));
        }

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of size method, of class EventFiringMapKeySet.
     */
    @Test
    public void testSize() {
        configureAnswer();

        assertEquals(0, testObject.size());

        set.add(Maps.immutableEntry("Key", "Value"));

        assertEquals(1, testObject.size());

        verifyZeroInteractions(helper);
    }

    @Test
    public void testSafeIterator() {
        // This method is used internally as a cheeper iterator. It does not fire events.
        Map<String, String> map = fillMap(1, 10);
        Set<Entry<String, String>> toAdd = map.entrySet();

        set.addAll(toAdd);

        configureAnswer();

        Iterator<String> it = testObject.safeIterator();
        for (; it.hasNext();) {
            it.next();
            try {
                it.remove();
                fail("No Exception thrown");
            } catch (UnsupportedOperationException e) {
            }
        }
        verifyZeroInteractions(helper);
    }

    /**
     * Test of toArray method, of class EventFiringMapKeySet.
     */
    @Test
    public void testToArray_0args() {
        Map<String, String> map = fillMap(1, 10);
        Set<Entry<String, String>> toAdd = map.entrySet();

        set.addAll(toAdd);

        configureAnswer();

        Object[] it = testObject.toArray();

        assertEquals(map.keySet(), ImmutableSet.copyOf(it));

        verifyZeroInteractions(helper);
    }

    /**
     * Test of toArray method, of class EventFiringMapKeySet.
     */
    @Test
    public void testToArray_GenericType() {
        Map<String, String> map = fillMap(1, 10);
        Set<Entry<String, String>> toAdd = map.entrySet();

        set.addAll(toAdd);

        configureAnswer();

        String[] it = testObject.toArray(new String[1]);

        assertEquals(map.keySet(), ImmutableSet.copyOf(it));

        verifyZeroInteractions(helper);
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
