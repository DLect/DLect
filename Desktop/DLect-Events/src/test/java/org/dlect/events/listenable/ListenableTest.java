/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.listenable;

import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventAdapterBuilder;
import org.dlect.events.EventListener;
import org.dlect.test.Resettable;
import org.dlect.test.helper.ObjectHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@SuppressWarnings("unchecked")
public class ListenableTest {

    private Resettable creatorReset;

    private Listenable testObject;

    private EventAdapter mockAdapter;

    @Before
    public void before() throws Exception {
        creatorReset = ObjectHelper.storeStaticStateOf(EventAdapterBuilder.class);
        EventAdapterBuilder.setEventAdapterClass(MockForwardingTestEventAdapter.class);
        testObject = new Listenable();

        assertTrue(testObject.getAdapter() instanceof MockForwardingTestEventAdapter);
        assertSame(testObject.getAdapter(), MockForwardingTestEventAdapter.getCurrent());

        mockAdapter = MockForwardingTestEventAdapter.getCurrent().getMock();
    }

    @After
    public void after() throws Exception {
        creatorReset.reset();
    }

    /**
     * Test of getAdapter method, of class Listenable.
     */
    @Test
    public void testGetAdapter() {
        assertNotNull(testObject.getAdapter());
    }

    /**
     * Test of addListener method, of class Listenable.
     */
    @Test
    public void testAddListener_EmptyClassList() {
        EventListener l = mock(EventListener.class);

        when(mockAdapter.addListener(l)).thenReturn(false);

        assertFalse(testObject.addListener(l));

        verify(mockAdapter).addListener(l);
    }

    /**
     * Test of addListener method, of class Listenable.
     */
    @Test
    public void testAddListener_ClassList() {
        Class<?>[] c = {String.class};

        EventListener l = mock(EventListener.class);

        when(mockAdapter.addListener(l, c)).thenReturn(false);

        assertFalse(testObject.addListener(l, c));

        verify(mockAdapter).addListener(l, c);
    }

    /**
     * Test of removeListener method, of class Listenable.
     */
    @Test
    public void testRemoveListener() {
        EventListener l = mock(EventListener.class);

        when(mockAdapter.removeListener(l)).thenReturn(false);

        assertFalse(testObject.removeListener(l));

        verify(mockAdapter).removeListener(l);
    }

    /**
     * Test of addChild method, of class Listenable.
     */
    @Test
    public void testAddChild_NoChild() {
        testObject.addChild();

        verifyZeroInteractions(mockAdapter);
    }

    /**
     * Test of addChild method, of class Listenable.
     */
    @Test
    public void testAddChild_BadChild() {
        Listenable child = mock(Listenable.class);

        EventAdapter childAdapter = mock(EventAdapter.class);
        EventAdapter childParentAdapter = mock(EventAdapter.class);

        when(child.getAdapter()).thenReturn(childAdapter);
        when(childAdapter.getParentAdapter()).thenReturn(childParentAdapter);
        try {
            testObject.addChild(child);
            fail("No exception");
        } catch (IllegalStateException e) {
            verify(child).getAdapter();
            verify(childAdapter).getParentAdapter();

            verifyNoMoreInteractions(child, childAdapter, childParentAdapter);

            verifyZeroInteractions(mockAdapter);
        }
    }

    /**
     * Test of addChild method, of class Listenable.
     */
    @Test
    public void testAddChild_AlreadySetParent() {
        Listenable child = mock(Listenable.class);

        EventAdapter childAdapter = mock(EventAdapter.class);

        when(child.getAdapter()).thenReturn(childAdapter);
        when(childAdapter.getParentAdapter()).thenReturn(testObject.getAdapter());

        testObject.addChild(child);

        verify(child).getAdapter();
        verify(childAdapter).getParentAdapter();

        verifyNoMoreInteractions(child, childAdapter);

        verifyZeroInteractions(mockAdapter);
    }

    /**
     * Test of addChild method, of class Listenable.
     */
    @Test
    public void testAddChild_NoParent() {
        Listenable child = mock(Listenable.class);

        EventAdapter childAdapter = mock(EventAdapter.class);

        when(child.getAdapter()).thenReturn(childAdapter);
        when(childAdapter.getParentAdapter()).thenReturn(null);

        testObject.addChild(child);

        verify(child).getAdapter();
        verify(childAdapter).getParentAdapter();
        verify(childAdapter).setParentAdapter(testObject.getAdapter());

        verifyNoMoreInteractions(child, childAdapter);

        verifyZeroInteractions(mockAdapter);
    }

    /**
     * Test of wrapList method, of class Listenable.
     */
    @Test
    public void testWrapList() {
    }

    /**
     * Test of wrapListenableList method, of class Listenable.
     */
    @Test
    public void testWrapListenableList() {
    }

    /**
     * Test of wrapSet method, of class Listenable.
     */
    @Test
    public void testWrapSet() {
    }

    /**
     * Test of wrapListenableSet method, of class Listenable.
     */
    @Test
    public void testWrapListenableSet() {
    }

    /**
     * Test of wrapMap method, of class Listenable.
     */
    @Test
    public void testWrapMap() {
    }

    /**
     * Test of fireEvent method, of class Listenable.
     */
    @Test
    public void testFireEvent_Null() {

        testObject.fireEvent(null);

        verify(mockAdapter).fireEvent(null);
    }

    /**
     * Test of fireEvent method, of class Listenable.
     */
    @Test
    public void testFireEvent_Event() {
        Event e = mock(Event.class);

        testObject.fireEvent(e);

        verify(mockAdapter).fireEvent(e);
    }

}
