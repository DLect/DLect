/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.events.listenable;

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
@Ignore
public class ListenableTest {

    @Mock
    private Object o;

    @InjectMocks
    private Object testObject;

    @Before
    public void before() {
    }



    @Test
    public void testExamplar() throws Exception {
        Object o = mock(Object.class);
        assertNotNull(o);
        fail();
    }

    /**
     * Test of getAdapter method, of class Listenable.
     */
    @Test
    public void testGetAdapter() {
    }

    /**
     * Test of addListener method, of class Listenable.
     */
    @Test
    public void testAddListener() {
    }

    /**
     * Test of removeListener method, of class Listenable.
     */
    @Test
    public void testRemoveListener() {
    }

    /**
     * Test of addChild method, of class Listenable.
     */
    @Test
    public void testAddChild() {
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
    public void testFireEvent() {
    }

}