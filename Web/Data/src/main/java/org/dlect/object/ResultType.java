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
package org.dlect.object;

/**
 *
 * @author lee
 */
public enum ResultType {

    /**
     * The response was as expected.
     */
    OK,
    /**
     * A User input was incorrect.
     */
    BAD_INPUT,
    /**
     * An error caused by DLect application such as no database.
     */
    INTERNAL_ERROR,
    /**
     * An error in data returned from a request to the university. This includes
     * any unexpected XML formatting ect. This does not include the university
     * not supporting the opperation.
     */
    UNIVERSITY_ERROR,
    /**
     * This indicates that either DLect or the chosen university does not
     * support this operation.
     */
    NOT_SUPPORTED,
    /**
     * The user is not logged in.
     */
    NOT_LOGGED_IN,
    /**
     * The user's credentials were incorrect.
     */
    BAD_CREDENTIALS,
}
