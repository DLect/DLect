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
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ListenableTest {

    private Resettable creatorReset;

    private Listenable testObject;

    private EventAdapter mockAdapter;
    private EventAdapter listenableAdapter;

    @Before
    public void before() throws Exception {
        creatorReset = ObjectHelper.storeStaticStateOf(EventAdapterBuilder.class);
        EventAdapterBuilder.setEventAdapterClass(MockForwardingTestEventAdapter.class);

        testObject = new Listenable();
        listenableAdapter = testObject.getAdapter();

        assertTrue(listenableAdapter instanceof MockForwardingTestEventAdapter);
        assertSame(listenableAdapter, MockForwardingTestEventAdapter.getCurrent());

        mockAdapter = MockForwardingTestEventAdapter.getCurrent().getMock();

        creatorReset.reset();
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
        when(childAdapter.getParentAdapter()).thenReturn(listenableAdapter);

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
        verify(childAdapter).setParentAdapter(listenableAdapter);

        verifyNoMoreInteractions(child, childAdapter);

        verifyZeroInteractions(mockAdapter);
    }
//
//    /**
//     * Test of wrapList method, of class Listenable.
//     */
//    @Test
//    public void testWrapList() {
//        List<String> original = Lists.newArrayList();
//        List<String> l = testObject.wrapList(original, TestListenableEventID.ID);
//
//        assertTrue(l instanceof EventFiringList);
//
//        verifyEventFiringList((EventFiringList<String>) l, original, listenableAdapter);
//
//        l.add("Hello");
//
//        verify(mockAdapter).fireEvent(ListEvent.getAddEvent(testObject, TestListenableEventID.ID, "Hello"));
//    }
//
//    /**
//     * Test of wrapListenableList method, of class Listenable.
//     */
//    @Test
//    public void testWrapListenableList() {
//        List<Listenable> original = Lists.newArrayList();
//        List<Listenable> l = testObject.wrapListenableList(original, TestListenableEventID.ID);
//
//        Listenable mock = new Listenable();
//
//        assertTrue(l instanceof EventFiringList);
//
//        CollectionEventHelper<Listenable> ceh = verifyEventFiringList((EventFiringList<Listenable>) l, original, listenableAdapter);
//
//        assertTrue(ceh instanceof ListenableCollectionEventHelper);
//
//        l.add(mock);
//
//        verify(mockAdapter).fireEvent(ListEvent.getAddEvent(testObject, TestListenableEventID.ID, mock));
//
//        assertNotNull(mock.getAdapter().getParentAdapter());
//    }
//
//    /**
//     * Test of wrapSet method, of class Listenable.
//     */
//    @Test
//    public void testWrapSet() {
//        Set<String> original = Sets.newHashSet();
//        Set<String> l = testObject.wrapSet(original, TestListenableEventID.ID);
//
//        assertTrue(l instanceof EventFiringSet);
//
//        verifyEventFiringSet((EventFiringSet<String>) l, original, listenableAdapter);
//
//        l.add("Hello");
//
//        verify(mockAdapter).fireEvent(ListEvent.getAddEvent(testObject, TestListenableEventID.ID, "Hello"));
//    }
//
//    /**
//     * Test of wrapListenableSet method, of class Listenable.
//     */
//    @Test
//    public void testWrapListenableSet() {
//        Listenable mock = new Listenable();
//
//        Set<Listenable> original = Sets.newHashSet();
//        Set<Listenable> l = testObject.wrapListenableSet(original, TestListenableEventID.ID);
//
//        assertTrue(l instanceof EventFiringSet);
//
//        CollectionEventHelper<Listenable> ceh = verifyEventFiringSet((EventFiringSet<Listenable>) l, original, listenableAdapter);
//
//        assertTrue(ceh instanceof ListenableCollectionEventHelper);
//
//        l.add(mock);
//
//        verify(mockAdapter).fireEvent(ListEvent.getAddEvent(testObject, TestListenableEventID.ID, mock));
//
//        assertNotNull(mock.getAdapter().getParentAdapter());
//    }
//
//    /**
//     * Test of wrapMap method, of class Listenable.
//     */
//    @Test
//    public void testWrapMap() {
//        Map<String, String> original = Maps.newHashMap();
//        Map<String, String> l = testObject.wrapMap(original, TestListenableEventID.ID);
//
//        assertTrue(l instanceof EventFiringMap);
//
//        verifyEventFiringMap((EventFiringMap<String, String>) l, original, listenableAdapter);
//
//        l.put("Hello", "World");
//
//        verify(mockAdapter).fireEvent(ListEvent.getAddEvent(testObject, TestListenableEventID.ID, Maps.immutableEntry("Hello", "World")));
//    }

    /**
     * Test of fireEvent method, of class Listenable.
     */
    @Test
    public void testFireEvent_Null() {
        Event e = null;
        testObject.fireEvent(e);

        verify(mockAdapter).fireEvent(e);
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
