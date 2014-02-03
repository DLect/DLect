/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lee.echo360.model;

import java.beans.PropertyChangeListener;
import java.util.SortedSet;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author lee
 */
@Ignore
public class BlackboardTest {

    public BlackboardTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addPropertyChangeListener method, of class Blackboard.
     */
    @Test
    public void testAddPropertyChangeListener_PropertyChangeListener() {
        System.out.println("addPropertyChangeListener");
        PropertyChangeListener listener = null;
        Blackboard instance = new Blackboard();
        instance.addPropertyChangeListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removePropertyChangeListener method, of class Blackboard.
     */
    @Test
    public void testRemovePropertyChangeListener_PropertyChangeListener() {
        System.out.println("removePropertyChangeListener");
        PropertyChangeListener listener = null;
        Blackboard instance = new Blackboard();
        instance.removePropertyChangeListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPropertyChangeListener method, of class Blackboard.
     */
    @Test
    public void testAddPropertyChangeListener_String_PropertyChangeListener() {
        System.out.println("addPropertyChangeListener");
        String propertyName = "";
        PropertyChangeListener listener = null;
        Blackboard instance = new Blackboard();
        instance.addPropertyChangeListener(propertyName, listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removePropertyChangeListener method, of class Blackboard.
     */
    @Test
    public void testRemovePropertyChangeListener_String_PropertyChangeListener() {
        System.out.println("removePropertyChangeListener");
        String propertyName = "";
        PropertyChangeListener listener = null;
        Blackboard instance = new Blackboard();
        instance.removePropertyChangeListener(propertyName, listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPassword method, of class Blackboard.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        Blackboard instance = new Blackboard();
        String expResult = "";
        String result = instance.getPassword();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addSubject method, of class Blackboard.
     */
    @Test
    public void testAddSubject() {
        System.out.println("addSubject");
        Subject c = null;
        Blackboard instance = new Blackboard();
        instance.addSubject(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearSubjects method, of class Blackboard.
     */
    @Test
    public void testClearSubjects() {
        System.out.println("clearSubjects");
        Blackboard instance = new Blackboard();
        instance.clearSubjects();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubjects method, of class Blackboard.
     */
    @Test
    public void testGetSubjects() {
        System.out.println("getSubjects");
        Blackboard instance = new Blackboard();
        SortedSet expResult = null;
        SortedSet result = instance.getSubjects();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUsername method, of class Blackboard.
     */
    @Test
    public void testSetUsername() {
        System.out.println("setUsername");
        String username = "";
        Blackboard instance = new Blackboard();
        instance.setUsername(username);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPassword method, of class Blackboard.
     */
    @Test
    public void testSetPassword() {
        System.out.println("setPassword");
        String password = "";
        Blackboard instance = new Blackboard();
        instance.setPassword(password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Blackboard.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Blackboard instance = new Blackboard();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBlackboardID method, of class Blackboard.
     */
    @Test
    public void testGetBlackboardID() {
        System.out.println("getBlackboardID");
        Blackboard instance = new Blackboard();
        long expResult = 0L;
        long result = instance.getBlackboardID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsername method, of class Blackboard.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        Blackboard instance = new Blackboard();
        String expResult = "";
        String result = instance.getUsername();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
