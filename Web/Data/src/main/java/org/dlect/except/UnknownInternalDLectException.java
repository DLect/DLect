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

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.dlect.object.ResultType;

public class UnknownInternalDLectException extends DLectException {

    public static final List<String> UNKNOW_ERROR_DESCRIPTION = ImmutableList.of("An unknown error occured.");

    public UnknownInternalDLectException() {
        super(ResultType.INTERNAL_ERROR, UNKNOW_ERROR_DESCRIPTION);
    }

    public UnknownInternalDLectException(String message) {
        super(message, ResultType.INTERNAL_ERROR, UNKNOW_ERROR_DESCRIPTION);
    }

    public UnknownInternalDLectException(String message, Throwable cause) {
        super(message, cause, ResultType.INTERNAL_ERROR, UNKNOW_ERROR_DESCRIPTION);
    }

    public UnknownInternalDLectException(Throwable cause) {
        super(cause, ResultType.INTERNAL_ERROR, UNKNOW_ERROR_DESCRIPTION);
    }

}
