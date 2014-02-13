/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import java.util.ConcurrentModificationException;
import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventID;
import org.dlect.events.ListEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class CollectionEventHelperTest {

    private final Answer<Void> CHECK_LOCKED = new Answer<Void>() {

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            assertTrue(testObject.isLocked());
            return null;
        }
    };
    private final Answer<Void> CHECK_LOCKED_THEN_THROW_EXCEPTION = new Answer<Void>() {

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            CHECK_LOCKED.answer(invocation);
            throw new IllegalStateException();
        }
    };

    @Mock
    private Event event;

    @Mock
    private EventAdapter adapter;

    private EventID eventID;

    private Object source;

    private CollectionEventHelper<Object> testObject;

    @Before
    public void before() {
        eventID = new EventID() {

            @Override
            public Class<?> getAppliedClass() {
                return Object.class;
            }

            @Override
            public String getName() {
                return "Name";
            }

            @Override
            public boolean isUniqueId() {
                return false;
            }
        };
        source = new Object();
        testObject = new CollectionEventHelper<>(source, eventID, adapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEventID() {
        CollectionEventHelper<Object> e = new CollectionEventHelper<>(source, null, adapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSource() {
        CollectionEventHelper<Object> e = new CollectionEventHelper<>(null, eventID, adapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullAdapter() {
        CollectionEventHelper<Object> e = new CollectionEventHelper<>(source, eventID, null);
    }

    /**
     * Test of beginChange method, of class CollectionEventHelper.
     */
    @Test
    public void testBeginChange_MultipleTimes() {
        testObject.beginChange(event);
        try {
            testObject.beginChange(event);
            fail("No exception thrown");
        } catch (ConcurrentModificationException e) {
            // Pass
        }
    }

    /**
     * Test of beginChange method, of class CollectionEventHelper.
     */
    @Test
    public void testBeginChange_NoEventsOnThread() {
        testObject.beginChange(event);
        verifyZeroInteractions(event, adapter);
    }

    /**
     * Test of endChange method, of class CollectionEventHelper.
     */
    @Test
    public void testEndChange_NotBegunFirst() {
        try {
            testObject.endChange();
            fail("No exception thrown");
        } catch (IllegalStateException e) {
            verifyZeroInteractions(adapter);
        }
    }

    @Test
    public void testEndChange_NoErrorOnBegun() {
        testObject.beginChange(event);
        testObject.endChange();
        verifyZeroInteractions(adapter);
    }

    /**
     * Test of fireAdd method, of class CollectionEventHelper.
     */
    @Test
    public void testFireAdd_FiresToAdapter() {
        Object added = new Object();
        ListEvent l = ListEvent.getAddEvent(source, eventID, added);

        testObject.fireAdd(added);

        verify(adapter).fireEvent(l);
    }

    /**
     * Test of fireAdd method, of class CollectionEventHelper.
     */
    @Test
    public void testFireAdd_LocksAndUnlocksOnError() {
        Object added = new Object();
        ListEvent l = ListEvent.getAddEvent(source, eventID, added);

        doAnswer(CHECK_LOCKED_THEN_THROW_EXCEPTION).when(adapter).fireEvent(l);
        try {
            testObject.fireAdd(added);
            fail("No exception thrown");
        } catch (IllegalStateException e) {
            verify(adapter).fireEvent(l);

            assertFalse("Object not unlocked", testObject.isLocked());
        }
    }

    /**
     * Test of fireAdd method, of class CollectionEventHelper.
     */
    @Test
    public void testFireAdd_LocksCorrectly() {
        Object added = new Object();
        ListEvent l = ListEvent.getAddEvent(source, eventID, added);

        doAnswer(CHECK_LOCKED).when(adapter).fireEvent(l);

        testObject.fireAdd(added);

        verify(adapter).fireEvent(l);

        assertFalse("Object not unlocked", testObject.isLocked());
    }

    /**
     * Test of fireRemove method, of class CollectionEventHelper.
     */
    @Test
    public void testFireRemove_FiresToAdapter() {
        Object removed = new Object();
        ListEvent l = ListEvent.getRemoveEvent(source, eventID, removed);

        testObject.fireRemove(removed);

        verify(adapter).fireEvent(l);
    }

    /**
     * Test of fireRemove method, of class CollectionEventHelper.
     */
    @Test
    public void testFireRemove_LocksAndUnlocksOnError() {
        Object removed = new Object();
        ListEvent l = ListEvent.getRemoveEvent(source, eventID, removed);

        doAnswer(CHECK_LOCKED_THEN_THROW_EXCEPTION).when(adapter).fireEvent(l);
        try {
            testObject.fireRemove(removed);
            fail("No exception thrown");
        } catch (IllegalStateException e) {
            verify(adapter).fireEvent(l);

            assertFalse("Object not unlocked", testObject.isLocked());
        }
    }

    /**
     * Test of fireRemove method, of class CollectionEventHelper.
     */
    @Test
    public void testFireRemove_LocksCorrectly() {
        Object removed = new Object();
        ListEvent l = ListEvent.getRemoveEvent(source, eventID, removed);

        doAnswer(CHECK_LOCKED).when(adapter).fireEvent(l);

        testObject.fireRemove(removed);

        verify(adapter).fireEvent(l);

        assertFalse("Object not unlocked", testObject.isLocked());
    }

    /**
     * Test of fireReplace method, of class CollectionEventHelper.
     */
    @Test
    public void testFireReplace_FiresToAdapter() {
        Object removed = new Object();
        Object added = new Object();
        ListEvent l = ListEvent.getReplaceEvent(source, eventID, removed, added);

        testObject.fireReplace(removed, added);

        verify(adapter).fireEvent(l);
    }

    /**
     * Test of fireReplace method, of class CollectionEventHelper.
     */
    @Test
    public void testFireReplace_LocksAndUnlocksOnError() {
        Object removed = new Object();
        Object added = new Object();
        ListEvent l = ListEvent.getReplaceEvent(source, eventID, removed, added);

        doAnswer(CHECK_LOCKED_THEN_THROW_EXCEPTION).when(adapter).fireEvent(l);
        try {
            testObject.fireReplace(removed, added);
            fail("No exception thrown");
        } catch (IllegalStateException e) {
            verify(adapter).fireEvent(l);

            assertFalse("Object not unlocked", testObject.isLocked());
        }
    }

    /**
     * Test of fireReplace method, of class CollectionEventHelper.
     */
    @Test
    public void testFireReplace_LocksCorrectly() {
        Object removed = new Object();
        Object added = new Object();
        ListEvent l = ListEvent.getReplaceEvent(source, eventID, removed, added);

        doAnswer(CHECK_LOCKED).when(adapter).fireEvent(l);

        testObject.fireReplace(removed, added);

        verify(adapter).fireEvent(l);

        assertFalse("Object not unlocked", testObject.isLocked());
    }

    /**
     * Test of isLocked method, of class CollectionEventHelper.
     */
    @Test
    public void testIsLocked_NoLock() {
        assertFalse(testObject.isLocked());
        verifyZeroInteractions(adapter);
    }

    /**
     * Test of isLocked method, of class CollectionEventHelper.
     */
    @Test
    public void testIsLocked_AfterLockMethod() {
        testObject.beginChange(event);
        assertTrue(testObject.isLocked());
    }

    /**
     * Test of isLocked method, of class CollectionEventHelper.
     */
    @Test
    public void testIsLocked_AfterSecondLockMethod() {
        testObject.beginChange(event);
        try {
            testObject.beginChange(event);
        } catch (ConcurrentModificationException e) {
            assertTrue(testObject.isLocked());
        }
    }

}
