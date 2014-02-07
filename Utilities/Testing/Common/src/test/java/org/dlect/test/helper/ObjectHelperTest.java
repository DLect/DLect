/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.test.helper;

import org.dlect.test.Resettable;
import org.dlect.test.helper.testClasses.MultipleStaticAndNonStatic;
import org.dlect.test.helper.testClasses.PrivateStatic;
import org.dlect.test.helper.testClasses.SingleNonStatic;
import org.dlect.test.helper.testClasses.SingleStatic;
import org.dlect.test.helper.testClasses.StaticFinal;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals("Method changed variable field1", f1, MultipleStaticAndNonStatic.field1);
        assertEquals("Method changed variable field2", f2, MultipleStaticAndNonStatic.field2);

        MultipleStaticAndNonStatic.field1 = f1 + " - DIFFERENT";
        MultipleStaticAndNonStatic.field2 = f2 + " - DIFFERENT";
        r.reset();

        assertEquals("Method failed to reset field1", f1, MultipleStaticAndNonStatic.field1);
        assertEquals("Method failed to reset field2", f2, MultipleStaticAndNonStatic.field2);
    }

    /**
     * Test of storeStaticStateOf method, of class ObjectHelper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testStoreStaticStateOf_PrivateStaticField() throws Exception {
        String field = PrivateStatic.getField();

        Resettable r = ObjectHelper.storeStaticStateOf(SingleStatic.class, StaticFinal.class);

        assertEquals("Method changed variable field1", field, PrivateStatic.getField());

        PrivateStatic.changeField();

        r.reset();

        assertEquals("Method failed to reset a private field.", field, PrivateStatic.getField());
    }

    /**
     * Test of storeStaticStateOf method, of class ObjectHelper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testStoreStaticStateOf_NoResetStaticFinalField() throws Exception {
        String finalF = StaticFinal.finalField;
        String nonFinalF = StaticFinal.nonFinalField;

        Resettable r = ObjectHelper.storeStaticStateOf(StaticFinal.class);

        assertEquals("Method changed variable field1", finalF, StaticFinal.finalField);
        assertEquals("Method changed variable field2", nonFinalF, StaticFinal.nonFinalField);

        StaticFinal.nonFinalField = nonFinalF + " - DIFFERENT";
        StaticFinal.changeFinalField();

        r.reset();

        assertEquals("Method reset a static final field.", finalF, StaticFinal.finalField);
        assertNotEquals("Method failed to reset a non-final field", nonFinalF, StaticFinal.nonFinalField);
    }

    /**
     * Test of storeStaticStateOf method, of class ObjectHelper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testStoreStaticStateOf_ResetMultipleClassses() throws Exception {
        String ssField = SingleStatic.field;
        String sfField = StaticFinal.nonFinalField;

        Resettable r = ObjectHelper.storeStaticStateOf(SingleStatic.class, StaticFinal.class);

        assertEquals("Method changed variable field1", ssField, SingleStatic.field);
        assertEquals("Method changed variable field2", sfField, StaticFinal.nonFinalField);

        SingleStatic.field = ssField + " - DIFFERENT";
        StaticFinal.nonFinalField = sfField + " - DIFFERENT";

        r.reset();

        assertEquals("Method failed to reset a field in SingleStatic.", ssField, SingleStatic.field);
        assertEquals("Method failed to reset a field in StaticFinal.", sfField, StaticFinal.nonFinalField);
    }

    /**
     * Test of setStaticField method, of class ObjectHelper.
     */
    @Test
    @Ignore
    public void testSetStaticField() throws Exception {

    }

    /**
     * Test of getStaticFieldResetter method, of class ObjectHelper.
     */
    @Test
    @Ignore
    public void testGetStaticFieldResetter() throws Exception {
    }

    /**
     * Test of getStaticField method, of class ObjectHelper.
     */
    @Test
    @Ignore
    public void testGetStaticField() throws Exception {
    }

    /**
     * Test of getClassField method, of class ObjectHelper.
     */
    @Test
    @Ignore
    public void testGetClassField() throws Exception {
    }

    /**
     * Test of setField method, of class ObjectHelper.
     */
    @Test
    @Ignore
    public void testSetField() throws Exception {
    }

    /**
     * Test of getField method, of class ObjectHelper.
     */
    @Test
    @Ignore
    public void testGetField() throws Exception {
    }

}
