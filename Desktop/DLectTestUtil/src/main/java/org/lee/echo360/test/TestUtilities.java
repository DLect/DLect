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
package org.lee.echo360.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import static org.mockito.Mockito.*;
import org.w3c.dom.Document;

/**
 *
 * @author lee
 */
public class TestUtilities {

    private TestUtilities() {
    }

    public static boolean isInternetReachable() {
        try {
            URL url = new URL("http://www.google.com");

            URLConnection urlConnect = url.openConnection();

            urlConnect.connect();
            return true;
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static HttpResponse createHttpResponce(String responce) throws Exception {
        HttpResponse r = mock(HttpResponse.class);
        HttpEntity e = mock(HttpEntity.class);
        doReturn(createInputStream(responce)).when(e).getContent();
        when(r.getEntity()).thenReturn(e);
        return r;
    }

    public static InputStream createInputStream(String responce) {
        return new ByteArrayInputStream(responce.getBytes());
    }

    public static Document createXMLDocument(String doc) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document parse = builder.parse(createInputStream(doc));
        return parse;
    }

    public static void setField(Object o, String name, Object val) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        assert o != null;
        Field pcsFeild = o.getClass().getDeclaredField(name);
        pcsFeild.setAccessible(true);
        pcsFeild.set(o, val);
    }

    public static Object getField(Object o, String name) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        assert o != null;
        Field pcsFeild = o.getClass().getDeclaredField(name);
        pcsFeild.setAccessible(true);
        return pcsFeild.get(o);
    }

    public static Object getStaticField(Class c, String name) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        assert c != null;
        Field pcsFeild = c.getDeclaredField(name);
        pcsFeild.setAccessible(true);
        return pcsFeild.get(null);
    }

    public static void setStaticField(Class c, String name, Object val) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        assert c != null;
        Field pcsFeild = c.getDeclaredField(name);
        pcsFeild.setAccessible(true);
        pcsFeild.set(null, val);
    }

    public static void assertNotMutable(String desc, List<?> result) {
        try {
            result.add(null);
            Assert.fail(desc + ": Add null succeeds");
        } catch (UnsupportedOperationException e) {
            try {
                result.remove(null);
                Assert.fail(desc + ": Remove null succeeds");
            } catch (UnsupportedOperationException e2) {
                return; // Success
            }
        }
    }

    public static void setExceptionReporterMultiThreaded(boolean multiThreaded) {
        ClassLoader cl = TestUtilities.class.getClassLoader();
        try {
            Class<?> reporter = cl.loadClass("org.lee.echo360.util.ExceptionReporter");
            setStaticField(reporter, "executeOnSeperateThread", multiThreaded);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TestUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TestUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static {
        setExceptionReporterMultiThreaded(false);
    }
}
