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

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLDecoder;
import org.apache.commons.io.IOUtils;
import org.jdesktop.swingx.util.OS;
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
public class InternalJarHelperTest {

    public InternalJarHelperTest() {
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
     * Test of exec method, of class InternalJarHelper.
     */
    @Test(timeout = 1000)
    public void testExec() throws Exception {
        if (OS.isWindows()) {
            InternalJarHelper instance = new InternalJarHelper();
            Process result = instance.exec("cmd", "/c", "echo", "Hello World");
            result.waitFor();
            String outS = IOUtils.toString(result.getInputStream());
            String errS = IOUtils.toString(result.getErrorStream());
            assertEquals("Error Stream contains something unexpected", "", errS);
            assertEquals("Output Stream contains something unexpected", "Hello World\n", outS);
            assertEquals("Exited with an error code", 0, result.exitValue());
        } else {
            InternalJarHelper instance = new InternalJarHelper();
            Process result = instance.exec("echo", "Hello World");
            result.waitFor();
            String outS = IOUtils.toString(result.getInputStream());
            String errS = IOUtils.toString(result.getErrorStream());
            assertEquals("Error Stream contains something unexpected", "", errS);
            assertEquals("Output Stream contains something unexpected", "Hello World\n", outS);
            assertEquals("Exited with an error code", 0, result.exitValue());
        }
    }

    /**
     * Test of getCurrentJarLocation method, of class InternalJarHelper.
     */
    @Test
    public void testGetCurrentJarLocation() {
        System.out.println("getCurrentJarLocation");
        InternalJarHelper instance = new InternalJarHelper();
        String expResult = URLDecoder.decode(InternalJarExecuter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String result = instance.getCurrentJarLocation();
        assertEquals(expResult, result);
    }

    /**
     * Test of copy method, of class InternalJarHelper.
     */
    @Test
    public void testCopy() throws Exception {
        File from = File.createTempFile("test", null);
        System.out.println(from);
        System.out.println(from.toURI());
        final String content = "Hello World";
        IOUtils.write(content, new FileOutputStream(from));
        File to = File.createTempFile("test", null);
        to.delete();
        InternalJarHelper instance = new InternalJarHelper();
        instance.copy(from.toURI().toURL(), to);
        assertTrue(to.exists());
        assertTrue(from.exists());
        String fromFile = IOUtils.toString(from.toURI());
        assertEquals(content, fromFile);
        String toFile = IOUtils.toString(to.toURI());
        assertEquals(content, toFile); // TODO review the generated test code and remove the default call to fail.
        try {
            from.deleteOnExit();
            from.delete();
            to.deleteOnExit();
            to.delete();
        } catch (Exception e) {
        }
    }

    /**
     * Test of getUpdaterJarLocation method, of class InternalJarHelper.
     */
    @Test
    public void testGetUpdaterJarLocation() {
        System.out.println("getUpdaterJarLocation");
        InternalJarHelper instance = new InternalJarHelper();
        URL expResult = InternalJarExecuter.class.getResource("/updater/updater.jar");
        URL result = instance.getUpdaterJarLocation();
        assertEquals(expResult, result);
    }
}
