/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

    /**
     * Test of delegate method, of class EventFiringList.
     */
    @Test
    public void testDelegate() {
        assertSame(list, testObject.delegate());
        assertSame(delegate, testMockedListObject.delegate());
    }

    /**
     * Test of add method, of class EventFiringList.
     */
    @Test
    public void testAdd_GenericType() {
        Object o = new Object();

        testMockedListObject.add(o);
        inOrder.verify(delegate).add(o);
        inOrder.verify(helper).fireAdd(o);
    }

    /**
     * Test of add method, of class EventFiringList.
     */
    @Test
    public void testAdd_int_GenericType() {
        Object o = new Object();

        testMockedListObject.add(0, o);
        inOrder.verify(delegate).add(0, o);
        inOrder.verify(helper).fireAdd(o);
    }

    /**
     * Test of addAll method, of class EventFiringList.
     */
    @Test
    public void testAddAll_Collection() {
        List<String> toAdd = Lists.newArrayList();

        for (int i = 0; i < 10; i++) {
            toAdd.add("Object No. " + i);
        }

        testMockedListObject.addAll(delegate);
        for (String o : toAdd) {
            inOrder.verify(delegate).add(o);
            inOrder.verify(helper).fireAdd(o);
        }
    }

    /**
     * Test of addAll method, of class EventFiringList.
     */
    @Ignore
    @Test
    public void testAddAll_int_Collection() {
        // TODO write this test considering that it uses an implementation in lists
    }

    /**
     * Test of iterator method, of class EventFiringList.
     */
    @Test
    public void testIterator() {
        // TODO Another complicated one.
    }

    /**
     * Test of listIterator method, of class EventFiringList.
     */
    @Test
    public void testListIterator_0args() {
        // TODO Another complicated one.
    }

    /**
     * Test of listIterator method, of class EventFiringList.
     */
    @Test
    public void testListIterator_int() {
        // TODO Another complicated one.
    }

    /**
     * Test of retainAll method, of class EventFiringList.
     */
    @Test
    public void testRetainAll() {
        // TODO Another complicated one.
    }

    /**
     * Test of removeAll method, of class EventFiringList.
     */
    @Test
    public void testRemoveAll() {
        // TODO Another complicated one.
    }

    /**
     * Test of remove method, of class EventFiringList.
     */
    @Test
    public void testRemove_int() {
        // TODO
    }

    /**
     * Test of remove method, of class EventFiringList.
     */
    @Test
    public void testRemove_Object() {
        // TODO
    }

    /**
     * Test of set method, of class EventFiringList.
     */
    @Test
    public void testSet() {
        // TODO
    }

}
