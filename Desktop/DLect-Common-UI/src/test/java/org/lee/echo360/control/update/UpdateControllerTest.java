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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lee.echo360.model.UpdateStyle;
import static org.lee.echo360.test.TestUtilities.*;
import org.lee.echo360.util.ExceptionListener;
import org.lee.echo360.util.ExceptionReporter;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
public class UpdateControllerTest {

    private static ConnectionBuilder oldBuilder;
    private static InternalJarExecuter executorInstance;

    public UpdateControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ExceptionReporter.setExecuteOnSeperateThread(false);
        oldBuilder = (ConnectionBuilder) getStaticField(UpdateController.class, "builder");
        executorInstance = (InternalJarExecuter) getStaticField(InternalJarExecuter.class, "instance");
    }

    @AfterClass
    public static void resetExecutor() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        InternalJarExecuter tmp = executorInstance;
        executorInstance = null;
        if (tmp != null) {
            setStaticField(InternalJarExecuter.class, "instance", tmp);
        }
    }

    @AfterClass
    public static void resetBuilder() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ConnectionBuilder tmp = oldBuilder;
        oldBuilder = null;
        if (tmp != null) {
            setStaticField(UpdateController.class, "builder", tmp);
        }
    }

    @Test
    public void testCheckForUpdates() throws IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        HttpURLConnection conn = makeMockConnection(UpdateController.UPDATES_AVALIABLE_RESPONCE_CODE);
        URL updateCheckingURL = new URL(VersionInformation.UPDATE_CHECKING_URL);
        ConnectionBuilder newHelper = makeMockBuilder(updateCheckingURL, conn);
        setStaticField(UpdateController.class, "builder", newHelper);

        int checkForUpdates = UpdateController.checkForUpdates();

        assertEquals("Different Code than given", UpdateController.UPDATES_AVALIABLE_RESPONCE_CODE, checkForUpdates);
        verifyUpdater(newHelper, updateCheckingURL, conn);
    }

    /**
     * Test of doUpdates method, of class UpdateController.
     */
    @Test
    public void testDoUpdatesWithAvaliable() throws IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        UpdateStyle s = UpdateStyle.USER_NOTIFIED;
        String styleOpt = s.getCode();
        
        InternalJarExecuter newExecutor = mock(InternalJarExecuter.class);
        when(newExecutor.spawnUpdater(styleOpt)).thenReturn(true);
        setStaticField(InternalJarExecuter.class, "instance", newExecutor);

        HttpURLConnection conn = makeMockConnection(UpdateController.UPDATES_AVALIABLE_RESPONCE_CODE);
        URL updateCheckingURL = new URL(VersionInformation.UPDATE_CHECKING_URL);
        ConnectionBuilder newHelper = makeMockBuilder(updateCheckingURL, conn);
        setStaticField(UpdateController.class, "builder", newHelper);

        ExceptionListener listener = mock(ExceptionListener.class);
        ExceptionReporter.addExceptionListener(listener);

        UpdateController.doUpdates(s);

        ExceptionReporter.removeExceptionListener(listener);

        verifyUpdater(newHelper, updateCheckingURL, conn);
        verify(newExecutor).spawnUpdater(styleOpt);

        verifyZeroInteractions(listener);
    }

    /**
     * Test of doUpdates method, of class UpdateController.
     */
    @Test
    public void testDoUpdatesNotAvaliable() throws IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        UpdateStyle s = UpdateStyle.USER_NOTIFIED;
        String styleOpt = s.getCode();
        
        InternalJarExecuter newExecutor = mock(InternalJarExecuter.class);
        setStaticField(InternalJarExecuter.class, "instance", newExecutor);

        HttpURLConnection conn = makeMockConnection(UpdateController.NO_UPDATES_RESPONCE_CODE);
        URL updateCheckingURL = new URL(VersionInformation.UPDATE_CHECKING_URL);
        ConnectionBuilder newHelper = makeMockBuilder(updateCheckingURL, conn);
        setStaticField(UpdateController.class, "builder", newHelper);

        ExceptionListener listener = mock(ExceptionListener.class);
        ExceptionReporter.addExceptionListener(listener);

        UpdateController.doUpdates(s);

        ExceptionReporter.removeExceptionListener(listener);

        verifyUpdater(newHelper, updateCheckingURL, conn);

        verifyZeroInteractions(newExecutor, listener);
    }

    /**
     * Test of doUpdates method, of class UpdateController.
     */
    @Test
    public void testDoUpdatesErrors() throws IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        UpdateStyle s = UpdateStyle.USER_NOTIFIED;
        String styleOpt = s.getCode();
        
        InternalJarExecuter newExecutor = mock(InternalJarExecuter.class);
        when(newExecutor.spawnUpdater(styleOpt)).thenReturn(true);
        setStaticField(InternalJarExecuter.class, "instance", newExecutor);

        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getHeaderField("X-Current-Version")).thenReturn("Test Version");
        final IOException exception = new IOException("Hello");
        when(conn.getResponseCode()).thenThrow(exception);

        URL updateCheckingURL = new URL(VersionInformation.UPDATE_CHECKING_URL);
        ConnectionBuilder newHelper = makeMockBuilder(updateCheckingURL, conn);
        setStaticField(UpdateController.class, "builder", newHelper);

        ExceptionListener listener = mock(ExceptionListener.class);
        ExceptionReporter.addExceptionListener(listener);

        UpdateController.doUpdates(s);

        ExceptionReporter.removeExceptionListener(listener);

        verifyUpdater(newHelper, updateCheckingURL, conn);

        verifyZeroInteractions(newExecutor);

        verify(listener).exceptionThrown(null);
        verifyNoMoreInteractions(listener);
    }

    private HttpURLConnection makeMockConnection(int responceCode) throws IOException {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getHeaderField("X-Current-Version")).thenReturn("Test Version");
        when(conn.getResponseCode()).thenReturn(responceCode);
        return conn;
    }

    private ConnectionBuilder makeMockBuilder(URL updateCheckingURL, HttpURLConnection conn) throws IOException {
        ConnectionBuilder newHelper = mock(ConnectionBuilder.class);
        when(newHelper.connect(updateCheckingURL)).thenReturn(conn);
        return newHelper;
    }

    private void verifyUpdater(ConnectionBuilder newHelper, URL updateCheckingURL, HttpURLConnection conn) throws IOException {
        verify(newHelper).connect(updateCheckingURL);
        verifyNoMoreInteractions(newHelper);

        verify(conn, atLeast(0)).getHeaderField("X-Current-Version");
        verify(conn, atLeast(1)).getResponseCode();
        verify(conn).disconnect();
        verifyNoMoreInteractions(conn);
    }
}
