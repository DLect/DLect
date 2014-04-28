/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.After;
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
public class UQRotaHelperImplTest {

    @Mock
    private Object o;

    //@InjectMocks
    private UQRotaHelperImpl testObject;

    @Before
    public void setUp() {
        testObject = new UQRotaHelperImpl();
    }

    @After
    public void tearDown() {
    }

    @Test
    @Ignore
    public void testExamplar() throws Exception {
        Object o = mock(Object.class);
        assertNotNull(o);
        fail();
    }

    /**
     * Test of getSemester method, of class UQRotaHelperImpl.
     */
    @Test
    public void testGetSemester() {
    }

    /**
     * Test of getStreamsFor method, of class UQRotaHelperImpl.
     */
    @Test
    public void testGetStreamsFor_String_int() {
        System.out.println(testObject.getStreamsFor("MATH1051", 6420));
    }

    /**
     * Test of getStreamsFor method, of class UQRotaHelperImpl.
     */
    @Test
    public void testGetStreamsFor_5args() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(0);
        cal.set(2014, 3 - 1, 27, 9, 0);
        System.out.println("CAL: " + cal);
        System.out.println(testObject.getStreamsFor("MATH1051", 6420, cal.getTime(), null, null));
    }

    /**
     * Test of getAllStreamsIn method, of class UQRotaHelperImpl.
     */
    @Test
    public void testGetAllStreamsIn() {
    }

    /**
     * Test of getOfferingId method, of class UQRotaHelperImpl.
     */
    @Test
    public void testGetOfferingId() {
    }

}
