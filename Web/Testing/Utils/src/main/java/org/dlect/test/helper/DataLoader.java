/*
 * This file is part of DLect. DLect is a suite of code that facilitates the downloading of lecture recordings.
 *
 * Copyright © 2014 Lee Symes.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dlect.test.helper;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
public class DataLoader {

    /**
     * Get the contents of an existent non-empty file or fail the test(to allow for empty files use
     * {@linkplain #loadFromFile(java.io.File, int) loadFromFile(fileName, 0)}).
     *
     * @param fileName The file to load from.
     *
     * @return A non-empty string representing the content of the file given by the file name; or 'fails' the test if the file is not
     *         accessible or is empty.
     */
    public static String loadFromFile(String fileName) throws IOException {
        return loadFromFile(fileName, 1);
    }

    /**
     * Get the contents of an existent non-empty file or fail the test(to allow for empty files use
     * {@linkplain #loadFromFile(java.io.File, int) loadFromFile(fileName, 0)}).
     *
     * @param fileName The file to load from.
     *
     * @return A non-empty string representing the content of the file given by the file name; or 'fails' the test if the file is not
     *         accessible or is empty.
     */
    public static String loadFromFile(File fileName) throws IOException {
        return loadFromFile(fileName, 1);
    }

    /**
     * Get the contents of an existent file of length greater than or equal to {@code minLength} or fail the test.
     *
     * @param fileName  The file to load from.
     * @param minLength The minimum length of the file, if shorter than then fail the test.
     *
     * @return A non-empty string representing the content of the file given by the file name; or 'fails' the test if the file is not
     *         accessible or is empty.
     */
    public static String loadFromFile(String fileName, int minLength) throws IOException {
        return loadFromFile(new File(fileName), minLength);
    }

    public static String loadFromFile(File fileName, int minLength) throws IOException {
        assertTrue("The file(" + fileName + ") must exists for this test to pass", fileName.exists());
        return loadFromStream(new FileInputStream(fileName), minLength);
    }

    public static String loadFromStream(InputStream stream, int minLength) throws IOException {
        String ret = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        assertTrue(ret.length() >= minLength);
        return ret;
    }

    public static String loadFromStream(InputStream stream) throws IOException {
        return loadFromStream(stream, 1);
    }

    public static String loadResource(String path) throws IOException {
        return loadResource(path, DataLoader.class, 1);
    }

    public static String loadResource(String path, Object relTo) throws IOException {
        return loadResource(path, relTo.getClass(), 1);
    }

    public static String loadResource(String path, Class<?> relTo) throws IOException {
        return loadResource(path, relTo.getClass(), 1);
    }

    public static String loadResource(String path, int minLength) throws IOException {
        return loadResource(path, DataLoader.class, minLength);
    }

    public static String loadResource(String path, Object relTo, int minLength) throws IOException {
        return loadResource(path, relTo.getClass(), minLength);
    }

    public static String loadResource(String path, Class<?> relTo, int minLength) throws IOException {
        InputStream str = DataLoader.class.getClassLoader().getResourceAsStream(path);
        if (str == null) {
            fail("Failed to load the path(" + path + ") using classLoader defined from " + relTo);
        }
        return loadFromStream(str, minLength);
    }

}
