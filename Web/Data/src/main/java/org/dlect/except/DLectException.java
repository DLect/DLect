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

import com.google.common.collect.Lists;
import java.util.List;
import org.dlect.object.ResultType;

import static org.dlect.helpers.DataHelpers.copyReplaceNull;
import static org.dlect.helpers.DataHelpers.wrap;

/**
 *
 * @author lee
 */
public class DLectException extends Exception {

    private final List<String> errors;
    private final ResultType result;

    public DLectException() {
        super();
        this.errors = Lists.newArrayList();
        this.result = ResultType.INTERNAL_ERROR;
    }

    public DLectException(ResultType result, List<String> errors) {
        super();
        this.errors = copyReplaceNull(errors);
        this.result = result;
    }

    public DLectException(String message, ResultType result, List<String> errors) {
        super(message);
        this.errors = copyReplaceNull(errors);
        this.result = result;
    }

    public DLectException(String message, Throwable cause, ResultType result, List<String> errors) {
        super(message, cause);
        this.errors = copyReplaceNull(errors);
        this.result = result;
    }

    public DLectException(Throwable cause, ResultType result, List<String> errors) {
        super(cause);
        this.errors = copyReplaceNull(errors);
        this.result = result;
    }

    public List<String> getErrors() {
        return wrap(this.errors);
    }

    public ResultType getStatusError() {
        return result;
    }

}
