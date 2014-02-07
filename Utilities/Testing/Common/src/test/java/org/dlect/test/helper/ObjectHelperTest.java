/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.test.helper;

import org.dlect.test.Resettable;
import org.dlect.test.helper.testClasses.MultipleStaticAndNonStatic;
import org.dlect.test.helper.testClasses.SingleNonStatic;
import org.dlect.test.helper.testClasses.SingleStatic;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@SuppressWarnings("unchecked")
public class ObjectHelperTest {

    /**
     * Test of storeStaticStateOf method, of class ObjectHelper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testStoreStaticStateOf_SingleStatic() throws Exception {
        String original = SingleStatic.field;
        Resettable r = ObjectHelper.storeStaticStateOf(SingleStatic.class);
        assertEquals("Method changed variable", original, SingleStatic.field);
        SingleStatic.field = original + " - DIFFERENT";
        r.reset();
        assertEquals("Method failed to reset", original, SingleStatic.field);
    }

    /**
     * Test of storeStaticStateOf method, of class ObjectHelper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testStoreStaticStateOf_SingleNonStatic() throws Exception {
        // Storing and resetting a class with no static variables should not error
        Resettable r = ObjectHelper.storeStaticStateOf(SingleNonStatic.class);
        r.reset();
    }

    /**
     * Test of storeStaticStateOf method, of class ObjectHelper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testStoreStaticStateOf_MultipleStaticAndNonStatic() throws Exception {
        String f1 = MultipleStaticAndNonStatic.field1;
        String f2 = MultipleStaticAndNonStatic.field2;
        Resettable r = ObjectHelper.storeStaticStateOf(MultipleStaticAndNonStatic.class);
        assertEquals("Method changed variable field1", f1,  MultipleStaticAndNonStatic.field1);
        assertEquals("Method changed variable field2", f2,  MultipleStaticAndNonStatic.field2);

        MultipleStaticAndNonStatic.field1 = f1 + " - DIFFERENT";
        MultipleStaticAndNonStatic.field2 = f2 + " - DIFFERENT";
        r.reset();
        
        assertEquals("Method failed to reset field1", f1, MultipleStaticAndNonStatic.field1);
        assertEquals("Method failed to reset field1", f2, MultipleStaticAndNonStatic.field2);
    }

    /**
     * Test of setStaticField method, of class ObjectHelper.
     */
    @Test
    public void testSetStaticField() throws Exception {
        
        
    }

    /**
     * Test of getStaticFieldResetter method, of class ObjectHelper.
     */
    @Test
    public void testGetStaticFieldResetter() throws Exception {
    }

    /**
     * Test of getStaticField method, of class ObjectHelper.
     */
    @Test
    public void testGetStaticField() throws Exception {
    }

    /**
     * Test of getClassField method, of class ObjectHelper.
     */
    @Test
    public void testGetClassField() throws Exception {
    }

    /**
     * Test of setField method, of class ObjectHelper.
     */
    @Test
    public void testSetField() throws Exception {
    }

    /**
     * Test of getField method, of class ObjectHelper.
     */
    @Test
    public void testGetField() throws Exception {
    }

}
