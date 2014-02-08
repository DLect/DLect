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
    @Test
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
    @Test
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
    @Test
    public void testAddListener_NonExistingAllListenerNullClassArray() {
        EventListener l = new TestEventListener();

        boolean addListener = nonMockedTestObject.addListener(l, (Class[]) null);

        assertTrue(addListener);

        assertTrue(nonMockedScel.isEmpty());
        assertTrue(nonMockedAcl.size() == 1);
        // First element.
        assertTrue(nonMockedAcl.iterator().next() == l);
    }

    /**
     * Test of addListener method, of class BaseEventAdapter.
     */
    @Test
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
     * Test of addListener method, of class BaseEventAdapter.
     */
    @Test
    public void testAddListener_AddFilteredListener() {
        EventListener l = new TestEventListener();

        boolean addListener = nonMockedTestObject.addListener(l, Object.class);

        // The listener was added.
        assertTrue(addListener);

        assertTrue(nonMockedScel.size() == 1);

        assertTrue(nonMockedScel.containsEntry(Object.class, l));

        // The adapter added it to the list.
        assertTrue(nonMockedAcl.isEmpty());

    }

    /**
     * Test of addListener method, of class BaseEventAdapter.
     */
    @Test
    public void testAddListener_AddMultiFilteredListener() {
        EventListener l = new TestEventListener();

        boolean addListener = nonMockedTestObject.addListener(l, Object.class, Number.class, Integer.class);

        // The listener was added.
        assertTrue(addListener);

        assertTrue(nonMockedScel.size() == 3);

        assertTrue(nonMockedScel.containsEntry(Object.class, l));
        assertTrue(nonMockedScel.containsEntry(Number.class, l));
        assertTrue(nonMockedScel.containsEntry(Integer.class, l));

        assertTrue(nonMockedAcl.isEmpty());
    }

    /**
     * Test of addListener method, of class BaseEventAdapter.
     */
    @Test
    public void testAddListener_AddExistingFilteredListener() {
        EventListener l = new TestEventListener();

        nonMockedScel.put(Object.class, l);

        boolean addListener = nonMockedTestObject.addListener(l, Object.class);

        // The listener was added.
        assertFalse(addListener);

        assertTrue(nonMockedScel.size() == 1);

        assertTrue(nonMockedScel.containsEntry(Object.class, l));

        assertTrue(nonMockedAcl.isEmpty());
    }

    /**
     * Test of addListener method, of class BaseEventAdapter.
     */
    @Test
    public void testAddListener_AddExistingMulitFilteredListener() {
        EventListener l = new TestEventListener();

        nonMockedScel.put(Object.class, l);

        // Object.class already exists but integer does not.
        boolean addListener = nonMockedTestObject.addListener(l, Object.class, Integer.class);

        assertTrue(addListener);

        assertTrue(nonMockedScel.size() == 2);

        assertTrue(nonMockedScel.containsEntry(Object.class, l));
        assertTrue(nonMockedScel.containsEntry(Integer.class, l));

        assertTrue(nonMockedAcl.isEmpty());
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
    @Test
    public void testGetParentAdapter_EnsureDefaultIsNull() {
        assertNull("Default parent is not null", nonMockedTestObject.getParentAdapter());
    }

    /**
     * Test of setParentAdapter method, of class BaseEventAdapter.
     */
    @Test
    public void testSetParentAdapter_NullAdapter() {
        nonMockedTestObject.setParentAdapter(null);

        assertNull(nonMockedTestObject.getParentAdapter());
    }

    /**
     * Test of setParentAdapter method, of class BaseEventAdapter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetParentAdapter_CyclicAdapter() {
        nonMockedTestObject.setParentAdapter(nonMockedTestObject);
    }

    /**
     * Test of setParentAdapter method, of class BaseEventAdapter.
     */
    @Test
    public void testSetParentAdapter_CyclicAdapter2() {
        BaseEventAdapter e2 = mock(BaseEventAdapter.class);
        when(e2.getParentAdapter()).thenReturn(nonMockedTestObject);
        try {
            nonMockedTestObject.setParentAdapter(e2);
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            verify(e2).getParentAdapter();
            verifyNoMoreInteractions(e2);
        }
    }

    /**
     * Test of setParentAdapter method, of class BaseEventAdapter.
     */
    @Test
    public void testSetParentAdapter_ValidParent() {
        BaseEventAdapter e2 = mock(BaseEventAdapter.class);
        when(e2.getParentAdapter()).thenReturn(null); // No parent.
        nonMockedTestObject.setParentAdapter(e2);

        assertEquals("Wrong parent", e2, nonMockedTestObject.getParentAdapter());

        verify(e2).getParentAdapter();
        verifyNoMoreInteractions(e2);
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
