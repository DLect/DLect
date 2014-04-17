/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.dlect.test.helper.DataGeneratorHelper.fillSet;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class EventFiringSetTest {

    @Mock
    private CollectionEventHelper<String> helper;

    private EventFiringSet<String> testObject;

    private Set<String> set;

    @Before
    public void before() {
        set = Sets.newHashSet();
        testObject = new EventFiringSet<>(set, helper);

    }

    private void configureAnswer() {
        Answer<Void> helperCheckingAnswer = new AnswerImpl();

        doAnswer(helperCheckingAnswer).when(helper).fireAdd(any(String.class));
        doAnswer(helperCheckingAnswer).when(helper).fireRemove(any(String.class));
        doAnswer(helperCheckingAnswer).when(helper).fireReplace(any(String.class), any(String.class));
    }

    /**
     * Test of delegate method, of class EventFiringSet.
     */
    @Test
    public void testDelegate() {
        assertSame(set, testObject.delegate());
    }

    /**
     * Test of getHelper method, of class EventFiringSet.
     */
    @Test
    public void testGetHelper() {
        assertSame(helper, testObject.getHelper());
    }

    /**
     * Test of add method, of class EventFiringSet.
     */
    @Test
    public void testAdd_New() {
        configureAnswer();

        assertTrue(testObject.add("Hello World"));

        verify(helper).fireAdd("Hello World");

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of add method, of class EventFiringSet.
     */
    @Test
    public void testAdd_Existing() {
        set.add("Hello World");
        configureAnswer();

        assertFalse(testObject.add("Hello World"));

        verifyZeroInteractions(helper);
    }

    /**
     * Test of addAll method, of class EventFiringSet.
     */
    @Test
    public void testAddAll() {
        Set<String> toAdd = fillSet(1, 10);

        configureAnswer();

        testObject.addAll(toAdd);

        for (String string : toAdd) {
            verify(helper).fireAdd(string);
        }

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of clear method, of class EventFiringSet.
     */
    @Test
    public void testClear() {
        Set<String> toClear = fillSet(1, 10);

        set.addAll(toClear);

        configureAnswer();

        testObject.clear();

        for (String string : toClear) {
            verify(helper).fireRemove(string);
        }

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of iterator method, of class EventFiringSet.
     */
    @Test
    public void testIterator() {
        Set<String> toClear = fillSet(1, 10);

        List<String> removeOrder = Lists.newArrayList();

        set.addAll(toClear);

        configureAnswer();

        for (Iterator<String> it = testObject.iterator(); it.hasNext();) {
            removeOrder.add(it.next());

            it.remove();
        }
        InOrder inOrder = inOrder(helper);
        for (String string : removeOrder) {
            inOrder.verify(helper).fireRemove(string);
        }

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of remove method, of class EventFiringSet.
     */
    @Test
    public void testRemove_Existant() {
        set.add("Bye");

        configureAnswer();

        assertTrue(testObject.remove("Bye"));

        verify(helper).fireRemove("Bye");

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of remove method, of class EventFiringSet.
     */
    @Test
    public void testRemove_NonExistant() {
        set.add("Bye Cruel World");

        configureAnswer();

        assertFalse(testObject.remove("Bye"));

        verifyZeroInteractions(helper);
    }

    @Test
    public void testRemoveAll_Valid() {
        Set<String> content = fillSet(1, 10);

        set.addAll(content);

        configureAnswer();

        testObject.removeAll(content);

        for (String encounter : content) {
            verify(helper).fireRemove(encounter);
        }
        verifyNoMoreInteractions(helper);
    }

    @Test
    public void testRemoveAll_NotValid() {
        Set<String> content = fillSet(1, 10);
        Set<String> toRemove = fillSet(100, 110);

        set.addAll(content);

        configureAnswer();

        testObject.removeAll(toRemove);

        verifyZeroInteractions(helper);
    }

    @Test
    public void testRetainAll_NonRetained() {
        Set<String> content = fillSet(1, 10);
        Set<String> toRetain = fillSet(100, 110);

        set.addAll(content);

        configureAnswer();

        testObject.retainAll(toRetain);

        for (String encounter : content) {
            verify(helper).fireRemove(encounter);
        }
        verifyNoMoreInteractions(helper);
    }

    @Test
    public void testRetainAll_AllRetained() {
        Set<String> content = fillSet(1, 10);

        set.addAll(content);

        configureAnswer();

        testObject.retainAll(content);

        verifyZeroInteractions(helper);
    }

    private class AnswerImpl implements Answer<Void> {

        public AnswerImpl() {
        }
        Set<String> prev = Sets.newHashSet(set);

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
            prev = Sets.newHashSet(set);
            return null;
        }

        private void checkAdd(Object object) {
            Set<String> copy = Sets.newHashSet(set);

            copy.removeAll(prev);

            assertEquals(1, copy.size());
            assertEquals(object, copy.iterator().next());
        }

        private void checkRemove(Object object) {
            Set<String> copy = Sets.newHashSet(prev);

            copy.removeAll(set);

            assertEquals(1, copy.size());
            assertEquals(object, copy.iterator().next());
        }

        private void checkReplace(Object ori, Object repl) {
            assertEquals(prev.size(), set.size());
            Set<String> copy = Sets.newHashSet(prev);

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
