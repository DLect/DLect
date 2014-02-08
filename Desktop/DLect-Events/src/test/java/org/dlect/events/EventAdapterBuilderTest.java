/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import org.dlect.events.testAdapter.TestEventAdapter;
import org.dlect.test.Resettable;
import org.dlect.test.Resettables;
import org.dlect.test.helper.LoggingSetup;
import org.dlect.test.helper.LoggingSetup.LoggingSetupReset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.dlect.test.helper.ObjectHelper.*;
import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
@SuppressWarnings("unchecked")
public class EventAdapterBuilderTest {

    private Resettable reset;

    public static final String ADAPTER_CLASS_FIELD_NAME = "eventAdapterClass";

    @Before
    public void beforeTest() throws Exception {
        reset = Resettables.join(LoggingSetup.disableLogging(EventLogger.LOG),
                                 storeStaticStateOf(EventAdapterBuilder.class));
    }

    @After
    public void afterTest() throws Exception {
        reset.reset();
    }

    /**
     * Test of getNewAdapter method, of class EventAdapterBuilder.
     */
    @Test
    public void testGetNewAdapter_Equality() {
        EventAdapter adapter1 = EventAdapterBuilder.getNewAdapter();
        EventAdapter adapter2 = EventAdapterBuilder.getNewAdapter();
        assertNotSame("Returning the same adapter twice", adapter1, adapter2);
    }

    /**
     * Test of getNewAdapter method, of class EventAdapterBuilder.
     *
     * @throws java.lang.Exception If the class fails to set the adapter class.
     */
    @Test
    public void testGetNewAdapter_Fallback() throws Exception {
        Resettable s = setStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME, EventAdapter.class);
        try {
            EventAdapter adapter1 = EventAdapterBuilder.getNewAdapter();
            assertNotNull("Failing to return an adapter when invalid adapter given", adapter1);
        } finally {
            s.reset();
        }
    }

    /**
     * Test of getNewAdapter method, of class EventAdapterBuilder.
     *
     * @throws java.lang.Exception If the class fails to set the adapter class.
     */
    @Test
    public void testGetNewAdapter_Customisation() throws Exception {
        setStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME, TestEventAdapter.class);

        EventAdapter adapter1 = EventAdapterBuilder.getNewAdapter();
        assertEquals("Failing to respect given adapter class", adapter1.getClass(), TestEventAdapter.class);
    }

    /**
     * Test of getNewAdapter method, of class EventAdapterBuilder.
     *
     * @throws java.lang.Exception If the class fails to set the adapter class.
     */
    @Test
    public void testGetNewAdapter_CustomisationEquality() throws Exception {
        setStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME, TestEventAdapter.class);
        EventAdapter adapter1 = EventAdapterBuilder.getNewAdapter();
        EventAdapter adapter2 = EventAdapterBuilder.getNewAdapter();

        assertEquals("Failing to respect given adapter class", adapter1.getClass(), TestEventAdapter.class);
        assertEquals("Failing to respect given adapter class", adapter2.getClass(), TestEventAdapter.class);
        assertNotSame("Failing to respect given adapter class", adapter1, adapter2);
    }

    /**
     * Test of setDefaultAdapterClass method, of class EventAdapterBuilder.
     *
     * @throws java.lang.Exception If the class fails to set the adapter class.
     */
    @Test
    public void testSetDefaultAdapterClass_Invalid() throws Exception {
        Object oldClass = getStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME);
        try {
            EventAdapterBuilder.setEventAdapterClass(EventAdapter.class);
            fail("Invalid class succeeded.");
        } catch (IllegalArgumentException e) {
            Object newClass = getStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME);
            assertEquals("Class changed even though the method failed", oldClass, newClass);
        }
    }

    /**
     * Test of setDefaultAdapterClass method, of class EventAdapterBuilder.
     *
     * @throws java.lang.Exception If the class fails to set the adapter class.
     */
    @Test
    public void testSetDefaultAdapterClass_Valid() throws Exception {
        Object oldClass = getStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME);
        
        EventAdapterBuilder.setEventAdapterClass(TestEventAdapter.class);
        
        Object newClass = getStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME);
        
        assertNotEquals("Class not changed even though the method succeded", oldClass, newClass);
        assertEquals("Class not the expected one.", TestEventAdapter.class, newClass);
    }

    /**
     * Test of setDefaultAdapterClass method, of class EventAdapterBuilder.
     *
     * @throws java.lang.Exception If the class fails to set the adapter class.
     */
    @Test
    public void testSetDefaultAdapterClass_ResetWithNull() throws Exception {
        setStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME, TestEventAdapter.class);
        
        Object oldClass = getStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME);

        EventAdapterBuilder.setEventAdapterClass(null);

        Object newClass = getStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME);
        
        assertNotEquals("Class not changed even though the method succeded", oldClass, newClass);
        assertEquals("Class not the expected one.", EventAdapterBuilder.DEFAULT_ADAPTER_CLASS, newClass);
    }

    /**
     * Test of getDefaultAdapterClass method, of class EventAdapterBuilder.
     *
     * @throws java.lang.Exception If the class fails to set the adapter class.
     */
    @Test
    public void testGetDefaultAdapterClass_Set() throws Exception {
        Class<?> setClass = TestEventAdapter.class;
        setStaticField(EventAdapterBuilder.class, ADAPTER_CLASS_FIELD_NAME, setClass);

        Class<? extends EventAdapter> clz = EventAdapterBuilder.getEventAdapterClass();

        assertEquals("Class returned is not the one that was set.", setClass, clz);
    }

    /**
     * Test of getDefaultAdapterClass method, of class EventAdapterBuilder.
     *
     * @throws java.lang.Exception If the class fails to set the adapter class.
     */
    @Test
    public void testGetDefaultAdapterClass_Default() throws Exception {
        Class<?> resetDefault = EventAdapterBuilder.DEFAULT_ADAPTER_CLASS;

        EventAdapterBuilder.resetDefaultAdapterClass();

        Class<? extends EventAdapter> clz = EventAdapterBuilder.getEventAdapterClass();

        assertEquals("Class returned is not the one that was set.", resetDefault, clz);
    }

}
