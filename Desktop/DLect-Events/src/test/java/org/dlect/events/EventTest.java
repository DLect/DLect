/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import com.google.common.testing.EqualsTester;
import org.dlect.events.TestObject.TestObjecEventID;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
@SuppressWarnings("unchecked")
public class EventTest {

    /**
     * Test of hashCode method, of class Event.
     */
    @Test
    public void testEquality() {
        TestObject o1 = new TestObject();
        TestObject o2 = new TestObject();

        new EqualsTester().addEqualityGroup(
                new Event(o1, TestObjecEventID.ID, 1, 2),
                new Event(o1, TestObjecEventID.ID, 1, 2)
        ).addEqualityGroup(
                new Event(o2, TestObjecEventID.ID, 1, 2)
        ).addEqualityGroup(
                new Event(o1, TestObjecEventID.NAME,  1, 2)
        ).addEqualityGroup(
                new Event(o1, TestObjecEventID.ID, 5, 2)
        ).addEqualityGroup(
                new Event(o1, TestObjecEventID.ID, 1, 3)
        ).addEqualityGroup(
                new Event(o2, TestObjecEventID.SUPPORTED_NUMBERS, 10, 20)
        ).testEquals();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSource() {
        Event e = new Event(getNullTestObject(), TestObjecEventID.ID, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEventID() {
        Event e = new Event(new TestObject(), getNullEventID(), 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EventIDAndObjectDiffer() {
        Event e = new Event(new Object(), TestObjecEventID.ID, 0, 1);
    }

    @Test
    public void testConstructor_SupportsSubtypes() {
        Event e = new Event(new TestObject() {

            @Override
            public String toString() {
                return "Hello World; I'm a sub-class";
            }

        }, TestObjecEventID.ID, 0, 1);
    }

    public void testConstructor_IgnoresSameBeforeAndAfter() {
        Event e = new Event(new TestObject(), TestObjecEventID.ID, 0, 0);
        assertEquals(e.getBefore(), e.getAfter());
    }

    public void testConstructor_IgnoresNullBeforeAndAfter() {
        Event e = new Event(new TestObject(), TestObjecEventID.ID, null, 0);
        assertEquals(null, e.getBefore());
        assertEquals(0, e.getAfter());

        e = new Event(new TestObject(), TestObjecEventID.ID, 0, null);
        assertEquals(0, e.getBefore());
        assertEquals(null, e.getAfter());

    }

    /**
     * Test of getSource method, of class Event.
     */
    @Test
    public void testGetSource() {
        TestObject source = new TestObject();
        Event e = new Event(source, TestObjecEventID.ID, 10, 20);

        assertSame(source, e.getSource());
    }

    /**
     * Test of getEventID method, of class Event.
     */
    @Ignore
    @Test
    public void testGetEventID() {
    }

    /**
     * Test of getBefore method, of class Event.
     */
    @Ignore
    @Test
    public void testGetBefore() {
    }

    /**
     * Test of getAfter method, of class Event.
     */
    @Ignore
    @Test
    public void testGetAfter() {
    }

    private Object getNullTestObject() {
        return null;
    }

    private EventID getNullEventID() {
        return null;
    }

}
