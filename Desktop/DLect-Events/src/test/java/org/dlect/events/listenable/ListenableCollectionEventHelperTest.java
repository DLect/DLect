/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.listenable;

import org.dlect.events.EventAdapter;
import org.dlect.events.ListEvent;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class ListenableCollectionEventHelperTest {

    @Mock
    private EventAdapter adapter;

    private TestListenableObject source;

    private TestListenableEventID eventID = TestListenableEventID.ID;

    private ListenableCollectionEventHelper<TestListenableObject> testObject;

    @Before
    public void before() {
        source = new TestListenableObject();

        testObject = new ListenableCollectionEventHelper<>(source, eventID, adapter);
    }

    /**
     * Test of addListenable method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testAddListenable_NullListenable() {
        testObject.addListenable(null);

        verifyZeroInteractions(adapter);
    }

    /**
     * Test of addListenable method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testAddListenable_NullEventAdapter() {
        TestListenableObject o = mock(TestListenableObject.class);
        when(o.getAdapter()).thenReturn(null);

        try {
            testObject.addListenable(o);
            fail("No exception");
        } catch (IllegalStateException e) {
            verify(o).getAdapter();
        }

        verifyZeroInteractions(adapter);
    }

    /**
     * Test of addListenable method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testAddListenable_NullParent() {
        EventAdapter oAdapter = mock(EventAdapter.class);
        when(oAdapter.getParentAdapter()).thenReturn(null);

        TestListenableObject o = mock(TestListenableObject.class);
        when(o.getAdapter()).thenReturn(oAdapter);

        testObject.addListenable(o);

        verify(o).getAdapter();
        verify(oAdapter).getParentAdapter();
        verify(oAdapter).setParentAdapter(adapter);

        verifyNoMoreInteractions(o, oAdapter);
        verifyZeroInteractions(adapter);
    }

    /**
     * Test of addListenable method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testAddListenable_NonNullParent() {
        EventAdapter oParent = mock(EventAdapter.class);

        EventAdapter oAdapter = mock(EventAdapter.class);
        when(oAdapter.getParentAdapter()).thenReturn(oParent);

        TestListenableObject o = mock(TestListenableObject.class);
        when(o.getAdapter()).thenReturn(oAdapter);
        try {
            testObject.addListenable(o);
            fail("No exception");
        } catch (IllegalStateException e) {
            verify(o).getAdapter();
            verify(oAdapter).getParentAdapter();

            verifyNoMoreInteractions(o, oAdapter, oParent);

            verifyZeroInteractions(adapter);
        }
    }

    /**
     * Test of removeListenable method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testRemoveListenable_NullObject() {
        testObject.removeListenable(null);

        verifyZeroInteractions(adapter);
    }

    /**
     * Test of removeListenable method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testRemoveListenable_NullEventAdapter() {
        TestListenableObject o = mock(TestListenableObject.class);
        when(o.getAdapter()).thenReturn(null);

        try {
            testObject.removeListenable(o);
            fail("No exception");
        } catch (IllegalStateException e) {
            verify(o).getAdapter();
        }

        verifyZeroInteractions(adapter);
    }

    /**
     * Test of addListenable method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testRemoveListenable_CorrectParent() {
        EventAdapter oAdapter = mock(EventAdapter.class);
        when(oAdapter.getParentAdapter()).thenReturn(adapter);

        TestListenableObject o = mock(TestListenableObject.class);
        when(o.getAdapter()).thenReturn(oAdapter);

        testObject.removeListenable(o);

        verify(o).getAdapter();
        verify(oAdapter).getParentAdapter();
        verify(oAdapter).setParentAdapter(null);

        verifyNoMoreInteractions(o, oAdapter);
        verifyZeroInteractions(adapter);
    }

    /**
     * Test of addListenable method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testRemoveListenable_IncorrectParent() {
        EventAdapter oParent = mock(EventAdapter.class);

        EventAdapter oAdapter = mock(EventAdapter.class);
        when(oAdapter.getParentAdapter()).thenReturn(oParent);

        TestListenableObject o = mock(TestListenableObject.class);
        when(o.getAdapter()).thenReturn(oAdapter);
        try {
            testObject.removeListenable(o);
            fail("No exception");
        } catch (IllegalStateException e) {
            verify(o).getAdapter();
            verify(oAdapter).getParentAdapter();

            verifyNoMoreInteractions(o, oAdapter, oParent);

            verifyZeroInteractions(adapter);
        }
    }

    /**
     * Test of fireAdd method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testFireAdd_NullObject() {
        TestListenableObject eventObj = null;

        testObject.fireAdd(eventObj);

        verify(adapter).fireEvent(ListEvent.getAddEvent(source, eventID, eventObj));
    }

    /**
     * Test of fireAdd method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testFireAdd_ListenableObject() {
        EventAdapter oAdapter = mock(EventAdapter.class);
        when(oAdapter.getParentAdapter()).thenReturn(null);

        TestListenableObject eventObj = mock(TestListenableObject.class);
        when(eventObj.getAdapter()).thenReturn(oAdapter);

        testObject.fireAdd(eventObj);

        verify(oAdapter).setParentAdapter(adapter);

        verify(adapter).fireEvent(ListEvent.getAddEvent(source, eventID, eventObj));
    }

    /**
     * Test of fireRemove method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testFireRemove_Null() {
        TestListenableObject eventObj = null;

        testObject.fireRemove(eventObj);

        verify(adapter).fireEvent(ListEvent.getRemoveEvent(source, eventID, eventObj));
    }

    /**
     * Test of fireAdd method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testFireRemove_ListenableObject() {
        EventAdapter oAdapter = mock(EventAdapter.class);
        when(oAdapter.getParentAdapter()).thenReturn(adapter);

        TestListenableObject eventObj = mock(TestListenableObject.class);
        when(eventObj.getAdapter()).thenReturn(oAdapter);

        testObject.fireRemove(eventObj);

        verify(oAdapter).setParentAdapter(null);
        verify(adapter).fireEvent(ListEvent.getRemoveEvent(source, eventID, eventObj));
    }

    /**
     * Test of fireReplace method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testFireReplace_Null() {
        TestListenableObject original = null;
        TestListenableObject added = null;

        testObject.fireReplace(original, added);

        verify(adapter).fireEvent(ListEvent.getReplaceEvent(source, eventID, original, added));
    }

    /**
     * Test of fireReplace method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testFireReplace_AddedNotNull() {
        EventAdapter addedAdapter = mock(EventAdapter.class);
        when(addedAdapter.getParentAdapter()).thenReturn(null);

        TestListenableObject added = mock(TestListenableObject.class);
        when(added.getAdapter()).thenReturn(addedAdapter);

        TestListenableObject original = null;

        testObject.fireReplace(original, added);

        verify(addedAdapter).setParentAdapter(adapter);
        verify(adapter).fireEvent(ListEvent.getReplaceEvent(source, eventID, original, added));
    }

    /**
     * Test of fireReplace method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testFireReplace_OriginalNotNull() {
        EventAdapter originalAdapter = mock(EventAdapter.class);
        when(originalAdapter.getParentAdapter()).thenReturn(adapter);

        TestListenableObject original = mock(TestListenableObject.class);
        when(original.getAdapter()).thenReturn(originalAdapter);

        TestListenableObject added = null;

        testObject.fireReplace(original, added);

        verify(originalAdapter).setParentAdapter(null);
        verify(adapter).fireEvent(ListEvent.getReplaceEvent(source, eventID, original, added));
    }

    /**
     * Test of fireReplace method, of class ListenableCollectionEventHelper.
     */
    @Test
    public void testFireReplace_BothNotNull() {
        EventAdapter originalAdapter = mock(EventAdapter.class);
        when(originalAdapter.getParentAdapter()).thenReturn(adapter);

        TestListenableObject original = mock(TestListenableObject.class);
        when(original.getAdapter()).thenReturn(originalAdapter);

        EventAdapter addedAdapter = mock(EventAdapter.class);
        when(addedAdapter.getParentAdapter()).thenReturn(null);

        TestListenableObject added = mock(TestListenableObject.class);
        when(added.getAdapter()).thenReturn(addedAdapter);

        testObject.fireReplace(original, added);

        verify(originalAdapter).setParentAdapter(null);
        verify(addedAdapter).setParentAdapter(adapter);
        verify(adapter).fireEvent(ListEvent.getReplaceEvent(source, eventID, original, added));
    }

}
