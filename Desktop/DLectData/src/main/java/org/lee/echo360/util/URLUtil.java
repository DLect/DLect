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
package org.lee.echo360.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 * @author lee
 */
public class URLUtil {

    public static boolean isOffline() {
        try {
            new URL("http://uqlectures.sourceforge.net/").openStream().close();
            return false;
        } catch (MalformedURLException e) {
            ExceptionReporter.reportException(e);
            return true;
        } catch (IOException e) {
            return true;
        }
    }

    /**
     *
     * @param regex the value of regex
     */
    public static String decodeUrlParam(String regex) {
        try {
            return URLDecoder.decode(regex, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return URLDecoder.decode(regex);
        }
    }

    public static String encode(String unencoded) {
        try {
            return URLEncoder.encode(unencoded, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return URLEncoder.encode(unencoded);
        }
    }

    /**
     * Performs an un-checked GET request to the specified URL. No errors are
     * thrown up the stack, however they are reported to the
     * {@linkplain ExceptionReporter} class. This method will not attempt to
     * repeat the request until a success is found.
     * @param url
     */
    public static void goTo(String url) {
        try {
            goTo(new URL(url));
        } catch (Throwable t) {
            // Catch anything
            ExceptionReporter.reportException(t);
        }
    }

    /**
     * Performs an un-checked GET request to the specified URL. No errors are
     * thrown up the stack, however they are reported to the
     * {@linkplain ExceptionReporter} class. This method will not attempt to
     * repeat the request until a success is found.
     * @param url
     */
    public static void goTo(URL url) {
        try {
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            con.connect();
            con.getInputStream().close();
        } catch (Throwable t) {
            // Catch anything
            ExceptionReporter.reportException(t);
        }
    }

    /**
     * Performs an un-checked GET request to the specified URL. No errors are
     * thrown up the stack, however they are reported to the
     * {@linkplain ExceptionReporter} class. This method will not attempt to
     * repeat the request until a success is found.
     * @param uri
     */
    public static void goTo(URI uri) {
        try {
            goTo(uri.toURL());
        } catch (Throwable t) {
            // Catch anything
            ExceptionReporter.reportException(t);
        }
    }

    private URLUtil() {
    }
}
