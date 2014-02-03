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

import java.util.List;
import org.dlect.except.DLectException;
import org.dlect.export.Status;
import org.dlect.object.ResultType;
import org.dlect.object.StatusBuilder;

import static com.google.common.collect.ImmutableList.of;

/**
 *
 * @author lee
 */
public class ExceptionToStatus {
    public static Status fromException(Throwable e) {
        
        ResultType r = ResultType.INTERNAL_ERROR;
        List<String> errorMessages = of("An unknown error occured");
        if (e instanceof DLectException) {
            DLectException de = (DLectException) e;
            r = de.getStatusError();
            errorMessages = de.getErrors();
        }
        
        return StatusBuilder.builder(r).setErrors(errorMessages).build();        
    }
}
