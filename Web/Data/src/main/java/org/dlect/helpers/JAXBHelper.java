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
package org.dlect.helpers;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.dlect.log.Stores;

/**
 *
 * @author lee
 */
public class JAXBHelper {

    public static <T> JaxbBindingSet<T> binding(Class<T> returnClass, Class... boundClasses) {
        return new JaxbBindingSet<>(ImmutableList.<Class<?>>builder().add(returnClass).add(boundClasses).build());
    }

    public static <T> JaxbBindingSet<T> bindingInterface(Class... boundClasses) {
        return new JaxbBindingSet<>(ImmutableList.<Class<?>>copyOf(boundClasses));
    }

    public static <T> ResponseHandler<T> responseHandlerFor(Class<T> returnClass, Class... boundClasses) {
        return responseHandlerFor(binding(returnClass, boundClasses));
    }

    public static <T> ResponseHandler<T> responseHandlerFor(final JaxbBindingSet<T> binding) {
        return new ResponseHandler<T>() {

            @Override
            public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                InputStream c = response.getEntity().getContent();
                String str = CharStreams.toString(new InputStreamReader(c));

                Stores.LOG.error("XML\n{}", str);

                try {
                    return unmarshalFromStream(new ByteArrayInputStream(str.getBytes()), binding);
                } catch (JAXBException ex) {

                    Stores.LOG.error("Headers: {}", Arrays.toString(response.getAllHeaders()));
                    Stores.LOG.error("Targeting: {}", Arrays.toString(binding.getBindings()));
                    Stores.LOG.error("Handle Responce failed", ex);
                    throw new IOException(ex);
                }
            }
        };
    }

    /**
     * Unmarshal XML data from the specified InputStream and return the
     * resulting content tree. Validation event location information may be
     * incomplete when using this form of the unmarshal API.
     *
     * <p>
     * Implements <a href="#unmarshalGlobal">Unmarshal Global Root Element</a>.
     *
     * @param stream        the InputStream to unmarshal XML data from
     * @param classesToBind
     *
     * @return the newly created root object of the java content tree
     *
     * @throws java.io.IOException
     * @throws JAXBException            If any unexpected errors occur while unmarshalling
     * @throws IllegalArgumentException If the InputStream parameter is null
     */
    public static Object unmarshalFromStream(InputStream stream, Class<?>... classesToBind) throws IOException, JAXBException {

        try (InputStream s = stream) {
            String str = CharStreams.toString(new InputStreamReader(s));
            stream.close();
            Stores.LOG.error("XML\n{}", str);

            JAXBContext j = JAXBContext.newInstance(classesToBind);
            return j.createUnmarshaller().unmarshal(new ByteArrayInputStream(str.getBytes()));
        }
    }

    /**
     * Unmarshal XML data from the specified InputStream and return the
     * resulting content tree. Validation event location information may be
     * incomplete when using this form of the unmarshal API.
     *
     * <p>
     * Implements <a href="#unmarshalGlobal">Unmarshal Global Root Element</a>.
     *
     * @param <T>
     * @param stream      the InputStream to unmarshal XML data from
     * @param classToBind
     *
     * @return the newly created root object of the java content tree
     *
     * @throws IOException
     * @throws JAXBException            If any unexpected errors occur while unmarshalling
     * @throws IllegalArgumentException If the InputStream parameter is null
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T unmarshalObjectFromStream(InputStream stream, Class<T> classToBind) throws IOException, JAXBException {
        return (T) unmarshalFromStream(stream, classToBind);
    }

    /**
     * Unmarshal XML data from the specified InputStream and return the
     * resulting content tree. Validation event location information may be
     * incomplete when using this form of the unmarshal API.
     *
     * <p>
     * Implements <a href="#unmarshalGlobal">Unmarshal Global Root Element</a>.
     *
     * @param <T>
     * @param stream      the InputStream to unmarshal XML data from
     * @param bindingType
     *
     * @return the newly created root object of the java content tree
     *
     * @throws IOException
     * @throws JAXBException            If any unexpected errors occur while unmarshalling
     * @throws IllegalArgumentException If the InputStream parameter is null
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshalFromStream(InputStream stream, JaxbBindingSet<T> bindingType) throws IOException, JAXBException {
        return (T) unmarshalFromStream(stream, bindingType.getBindings());
    }

    /**
     * Unmarshal XML data from the specified URI and return the resulting
     * content tree.
     *
     * All further documentation copied from {@link javax.xml.bind.Unmarshaller#unmarshal(org.xml.sax.InputSource) }
     *
     * Validation event location information may be incomplete
     * when using this form of the unmarshal API.
     *
     * <p>
     * Implements <a href="#unmarshalGlobal">Unmarshal Global Root Element</a>.
     *
     * @param <T>
     * @param uri         the URI to open a connection to and unmarshal XML data from.
     * @param bindingType A set of classes that this binding should respond to.
     *
     * @return the newly created root object of the java content tree
     *
     * @throws JAXBException            If any unexpected errors occur while unmarshalling
     * @throws IOException              If there was a problem opening the URI. Also
     *                                  encompasses {@linkplain java.net.MalformedURLException}.
     * @throws IllegalArgumentException If the InputStream parameter is null
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshalFromUri(URI uri, JaxbBindingSet<T> bindingType) throws JAXBException, IOException {
        return (T) unmarshalFromStream(uri.toURL().openStream(), bindingType.getBindings());
    }

    private JAXBHelper() {
    }

    public static class JaxbBindingSet<T> {

        private final List<Class<?>> classes;

        /**
         *
         * @param classes
         */
        public JaxbBindingSet(List<Class<?>> classes) {
            this.classes = classes;
        }

        public Class<?>[] getBindings() {
            return classes.toArray(new Class<?>[classes.size()]);
        }

    }

}
