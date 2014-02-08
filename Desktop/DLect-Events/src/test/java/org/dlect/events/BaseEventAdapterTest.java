/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Set;
import org.dlect.events.testListener.TestEventListener;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class BaseEventAdapterTest {

    @Mock
    private Set<EventListener> mockedAcl;
    @Mock
    private Multimap<Class<?>, EventListener> mockedScel;
    @InjectMocks
    private BaseEventAdapter mockedTestObject;

    private Set<EventListener> nonMockedAcl;
    private Multimap<Class<?>, EventListener> nonMockedScel;
    private BaseEventAdapter nonMockedTestObject;

    private BaseEventAdapter defaultTestObject;

    @Before
    public void before() throws Exception {
        nonMockedAcl = Sets.newHashSet();
        nonMockedScel = HashMultimap.create();
        nonMockedTestObject = new BaseEventAdapter(nonMockedAcl, nonMockedScel);
        defaultTestObject = new BaseEventAdapter();
    }

    /**
     * Test of addListener method, of class BaseEventAdapter.
     *
     * @throws RuntimeException Must throw a runtime exception for a null adapter. Can be either NullPointer or
     *                          IllegalArgument.
     */
    @Test(expected = RuntimeException.class)
    public void testAddListener_NullListener() {
        // Using method to avoid warnings about null listeners.
        EventListener l = getNullListener();
        mockedTestObject.addListener(l);
    }

    /**
     * Test of addListener method, of class BaseEventAdapter.
     */
    public void testAddListener_ExistingListener() {
        EventListener l = new TestEventListener();

        when(mockedAcl.contains(l)).thenReturn(true);

        boolean addListener = mockedTestObject.addListener(l);

        assertFalse(addListener);
        verify(mockedAcl).contains(l);
        verifyNoMoreInteractions(mockedAcl);
    }

    /**
     * Test of addListener method, of class BaseEventAdapter.
     */
    public void testAddListener_NonExistingAllListener() {
        EventListener l = new TestEventListener();

        boolean addListener = nonMockedTestObject.addListener(l);

        assertTrue(addListener);

        assertTrue(nonMockedScel.isEmpty());
        assertTrue(nonMockedAcl.size() == 1);
        // First element.
        assertTrue(nonMockedAcl.iterator().next() == l);
    }

    /**
     * Test of addListener method, of class BaseEventAdapter.
     */
    public void testAddListener_AddExistingFilterListenerNowAll() {
        EventListener l = new TestEventListener();

        nonMockedScel.put(Object.class, l);

        boolean addListener = nonMockedTestObject.addListener(l);

        // The listener was added.
        assertTrue(addListener);

        // The adapter saved time by removing the new listener from SCEL.
        assertTrue(nonMockedScel.isEmpty());
        // The adapter added it to the list.
        assertTrue(nonMockedAcl.size() == 1);
        // First element.
        assertTrue(nonMockedAcl.iterator().next() == l);
    }

    /**
     * Test of fireEvent method, of class BaseEventAdapter.
     */
    @Ignore
    @Test
    public void testFireEvent() {
    }

    /**
     * Test of getParentAdapter method, of class BaseEventAdapter.
     */
    @Ignore
    @Test
    public void testGetParentAdapter() {
    }

    /**
     * Test of setParentAdapter method, of class BaseEventAdapter.
     */
    @Ignore
    @Test
    public void testSetParentAdapter() {
    }

    /**
     * Test of removeListener method, of class BaseEventAdapter.
     */
    @Ignore
    @Test
    public void testRemoveListener() {
    }

    private EventListener getNullListener() {
        return null;
    }
}
