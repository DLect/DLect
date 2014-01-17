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

package org.dlect.except;

/**
 *
 * @author lee
 */
public class CommonExceptionBuilder {

    public static DLectException getOnFailContractBreachException(String component) {
        return new UnknownInternalDLectException(component + " returned false but did not throw an exception.");
    }

    public static DLectException getOnFailContractBreachException(String function, Object calledOn) {
        return new UnknownInternalDLectException(function + " in " + getDebugFor(calledOn) + " returned false but did not throw an exception.");
    }

    public static DLectException getInvalidObjectStateException(String message, Object component) {
        return new UnknownInternalDLectException(message + "\n  Component: " + getDebugFor(component));
    }

    public static DLectException getIllegalReturnTypeException(String message, Object component, Object returnValue) {
        return new UnknownInternalDLectException(message
                                                 + "\n  Component: " + getDebugFor(component)
                                                 + "\n  Return Value: " + getDebugFor(returnValue));
    }

    public static DLectException getIllegalReturnTypeException(String message, Throwable exception, Object component, Object returnValue) {
        return new UnknownInternalDLectException(message
                                                 + "\n  Component: " + getDebugFor(component)
                                                 + "\n  Return Value: " + getDebugFor(returnValue), exception);
    }

    public static String getDebugFor(Object component) {
        return (component == null ? "()" : ("(Class: " + component.getClass() + ")")) + component;
    }

    private CommonExceptionBuilder() {
    }

}
