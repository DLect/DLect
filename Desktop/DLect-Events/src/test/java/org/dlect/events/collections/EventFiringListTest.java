/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.dlect.test.helper.DataGeneratorHelper.fillList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class EventFiringListTest {

    private InOrder inOrder;

    @Mock
    private List<Object> delegate;

    private List<Object> list;

    @Mock
    private CollectionEventHelper<Object> helper;

    @InjectMocks
    private EventFiringList<Object> testMockedListObject;

    private EventFiringList<Object> testObject;

    @Before
    public void before() {
        list = new ArrayList<>();
        testObject = new EventFiringList<>(list, helper);
        inOrder = inOrder(delegate, helper);
    }

    private void configureAnswer() {
        Answer<Void> helperCheckingAnswer = new AnswerImpl();

        doAnswer(helperCheckingAnswer).when(helper).fireAdd(any());
        doAnswer(helperCheckingAnswer).when(helper).fireRemove(any());
        doAnswer(helperCheckingAnswer).when(helper).fireReplace(any(), any());
    }

    /**
     * Test of delegate method, of class EventFiringList.
     */
    @Test
    public void testDelegate() {
        assertSame(list, testObject.delegate());
        assertSame(delegate, testMockedListObject.delegate());
    }

    @Test
    public void testGetHelper() {
        assertSame(helper, testObject.getHelper());
        assertSame(helper, testMockedListObject.getHelper());
    }

    /**
     * Test of add method, of class EventFiringList.
     */
    @Test
    public void testAdd_GenericType() {
        final String no1 = "Non-Object 1";
        final String no2 = "Non-Object 2";
        list.add(no1);
        list.add(no2);

        configureAnswer();
        Object o = new Object();

        boolean add = testObject.add(o);

        assertTrue(add);

        verify(helper).fireAdd(o);
        verifyNoMoreInteractions(helper);

        List<Object> toAdd = Lists.newArrayList(no1, no2, o);
        assertEquals(toAdd, testObject);
        assertEquals(toAdd, list);
    }

    /**
     * Test of add method, of class EventFiringList.
     */
    @Test
    public void testAdd_GenericType_AddFailedNoCall() {
        Object o = new Object();

        when(delegate.add(o)).thenReturn(false);

        boolean add = testMockedListObject.add(o);

        assertFalse(add);

        verifyZeroInteractions(helper);
    }

    /**
     * Test of add method, of class EventFiringList.
     */
    @Test
    public void testAdd_int_GenericType() {
        final String no1 = "Non-Object 1";
        final String no2 = "Non-Object 2";
        list.add(no1);
        list.add(no2);

        configureAnswer();
        Object o = new Object();

        testObject.add(1, o);

        verify(helper).fireAdd(o);
        verifyNoMoreInteractions(helper);

        List<Object> toAdd = Lists.newArrayList(no1, o, no2);

        assertEquals(toAdd, testObject);
        assertEquals(toAdd, list);
    }

    /**
     * Test of addAll method, of class EventFiringList.
     */
    @Test
    public void testAddAll_Collection() {
        configureAnswer();
        List<String> toAdd = fillList(0, 10);

        testObject.addAll(toAdd);

        for (String o : toAdd) {
            inOrder.verify(helper).fireAdd(o);
        }
        verifyNoMoreInteractions(helper);

        assertEquals(toAdd, testObject);
        assertEquals(toAdd, list);
    }

    /**
     * Test of addAll method, of class EventFiringList.
     */
    @Test
    public void testAddAll_int_Collection() {
        final String no1 = "Non-Object 1";
        final String no2 = "Non-Object 2";
        list.add(no1);
        list.add(no2);

        configureAnswer();
        List<String> toAdd = fillList(0, 10);

        testObject.addAll(1, toAdd);

        for (String o : toAdd) {
            inOrder.verify(helper).fireAdd(o);
        }
        verifyNoMoreInteractions(helper);

        toAdd.add(0, no1);
        toAdd.add(no2);
        assertEquals(toAdd, testObject);
        assertEquals(toAdd, list);
    }

    /**
     * Test of iterator method, of class EventFiringList.
     */
    @Test
    public void testIterator() {
        List<String> toAdd = fillList(0, 10);
        list.addAll(toAdd);

        configureAnswer();

        Iterator<Object> retItt = testObject.iterator();

        for (String taS : toAdd) {
            assertTrue(retItt.hasNext());
            Object listS = retItt.next();

            assertEquals(taS, listS);

            retItt.remove();
        }

        assertTrue(testObject.isEmpty());
        assertTrue(list.isEmpty());

        for (String s : toAdd) {
            inOrder.verify(helper).fireRemove(s);
        }
        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of listIterator method, of class EventFiringList.
     */
    @Test
    public void testListIterator_0args() {
        List<String> toAdd = fillList(0, 10);

        list.addAll(toAdd);

        List<Object> toCheck = Lists.newArrayList();

        configureAnswer();

        ListIterator<Object> retItt = testObject.listIterator();

        for (String taS : toAdd) {
            assertTrue(retItt.hasNext());
            Object listS = retItt.next();

            assertEquals("ToCheck: " + toCheck, taS, listS);

            retItt.set(taS + "Objective");
            retItt.remove();
            retItt.add(taS + "ReAdd");
            toCheck.add(taS + "ReAdd");
        }

        assertEquals(testObject, toCheck);
        assertEquals(list, toCheck);

        for (String s : toAdd) {
            inOrder.verify(helper).fireReplace(s, s + "Objective");
            inOrder.verify(helper).fireRemove(s + "Objective");
            inOrder.verify(helper).fireAdd(s + "ReAdd");
        }
        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of listIterator method, of class EventFiringList.
     */
    @Test
    public void testListIterator_int() {
        List<String> toAdd = fillList(0, 10);

        list.addAll(toAdd);

        // List is reversed as we are using previous rather than next.
        toAdd = Lists.reverse(toAdd);

        List<Object> toCheck = Lists.newArrayList();

        configureAnswer();

        ListIterator<Object> retItt = testObject.listIterator(testObject.size());

        for (String taS : toAdd) {
            assertTrue(retItt.hasPrevious());
            Object listS = retItt.previous();

            assertEquals("ToCheck: " + toCheck, taS, listS);

            retItt.set(taS + "Objective");
            retItt.remove();
            retItt.add(taS + "ReAdd");
            toCheck.add(0, taS + "ReAdd");
            retItt.previous();
        }

        assertEquals(toCheck, testObject);
        assertEquals(toCheck, list);

        for (String s : toAdd) {
            inOrder.verify(helper).fireReplace(s, s + "Objective");
            inOrder.verify(helper).fireRemove(s + "Objective");
            inOrder.verify(helper).fireAdd(s + "ReAdd");
        }
        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of retainAll method, of class EventFiringList.
     */
    @Test
    public void testRetainAll() {
        List<String> toAdd = fillList(0, 10);

        list.addAll(toAdd);

        List<String> toRetain = fillList(-1, 5);

        configureAnswer();

        testObject.retainAll(toRetain);

        List<String> removed = Lists.newArrayList(toAdd);
        removed.removeAll(toRetain);

        List<String> retained = Lists.newArrayList(toAdd);
        retained.retainAll(toRetain);

        for (String r : removed) {
            verify(helper).fireRemove(r);
        }
        verifyNoMoreInteractions(helper);

        assertEquals(retained, testObject);
        assertEquals(retained, list);
    }

    /**
     * Test of removeAll method, of class EventFiringList.
     */
    @Test
    public void testRemoveAll() {
        List<String> toAdd = fillList(0, 10);

        list.addAll(toAdd);

        List<String> toRemove = fillList(-1, 5);

        configureAnswer();

        testObject.removeAll(toRemove);

        List<String> removed = Lists.newArrayList(toAdd);
        removed.retainAll(toRemove);

        List<String> retained = Lists.newArrayList(toAdd);
        retained.removeAll(toRemove);

        for (String r : removed) {
            verify(helper).fireRemove(r);
        }
        verifyNoMoreInteractions(helper);

        assertEquals(retained, testObject);
        assertEquals(retained, list);
    }

    /**
     * Test of remove method, of class EventFiringList.
     */
    @Test
    public void testRemove_int() {
        List<String> toAdd = fillList(0, 10);

        list.addAll(toAdd);

        List<Object> toCheck = Lists.newArrayList();

        configureAnswer();

        while (!list.isEmpty()) {
            toCheck.add(testObject.remove(0));
        }

        for (Object s : toCheck) {
            inOrder.verify(helper).fireRemove(s);
        }
        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of remove method, of class EventFiringList.
     */
    @Test
    public void testRemove_int_Fail() {
        when(delegate.remove(0)).thenThrow(IndexOutOfBoundsException.class);

        try {
            testMockedListObject.remove(0);
            fail("No exception thrown.");
        } catch (IndexOutOfBoundsException e) {
            verify(delegate).remove(0);

            verifyNoMoreInteractions(delegate);
            verifyZeroInteractions(helper);
        }
    }

    /**
     * Test of remove method, of class EventFiringList.
     */
    @Test
    public void testRemove_Object() {
        List<String> toAdd = fillList(0, 10);

        list.addAll(toAdd);

        List<Object> toCheck = Lists.newArrayList();

        configureAnswer();

        for (String string : toAdd) {
            testObject.remove(string);
            toCheck.add(string);
        }

        for (Object s : toCheck) {
            inOrder.verify(helper).fireRemove(s);
        }
        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of remove method, of class EventFiringList.
     */
    @Test
    public void testRemove_Object_Fails() {
        List<String> toAdd = fillList(0, 10);
        List<String> toRemove = fillList(100, 110);

        list.addAll(toAdd);

        configureAnswer();

        for (String string : toRemove) {
            assertFalse(testObject.remove(string));
        }

        verifyZeroInteractions(helper);
    }

    /**
     * Test of set method, of class EventFiringList.
     */
    @Test
    public void testSet() {
        List<String> toAdd = fillList(0, 10);

        list.addAll(toAdd);

        configureAnswer();

        for (int i = 0; i < toAdd.size(); i++) {
            String string = toAdd.get(i);
            testObject.set(i, string + "Helloo");
        }

        for (String string : toAdd) {
            inOrder.verify(helper).fireReplace(string, string + "Helloo");
        }

        verifyNoMoreInteractions(helper);
    }

    /**
     * Test of set method, of class EventFiringList.
     */
    @Test
    public void testClear() {
        List<String> toAdd = fillList(0, 10);

        list.addAll(toAdd);

        configureAnswer();

        testObject.clear();

        for (String string : toAdd) {
            // Not in any specific order :(.
            verify(helper).fireRemove(string);
        }

        verifyNoMoreInteractions(helper);
    }

    private class AnswerImpl implements Answer<Void> {

        public AnswerImpl() {
        }
        List<Object> prev = Lists.newArrayList(list);

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
            prev = Lists.newArrayList(list);
            return null;
        }

        private void checkAdd(Object object) {
            List<Object> copy = Lists.newArrayList(list);

            copy.removeAll(prev);

            assertEquals(1, copy.size());
            assertEquals(object, copy.get(0));
        }

        private void checkRemove(Object object) {
            List<Object> copy = Lists.newArrayList(prev);

            copy.removeAll(list);

            assertEquals(1, copy.size());
            assertEquals(object, copy.get(0));
        }

        private void checkReplace(Object ori, Object repl) {
            assertEquals(prev.size(), list.size());
            List<Object> copy = Lists.newArrayList(prev);

            copy.removeAll(list);

            assertEquals(1, copy.size());
            assertEquals(ori, copy.get(0));

            copy = Lists.newArrayList(list);

            copy.removeAll(prev);

            assertEquals(1, copy.size());
            assertEquals(repl, copy.get(0));
        }
    }

}
