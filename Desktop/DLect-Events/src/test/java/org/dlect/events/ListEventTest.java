/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import com.google.common.testing.EqualsTester;
import org.dlect.events.TestObject.TestObjecEventID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class ListEventTest {

    private Object source = new Object();
    private EventID eventID = new EventID() {

        @Override
        public Class<?> getAppliedClass() {
            return Object.class;
        }

        @Override
        public String getName() {
            return "name";
        }

        @Override
        public boolean isUniqueId() {
            return false;
        }
    };

    @Test
    public void testConstructor_NoExceptionOnValid() {
        ListEvent e = new ListEvent(source, eventID, ListEventType.REPLACED, "Old", "New");

        assertEquals(ListEventType.REPLACED, e.getListEventType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_ExceptionOnNullType() {
        ListEvent e = new ListEvent(source, eventID, null, "Old", "New");
    }

    /**
     * Test of hashCode method, of class Event.
     */
    @Test
    public void testEquality() {
        TestObject o1 = new TestObject();
        TestObject o2 = new TestObject();

        new EqualsTester().addEqualityGroup(
                new ListEvent(o1, TestObjecEventID.ID, ListEventType.REPLACED, 1, 2),
                new ListEvent(o1, TestObjecEventID.ID, ListEventType.REPLACED, 1, 2)
        ).addEqualityGroup(
                new ListEvent(o2, TestObjecEventID.ID, ListEventType.REPLACED, 1, 2)
        ).addEqualityGroup(
                new ListEvent(o1, TestObjecEventID.NAME, ListEventType.REPLACED, 1, 2)
        ).addEqualityGroup(
                new ListEvent(o1, TestObjecEventID.ID, ListEventType.ADDED, 1, 2)
        ).addEqualityGroup(
                new ListEvent(o1, TestObjecEventID.ID, ListEventType.REPLACED, 3, 2)
        ).addEqualityGroup(
                new ListEvent(o1, TestObjecEventID.ID, ListEventType.REPLACED, 1, 3)
        ).addEqualityGroup(
                new ListEvent(new TestObject(), TestObjecEventID.SUPPORTED_NUMBERS, ListEventType.REMOVED, 5, 12)
        ).testEquals();
    }

    /**
     * Test of getAddEvent method, of class ListEvent.
     */
    @Test
    public void testGetAddEvent_Valid() {
        Object o = new Object();
        ListEvent evt = ListEvent.getAddEvent(source, eventID, o);

        assertEquals(ListEventType.ADDED, evt.getListEventType());
        assertEquals(source, evt.getSource());
        assertEquals(eventID, evt.getEventID());
        assertEquals(null, evt.getBefore());
        assertEquals(o, evt.getAfter());
    }

    /**
     * Test of getRemoveEvent method, of class ListEvent.
     */
    @Test
    public void testGetRemoveEvent() {
        Object o = new Object();
        ListEvent evt = ListEvent.getRemoveEvent(source, eventID, o);

        assertEquals(ListEventType.REMOVED, evt.getListEventType());
        assertEquals(source, evt.getSource());
        assertEquals(eventID, evt.getEventID());
        assertEquals(o, evt.getBefore());
        assertEquals(null, evt.getAfter());
    }

    /**
     * Test of getReplaceEvent method, of class ListEvent.
     */
    @Test
    public void testGetReplaceEvent() {
        Object orig = new Object();
        Object repl = new Object();
        ListEvent evt = ListEvent.getReplaceEvent(source, eventID, orig, repl);

        assertEquals(ListEventType.REPLACED, evt.getListEventType());
        assertEquals(source, evt.getSource());
        assertEquals(eventID, evt.getEventID());
        assertEquals(orig, evt.getBefore());
        assertEquals(repl, evt.getAfter());
    }

}
