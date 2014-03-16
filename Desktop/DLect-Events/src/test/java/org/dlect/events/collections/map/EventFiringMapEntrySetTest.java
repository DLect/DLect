/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections.map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.dlect.events.collections.CollectionEventHelper;
import org.dlect.events.collections.EventFiringMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.dlect.test.helper.DataGeneratorHelper.fillMap;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class EventFiringMapEntrySetTest {

    private Map<String, String> map;

    @Mock
    private CollectionEventHelper<Entry<String, String>> helper;

    private EventFiringMap<String, String> testMap;

    private Set<Entry<String, String>> testEntrySet;

    private Entry<String, String> entry(String key, String value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    private Entry<String, String> entry(Entry<String, String> entry) {
        return new AbstractMap.SimpleEntry<>(entry);
    }

    @Before
    public void setUp() {
        map = Maps.newHashMap();
        testMap = new EventFiringMap<>(map, helper);
        testEntrySet = testMap.entrySet();

    }

    private void configureAnswer() {
        Answer<Void> helperCheckingAnswer = new AnswerImpl();

        doAnswer(helperCheckingAnswer).when(helper).fireAdd(any(Entry.class));
        doAnswer(helperCheckingAnswer).when(helper).fireRemove(any(Entry.class));
        doAnswer(helperCheckingAnswer).when(helper).fireReplace(any(Entry.class), any(Entry.class));
    }

    @Test
    public void testAdd() {
        try {
            testEntrySet.add(entry("Hello", "World"));
            fail("No exception thrown");
        } catch (UnsupportedOperationException e) {
            verifyZeroInteractions(helper);
        }
    }

    @Test
    public void testAddAll() {
        try {
            testEntrySet.addAll(Lists.newArrayList(entry("Hello", "World")));
            fail("No exception thrown");
        } catch (UnsupportedOperationException e) {
            verifyZeroInteractions(helper);
        }
    }

    @Test
    public void testClear() {
        Map<String, String> content = fillMap(1, 10);

        map.putAll(content);

        configureAnswer();

        testEntrySet.clear();

        for (Entry<String, String> entry : content.entrySet()) {
            verify(helper).fireRemove(entry);
        }
        verifyNoMoreInteractions(helper);
    }

    @Test
    public void testIterator() {
        Map<String, String> content = fillMap(1, 10);

        List<Entry<String, String>> encountered = Lists.newArrayList();

        map.putAll(content);

        configureAnswer();

        Iterator<Entry<String, String>> it = testEntrySet.iterator();
        for (; it.hasNext();) {
            Entry<String, String> next = it.next();
            Entry<String, String> old = entry(next);
            encountered.add(old);

            next.setValue(next.getValue() + " New");

            it.remove();
        }

        assertTrue(map.isEmpty());
        assertTrue(testEntrySet.isEmpty());

        InOrder inOrder = inOrder(helper);

        for (Entry<String, String> encounter : encountered) {
            Entry<String, String> newEntry = entry(encounter);
            newEntry.setValue(newEntry.getValue() + " New");
            inOrder.verify(helper).fireReplace(encounter, newEntry);
            inOrder.verify(helper).fireRemove(newEntry);
        }
        verifyNoMoreInteractions(helper);

    }

    @Test
    public void testIterator_SetValueNoChange() {
        Map<String, String> content = fillMap(1, 10);

        map.putAll(content);

        configureAnswer();

        Iterator<Entry<String, String>> it = testEntrySet.iterator();
        for (; it.hasNext();) {
            Entry<String, String> next = it.next();

            next.setValue(next.getValue());
        }

        verifyNoMoreInteractions(helper);
    }

    @Test
    public void testRemove_Valid() {
        Map<String, String> content = fillMap(1, 10);

        List<Entry<String, String>> encountered = Lists.newArrayList();

        map.putAll(content);

        configureAnswer();

        for (Entry<String, String> entry : content.entrySet()) {
            testEntrySet.remove(entry);
            encountered.add(entry);
        }

        assertTrue(map.isEmpty());
        assertTrue(testEntrySet.isEmpty());

        InOrder inOrder = inOrder(helper);

        for (Entry<String, String> encounter : encountered) {
            inOrder.verify(helper).fireRemove(encounter);
        }
        verifyNoMoreInteractions(helper);
    }

    @Test
    public void testRemove_Invalid() {
        Map<String, String> content = fillMap(1, 10);

        map.putAll(content);

        configureAnswer();

        testEntrySet.remove(entry("Brave New", "World"));

        verifyZeroInteractions(helper);
    }

    @Test
    public void testRemoveAll_Valid() {
        Map<String, String> content = fillMap(1, 10);

        map.putAll(content);

        configureAnswer();

        testEntrySet.removeAll(content.entrySet());

        assertTrue(map.isEmpty());
        assertTrue(testEntrySet.isEmpty());

        for (Entry<String, String> encounter : content.entrySet()) {
            verify(helper).fireRemove(encounter);
        }
        verifyNoMoreInteractions(helper);
    }

    @Test
    public void testRemoveAll_NotValid() {
        Map<String, String> content = fillMap(1, 10);
        Map<String, String> toRemove = fillMap(100, 110);

        List<Entry<String, String>> encountered = Lists.newArrayList();

        map.putAll(content);

        configureAnswer();

        testEntrySet.removeAll(toRemove.entrySet());

        verifyZeroInteractions(helper);
    }

    @Test
    public void testRetainAll_NonRetained() {
        Map<String, String> content = fillMap(1, 10);
        Map<String, String> toRetain = fillMap(100, 110);

        map.putAll(content);

        configureAnswer();

        testEntrySet.retainAll(toRetain.entrySet());

        assertTrue(map.isEmpty());
        assertTrue(testEntrySet.isEmpty());

        for (Entry<String, String> encounter : content.entrySet()) {
            verify(helper).fireRemove(encounter);
        }
        verifyNoMoreInteractions(helper);
    }

    @Test
    public void testRetainAll_AllRetained() {
        Map<String, String> content = fillMap(1, 10);

        map.putAll(content);

        configureAnswer();

        testEntrySet.retainAll(content.entrySet());

        assertFalse(map.isEmpty());
        assertFalse(testEntrySet.isEmpty());

        verifyZeroInteractions(helper);
    }

    private class AnswerImpl implements Answer<Void> {

        public AnswerImpl() {
        }
        private Set<Entry<String, String>> prev = copyMap();

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
            prev = copyMap();
            return null;
        }

        private Entry<String, String> assertEntryAndImmutable(Object object) {
            assertTrue(object instanceof Entry);
            Entry<String, String> e = (Entry<String, String>) object;
            try {
                try {
                    e.setValue("New Value - Never before seen prices");
                } catch (AssertionError er) {
                    throw new AssertionError("Set Value ran away and caused an error", er);
                }
                fail("No exception thrown on set value");
            } catch (UnsupportedOperationException ex) {
                // No Op.
            }
            return e;
        }

        private void checkAdd(Object object) {
            assertEquals(prev.size() + 1, map.size());

            Entry<String, String> e = assertEntryAndImmutable(object);

            Set<Entry<String, String>> copy = copyMap();

            copy.removeAll(copyPrev());

            assertEquals(1, copy.size());
            Entry<String, String> next = copy.iterator().next();
            assertEquals(e, next);
            assertEquals(e.getKey(), next.getKey());
            assertEquals(e.getValue(), next.getValue());
        }

        private void checkRemove(Object object) {
            assertEquals(prev.size() - 1, map.size());

            Entry<String, String> e = assertEntryAndImmutable(object);

            Set<Entry<String, String>> copy = copyPrev();

            copy.removeAll(copyMap());

            assertEquals(1, copy.size());

            Entry<String, String> next = copy.iterator().next();
            assertEquals(e, next);
            assertEquals(e.getKey(), next.getKey());
            assertEquals(e.getValue(), next.getValue());
        }

        private void checkReplace(Object oriO, Object replO) {
            assertEquals("Map size changed.", prev.size(), map.size());

            Entry<String, String> ori = assertEntryAndImmutable(oriO);
            Entry<String, String> repl = assertEntryAndImmutable(replO);

            assertEquals(ori.getKey(), repl.getKey());

            Set<Entry<String, String>> copy = copyPrev();

            copy.removeAll(copyMap());

            assertEquals("Compare the pair: " + prev + "\nCompare the pair: " + map.entrySet(), 1, copy.size());

            Entry<String, String> next = copy.iterator().next();
            assertEquals(ori, next);
            assertEquals(ori.getKey(), next.getKey());
            assertEquals(ori.getValue(), next.getValue());

            // \/ NEW VALUE CORRECT \/
            copy = copyMap();

            copy.removeAll(copyPrev());

            assertEquals(1, copy.size());
            next = copy.iterator().next();
            assertEquals(repl, next);
            assertEquals(repl.getKey(), next.getKey());
            assertEquals(repl.getValue(), next.getValue());
        }

        private Set<Entry<String, String>> copyPrev() {
            return Sets.newHashSet(prev);
        }

        private Set<Entry<String, String>> copyMap() {
            return Maps.newHashMap(map).entrySet();
        }
    }
}
