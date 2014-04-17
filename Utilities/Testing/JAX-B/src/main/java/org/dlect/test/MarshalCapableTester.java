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
package org.dlect.test;

import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.logging.TestLogging;

import static org.junit.Assert.fail;

/**
 *
 * @author lee
 */
public class MarshalCapableTester {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    public static String testMarshalNonRootObject(Class<?> clazz, Class<?>... classes) {
        Class<?>[] boundClasses = Lists.asList(clazz, classes).toArray(new Class<?>[1]);
        try {

            Object obj = clazz.newInstance();

            return testMarshalNonRootObject(obj, boundClasses);

        } catch (InstantiationException | IllegalAccessException ex) {
            TestLogging.LOG.error("Error Caught On Init", ex);
            fail("Error Caught On Init");
            return null;
        }
    }

    public static String testMarshalNonRootObject(Object obj, Class<?>... classes) {
        Class<?>[] boundClasses = Lists.asList(WrapperElement.class, classes).toArray(new Class<?>[1]);
        return testMarshalObject(new WrapperElement(obj), boundClasses);
    }

    public static String testMarshalObject(Class<?> clazz, Class<?>... classes) {

        Class<?>[] boundClasses = Lists.asList(clazz, classes).toArray(new Class<?>[1]);

        try {

            Object obj = clazz.newInstance();

            return testMarshalObject(obj, boundClasses);

        } catch (InstantiationException | IllegalAccessException ex) {
            TestLogging.LOG.error("Error Caught On Init", ex);
            fail("Error Caught On Init");
            return null;
        }

    }

    public static String testMarshalObject(Object obj, Class<?>... boundClasses) {
        try {
            JAXBContext jc = JAXBContext.newInstance(boundClasses);

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, UTF_8);

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            marshaller.marshal(obj, os);

            String s = os.toString(UTF_8);

            return s;

        } catch (JAXBException | UnsupportedEncodingException ex) {
            TestLogging.LOG.error("Error Caught On marshal", ex);
            fail("Error Caught On marshal");
            return null;
        }
    }

    private MarshalCapableTester() {
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> T testUnmarshalObject(String input, Class<T> type, Class<?>... otherClasses) {
        Class<?>[] classes = Lists.asList(type, otherClasses).toArray(new Class<?>[otherClasses.length + 1]);
        try {
            JAXBContext jc = JAXBContext.newInstance(classes);

            Unmarshaller unmarshaller = jc.createUnmarshaller();

            ByteArrayInputStream os = new ByteArrayInputStream(input.getBytes());

            return (T) unmarshaller.unmarshal(os);

        } catch (JAXBException | ClassCastException ex) {
            TestLogging.LOG.error("Error Caught On Unmarshal", ex);
            fail("Error Caught On Unmarshal");
            return null;
        }
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> T testUnmarshalNonRootObject(String input, Class<T> type, Class<?>... otherClasses) {
        Class<?>[] classes = Lists.asList(WrapperElement.class, type, otherClasses).toArray(new Class<?>[otherClasses.length + 2]);
        try {
            JAXBContext jc = JAXBContext.newInstance(classes);

            Unmarshaller unmarshaller = jc.createUnmarshaller();

            ByteArrayInputStream os = new ByteArrayInputStream(input.getBytes());

            WrapperElement we = (WrapperElement) unmarshaller.unmarshal(os);

            return (T) we.obj;

        } catch (JAXBException | ClassCastException ex) {
            TestLogging.LOG.error("Error Caught On Unmarshal", ex);
            fail("Error Caught On Unmarshal");
            return null;
        }
    }

}

@XmlRootElement(name = "NonRootElementWrapper")
class WrapperElement {

    @XmlElement(name = "NonRootElementWrapperWrapper")
    Object obj;

    public WrapperElement() {
    }

    public WrapperElement(Object obj) {
        this.obj = obj;
    }

}
