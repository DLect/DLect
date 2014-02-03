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
import java.io.IOException;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.lee.echo360.test.TestUtilities.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
public class InternalJarExecuterTest {

    private InternalJarExecuter instance;

    public InternalJarExecuterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        instance = (InternalJarExecuter) getStaticField(InternalJarExecuter.class, "instance");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSpawnUpdaterProcess1() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        InternalJarExecuter newExecutor = mock(InternalJarExecuter.class);
        try {
            String opt = "-?";
            when(newExecutor.spawnUpdater(opt)).thenReturn(true);
            setStaticField(InternalJarExecuter.class, "instance", newExecutor);

            boolean re = InternalJarExecuter.spawnUpdaterProcess(opt);
            assertTrue(re);

            verify(newExecutor).spawnUpdater(opt);
            verifyNoMoreInteractions(newExecutor);
        } finally {
            setStaticField(InternalJarExecuter.class, "instance", instance);
        }
    }

    @Test
    public void testSpawnUpdaterProcess0() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        InternalJarExecuter newExecutor = mock(InternalJarExecuter.class);
        try {
            String defaultOpt = "";
            when(newExecutor.spawnUpdater(defaultOpt)).thenReturn(true);
            setStaticField(InternalJarExecuter.class, "instance", newExecutor);

            boolean re = InternalJarExecuter.spawnUpdaterProcess();
            assertTrue(re);

            verify(newExecutor).spawnUpdater(defaultOpt);
            verifyNoMoreInteractions(newExecutor);
        } finally {
            setStaticField(InternalJarExecuter.class, "instance", instance);
        }
    }

    @Test
    public void testSpawnUpdaterProcessF() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        InternalJarExecuter newExecutor = mock(InternalJarExecuter.class);
        try {
            when(newExecutor.spawnUpdater(anyString())).thenReturn(false);
            setStaticField(InternalJarExecuter.class, "instance", newExecutor);

            boolean re = InternalJarExecuter.spawnUpdaterProcess();
            assertFalse(re);

            verify(newExecutor).spawnUpdater(anyString());
            verifyNoMoreInteractions(newExecutor);
        } finally {
            setStaticField(InternalJarExecuter.class, "instance", instance);
        }
    }

    @Test
    public void testSpawnUpdaterDefaultJava() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException {
        Object oldHelper = getStaticField(InternalJarExecuter.class, "helper");
        try {
            InternalJarHelper newHelper = mock(InternalJarHelper.class);
            setStaticField(InternalJarExecuter.class, "helper", newHelper);

            String currentLocation = "Current Location";
            URL updaterJarFile = new URL("http://localhost/");
            String styleOpt = "-n";
            when(newHelper.getCurrentJarLocation()).thenReturn(currentLocation);
            when(newHelper.getUpdaterJarLocation()).thenReturn(updaterJarFile);

            final ArgumentCaptor<File> ac = ArgumentCaptor.forClass(File.class);
            doNothing().when(newHelper).copy(eq(updaterJarFile), ac.capture());

            Process javaVerProcess = mock(Process.class);
            doNothing().when(javaVerProcess).destroy();
            when(newHelper.exec("java", "-version")).thenReturn(javaVerProcess);

            when(newHelper.exec(eq("java"), eq("-jar"), anyString(), eq(currentLocation), eq(styleOpt))).thenReturn(mock(Process.class));
            
            boolean success = instance.spawnUpdater(styleOpt);

            assertTrue(success);
            
            File tempFile = ac.getValue();

            verify(newHelper).getCurrentJarLocation();
            verify(newHelper).getUpdaterJarLocation();

            verify(newHelper).copy(updaterJarFile, tempFile);

            verify(newHelper).exec("java", "-version");
            verify(newHelper).exec("java", "-jar", tempFile.getPath(), currentLocation, styleOpt);

            verifyNoMoreInteractions(newHelper);
        } finally {
            setStaticField(InternalJarExecuter.class, "helper", oldHelper);
        }
    }

    @Test
    public void testSpawnUpdaterOtherJava() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException {
        Object oldHelper = getStaticField(InternalJarExecuter.class, "helper");
        try {
            InternalJarHelper newHelper = mock(InternalJarHelper.class);
            setStaticField(InternalJarExecuter.class, "helper", newHelper);

            String currentLocation = "Current Location";
            URL updaterJarFile = new URL("http://localhost/");
            String styleOpt = "-n";
            when(newHelper.getCurrentJarLocation()).thenReturn(currentLocation);
            when(newHelper.getUpdaterJarLocation()).thenReturn(updaterJarFile);

            final ArgumentCaptor<File> tmpFile = ArgumentCaptor.forClass(File.class);
            doNothing().when(newHelper).copy(eq(updaterJarFile), tmpFile.capture());

            when(newHelper.exec("java", "-version")).thenThrow(IOException.class);

            Process javaVerProcess = mock(Process.class);
            final ArgumentCaptor<String> javaLoc = ArgumentCaptor.forClass(String.class);
            doNothing().when(javaVerProcess).destroy();
            when(newHelper.exec(javaLoc.capture(), eq("-version"))).thenReturn(javaVerProcess);

            when(newHelper.exec(anyString(), eq("-jar"), anyString(), eq(currentLocation), eq(styleOpt))).thenReturn(mock(Process.class));

            instance.spawnUpdater(styleOpt);

            File tempFile = tmpFile.getValue();

            verify(newHelper).getCurrentJarLocation();
            verify(newHelper).getUpdaterJarLocation();

            verify(newHelper).copy(updaterJarFile, tempFile);

            verify(newHelper).exec("java", "-version");
            verify(newHelper).exec(javaLoc.getValue(), "-version");
            verify(newHelper).exec(javaLoc.getValue(), "-jar", tempFile.getPath(), currentLocation, styleOpt);

            verifyNoMoreInteractions(newHelper);
        } finally {
            setStaticField(InternalJarExecuter.class, "helper", oldHelper);
        }
    }

    @Test
    public void testSpawnUpdaterNoJava() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException {
        Object oldHelper = getStaticField(InternalJarExecuter.class, "helper");
        try {
            InternalJarHelper newHelper = mock(InternalJarHelper.class);
            setStaticField(InternalJarExecuter.class, "helper", newHelper);

            String currentLocation = "Current Location";
            URL updaterJarFile = new URL("http://localhost/");
            when(newHelper.getCurrentJarLocation()).thenReturn(currentLocation);
            when(newHelper.getUpdaterJarLocation()).thenReturn(updaterJarFile);

            final ArgumentCaptor<File> tmpFile = ArgumentCaptor.forClass(File.class);
            doNothing().when(newHelper).copy(eq(updaterJarFile), tmpFile.capture());

            when(newHelper.exec("java", "-version")).thenThrow(IOException.class);

            final ArgumentCaptor<String> javaLoc = ArgumentCaptor.forClass(String.class);
            when(newHelper.exec(javaLoc.capture(), eq("-version"))).thenThrow(IOException.class);

            instance.spawnUpdater("");

            File tempFile = tmpFile.getValue();

            verify(newHelper).getCurrentJarLocation();
            verify(newHelper).getUpdaterJarLocation();

            verify(newHelper).copy(updaterJarFile, tempFile);

            verify(newHelper).exec("java", "-version");
            verify(newHelper).exec(javaLoc.getValue(), "-version");

            verifyNoMoreInteractions(newHelper);
        } finally {
            setStaticField(InternalJarExecuter.class, "helper", oldHelper);
        }
    }
}
