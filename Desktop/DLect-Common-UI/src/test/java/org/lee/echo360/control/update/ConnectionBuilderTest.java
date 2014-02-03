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
package org.lee.echo360.control.update;

import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.lee.echo360.test.TestUtilities.*;

/**
 *
 * @author lee
 */
public class ConnectionBuilderTest {

    public ConnectionBuilderTest() {
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
     * Test of connect method, of class ConnectionBuilder.
     */
    @Test
    public void testConnect() throws Exception {
        Assume.assumeTrue(isInternetReachable());
        URL u = new URL("http://www.google.com/humans.txt");
        ConnectionBuilder instance = new ConnectionBuilder();
        HttpURLConnection result = instance.connect(u);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        String res = IOUtils.toString(result.getInputStream());
        assertTrue(res.contains("Google"));
        assertEquals("text/plain", result.getHeaderField("Content-Type"));
        result.disconnect();
    }

    /**
     * Test of connect method, of class ConnectionBuilder.
     */
    @Test//(expected = IOException.class)
    public void testConnectWithException() throws Exception {
        URL u = new URL("http://www.google.com/404/asldkjflkajsdflaksdf");
        ConnectionBuilder instance = new ConnectionBuilder();
        instance.connect(u);
    }
}
