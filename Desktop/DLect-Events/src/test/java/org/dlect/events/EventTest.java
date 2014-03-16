/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import com.google.common.testing.EqualsTester;
import org.dlect.events.TestObject.TestObjectEventID;
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
                new Event(o1, TestObjectEventID.ID, 1, 2),
                new Event(o1, TestObjectEventID.ID, 1, 2)
        ).addEqualityGroup(
                new Event(o2, TestObjectEventID.ID, 1, 2)
        ).addEqualityGroup(
                new Event(o1, TestObjectEventID.NAME, 1, 2)
        ).addEqualityGroup(
                new Event(o1, TestObjectEventID.ID, 5, 2)
        ).addEqualityGroup(
                new Event(o1, TestObjectEventID.ID, 1, 3)
        ).addEqualityGroup(
                new Event(o2, TestObjectEventID.SUPPORTED_NUMBERS, 10, 20)
        ).testEquals();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSource() {
        Event e = new Event(getNullTestObject(), TestObjectEventID.ID, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEventID() {
        Event e = new Event(new TestObject(), getNullEventID(), 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EventIDAndObjectDiffer() {
        Event e = new Event(new Object(), TestObjectEventID.ID, 0, 1);
    }

    @Test
    public void testConstructor_SupportsSubtypes() {
        Event e = new Event(new TestObject() {

            @Override
            public String toString() {
                return "Hello World; I'm a sub-class";
            }

        }, TestObjectEventID.ID, 0, 1);
    }

    public void testConstructor_IgnoresSameBeforeAndAfter() {
        Event e = new Event(new TestObject(), TestObjectEventID.ID, 0, 0);
        assertEquals(e.getBefore(), e.getAfter());
    }

    public void testConstructor_IgnoresNullBeforeAndAfter() {
        Event e = new Event(new TestObject(), TestObjectEventID.ID, null, 0);
        assertEquals(null, e.getBefore());
        assertEquals(0, e.getAfter());

        e = new Event(new TestObject(), TestObjectEventID.ID, 0, null);
        assertEquals(0, e.getBefore());
        assertEquals(null, e.getAfter());

    }

    /**
     * Test of getSource method, of class Event.
     */
    @Test
    public void testGetSource() {
        TestObject source = new TestObject();
        Event e = new Event(source, TestObjectEventID.ID, 10, 20);

        assertSame(source, e.getSource());
    }

    /**
     * Test of getEventID method, of class Event.
     */
    @Test
    public void testGetEventID() {
        TestObject source = new TestObject();
        EventID eid = new EventID() {

            @Override
            public Class<?> getAppliedClass() {
                return TestObject.class;
            }

            @Override
            public String getName() {
                return "Name";
            }
        };
        Event e = new Event(source, eid, 10, 20);

        assertSame(eid, e.getEventID());
    }

    /**
     * Test of getEventID method, of class Event.
     */
    @Test
    public void testGetSourceClass() {
        TestObject source = new TestObject();
        EventID eid = new EventID() {

            @Override
            public Class<?> getAppliedClass() {
                return Object.class;
            }

            @Override
            public String getName() {
                return "Name";
            }
        };
        Event e = new Event(source, eid, 10, 20);

        // Must use eventID.getAppliedClass(); not source.getClass();
        assertEquals(Object.class, e.getSourceClass());
    }

    /**
     * Test of getBefore method, of class Event.
     */
    @Test
    public void testGetBefore() {
        TestObject source = new TestObject();
        Object before = new Object();
        Event e = new Event(source, TestObjectEventID.ID, before, 20);

        // Must use eventID.getAppliedClass(); not source.getClass();
        assertSame(before, e.getBefore());
    }

    /**
     * Test of getAfter method, of class Event.
     */
    @Test
    public void testGetAfter() {
        TestObject source = new TestObject();
        Object after = new Object();
        Event e = new Event(source, TestObjectEventID.ID, 10, after);

        // Must use eventID.getAppliedClass(); not source.getClass();
        assertSame(after, e.getAfter());
    }

    private Object getNullTestObject() {
        return null;
    }

    private EventID getNullEventID() {
        return null;
    }

}
