/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
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

import static org.dlect.test.helper.DataGeneratorHelper.fillMap;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class EventFiringMapTest {

    @Mock
    private Map<String, String> delegate;

    private Map<String, String> map;
    @Mock
    CollectionEventHelper<Map.Entry<String, String>> helper;

    @InjectMocks
    private EventFiringMap<String, String> testMockedMapObject;

    private EventFiringMap<String, String> testObject;

    @Before
    public void before() {
        map = Maps.newHashMap();

        testObject = new EventFiringMap<>(map, helper);
    }

    private void configureAnswer() {
        Answer<Void> helperCheckingAnswer = new AnswerImpl();

        doAnswer(helperCheckingAnswer).when(helper).fireAdd(any(Entry.class));
        doAnswer(helperCheckingAnswer).when(helper).fireRemove(any(Entry.class));
        doAnswer(helperCheckingAnswer).when(helper).fireReplace(any(Entry.class), any(Entry.class));
    }

    private Entry<String, String> entry(String key, String value) {
        return Maps.immutableEntry(key, value);
    }

    /**
     * Test of delegate method, of class EventFiringMap.
     */
    @Test
    public void testDelegate() {
        assertSame(delegate, testMockedMapObject.delegate());
    }

    /**
     * Test of clear method, of class EventFiringMap.
     */
    @Test
    public void testClear_EmptyMap() {
        configureAnswer();
        testObject.clear();

        verifyZeroInteractions(helper);
    }

    /**
     * Test of clear method, of class EventFiringMap.
     */
    @Test
    public void testClear_WithItems() {
        Map<String, String> toAdd = fillMap(1, 10);

        map.putAll(toAdd);

        configureAnswer();

        testObject.clear();

        for (Entry<String, String> entry : toAdd.entrySet()) {
            verify(helper).fireRemove(entry);
        }
        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of put method, of class EventFiringMap.
     */
    @Test
    public void testPut_NonExisting() {
        configureAnswer();

        testObject.put("Hello", "World");

        verify(helper).fireAdd(entry("Hello", "World"));

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of put method, of class EventFiringMap.
     */
    @Test
    public void testPut_Existing() {
        map.put("Hello", "Old World");
        configureAnswer();

        testObject.put("Hello", "World");

        verify(helper).fireReplace(entry("Hello", "Old World"), entry("Hello", "World"));

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of put method, of class EventFiringMap.
     */
    @Test
    public void testPut_ExistingNoChange() {
        map.put("Hello", "World");
        configureAnswer();

        testObject.put("Hello", "World");

        verifyZeroInteractions(helper);
    }

    /**
     * Test of putAll method, of class EventFiringMap.
     */
    @Test
    public void testPutAll_NonExisting() {
        Map<String, String> toAdd = fillMap(1, 10);
        configureAnswer();
        testObject.putAll(toAdd);

        for (Entry<String, String> entry : toAdd.entrySet()) {
            verify(helper).fireAdd(entry);
        }
        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of putAll method, of class EventFiringMap.
     */
    @Test
    public void testPutAll_Existing() {
        Map<String, String> toAdd = fillMap(1, 10, "", "Old ");

        map.putAll(toAdd);

        Map<String, String> toUpdate = fillMap(1, 10, "", "New ");

        configureAnswer();
        testObject.putAll(toUpdate);

        for (String k : toAdd.keySet()) {
            String old = toAdd.get(k);
            String upd = toUpdate.get(k);

            verify(helper).fireReplace(entry(k, old), entry(k, upd));
        }
        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of putAll method, of class EventFiringMap.
     */
    @Test
    public void testPutAll_ExistingNoChange() {
        Map<String, String> toAdd = fillMap(1, 10);

        map.putAll(toAdd);

        Map<String, String> toUpdate = fillMap(1, 10);

        configureAnswer();
        testObject.putAll(toUpdate);

        verifyZeroInteractions(helper);
    }

    /**
     * Test of remove method, of class EventFiringMap.
     */
    @Test
    public void testRemove() {
        map.put("Hello", "World");
        map.put("Hi", "World2");

        configureAnswer();

        testObject.remove("Hello");

        verify(helper).fireRemove(entry("Hello", "World"));

        verifyNoMoreInteractions(helper);
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
