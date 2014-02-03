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

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lee
 */
public class MobileProviderTest {

    private static final String NAME = "Name";
    private static final String B2_URL = "https://www.google.com/";
    private static final int CLIENT_ID = 100;
    private static final boolean USING_HTTP_AUTH = false;
    private static final boolean USING_SSL = true;

    public MobileProviderTest() {
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

    private static ProviderInformation makeInstance() {
        return new ProviderInformation(NAME, B2_URL, CLIENT_ID, USING_HTTP_AUTH, USING_SSL);
    }


    /**
     * Test of equals method, of class ProviderInformation.
     */
    @Test
    public void testEquals() {
        ProviderInformation i1 = makeInstance();
        ProviderInformation i2= makeInstance();
        assertEquals(i1, i2);
    }

    /**
     * Test of getName method, of class ProviderInformation.
     */
    @Test
    public void testGetName() {
        ProviderInformation instance = makeInstance();
        String expResult = NAME;
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getB2Url method, of class ProviderInformation.
     */
    @Test
    public void testGetB2Url() {
        ProviderInformation instance = makeInstance();
        String expResult = B2_URL;
        String result = instance.getB2Url();
        assertEquals(expResult, result);
    }

    /**
     * Test of getClientId method, of class ProviderInformation.
     */
    @Test
    public void testGetClientId() {
        ProviderInformation instance = makeInstance();
        int expResult = CLIENT_ID;
        int result = instance.getClientId();
        assertEquals(expResult, result);
    }

    /**
     * Test of isUsingHttpAuth method, of class ProviderInformation.
     */
    @Test
    public void testIsUsingHttpAuth() {
        ProviderInformation instance = makeInstance();
        boolean expResult = USING_HTTP_AUTH;
        boolean result = instance.isUsingHttpAuth();
        assertEquals(expResult, result);
    }

    /**
     * Test of isUsingSSL method, of class ProviderInformation.
     */
    @Test
    public void testIsUsingSSL() {
        ProviderInformation instance = makeInstance();
        boolean expResult = USING_SSL;
        boolean result = instance.isUsingSSL();
        assertEquals(expResult, result);
    }
}