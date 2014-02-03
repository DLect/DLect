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
public class ActionResultTest {

    public ActionResultTest() {
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
     * Test of isSuccess method, of class ActionResult.
     */
    @Test
    public void testIsSuccessOnSuccess() {
        ActionResult instance = ActionResult.SUCCEDED;
        boolean result = instance.isSuccess();
        assertTrue(result);
    }

    /**
     * Test of isSuccess method, of class ActionResult.
     */
    @Test
    public void testIsSuccessOnFatal() {
        ActionResult instance = ActionResult.FATAL;
        boolean result = instance.isSuccess();
        assertFalse(result);
    }

    /**
     * Test of isSuccess method, of class ActionResult.
     */
    @Test
    public void testIsSuccessOnFailed() {
        ActionResult instance = ActionResult.FAILED;
        boolean result = instance.isSuccess();
        assertFalse(result);
    }

    /**
     * Test of isFatal method, of class ActionResult.
     */
    @Test
    public void testIsFatalOnSuccess() {
        ActionResult instance = ActionResult.SUCCEDED;
        boolean result = instance.isFatal();
        assertFalse(result);
    }

    /**
     * Test of isFatal method, of class ActionResult.
     */
    @Test
    public void testIsFatalOnFatal() {
        ActionResult instance = ActionResult.FATAL;
        boolean result = instance.isFatal();
        assertTrue(result);
    }

    /**
     * Test of isFatal method, of class ActionResult.
     */
    @Test
    public void testIsFatalOnFailure() {
        ActionResult instance = ActionResult.FAILED;
        boolean result = instance.isFatal();
        assertFalse(result);
    }
}