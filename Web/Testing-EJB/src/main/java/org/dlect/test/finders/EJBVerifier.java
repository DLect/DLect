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

package org.dlect.test.finders;

import com.google.common.collect.ImmutableSet;
import java.lang.annotation.Annotation;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author lee
 */
public class EJBVerifier {

    private static final Set<Class<? extends Annotation>> VALID_EJB_INTERFACE_ANNOTATIONS = ImmutableSet.of(Local.class, Remote.class);

    public static boolean isEjbInterface(Class<?> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            return false;
        }
        for (Class<? extends Annotation> annotClass : VALID_EJB_INTERFACE_ANNOTATIONS) {
            if (interfaceClass.isAnnotationPresent(annotClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns true if this class is a EJB Interface or if any of this class's parent classes implement an EJB Interface.
     *
     * @param classToTest
     *
     * @return
     */
    public static boolean hasEjbInterface(Class<?> classToTest) {
        if (isEjbInterface(classToTest)) {
            return true;
        }
        Class<?> c = classToTest.getSuperclass();

        // If any of the classes interfaces are EJB interfaces then this will return true.
        for (Class<?> interfaceClass : classToTest.getInterfaces()) {
            if (isEjbInterface(interfaceClass)) {
                return true;
            }
        }

        if (c != null && c != Object.class) {
            if (hasEjbInterface(c)) {
                return true;
            }
        }
        
        return false;
    }

}
