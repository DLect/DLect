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
import java.util.Collections;
import java.util.List;
import org.dlect.log.Stores;
import org.dlect.object.ResultType;

/**
 *
 * @author lee
 */
public class DLectExceptionBuilder {

    public static final String DEFAULT_ERROR_MESSAGE = "An Unknown error occured";

    public static DLectException rethrow(String string, DLectException ex) {
        return DLectExceptionBuilder
                .builder()
                .setCause(ex)
                .setMessage(string)
                .setErrorMessages(ex.getErrors())
                .setResult(ex.getStatusError())
                .build();
    }

    public static DLectException rethrow(String message, ResultType fault, Throwable ex) {
        if (ex.getCause() instanceof DLectException) {
            return DLectExceptionBuilder.rethrow(message, (DLectException) ex.getCause());
        }
        return DLectExceptionBuilder.build("Get Semester Failed", ex, fault, DEFAULT_ERROR_MESSAGE);
    }

    private List<String> errors;
    private ResultType result;
    private Throwable cause;
    private String message;

    public static DLectExceptionBuilder builder() {
        return new DLectExceptionBuilder();
    }

    public static DLectException build(ResultType result, String... error) {
        return builder().setResult(result).setErrorMessages(error).build();
    }

    public static DLectException build(String message, ResultType result, String... error) {
        return builder().setMessage(message).setResult(result).setErrorMessages(error).build();
    }

    public static DLectException build(String message, Throwable t, ResultType result, String... error) {
        return builder().setMessage(message).setCause(t).setResult(result).setErrorMessages(error).build();
    }

    protected DLectExceptionBuilder() {
    }

    public DLectExceptionBuilder setResult(ResultType result) {
        if (result == null || result == ResultType.OK) {
            Stores.LOG.error("WARNING: Result type is not valid for an exception - please review the causing code(RT: " + result + ")", new IllegalArgumentException());
        }
        this.result = result;
        return this;
    }

    public DLectExceptionBuilder setErrorMessages(String... errors) {
        this.errors = Lists.newArrayList(errors);
        return this;
    }

    public DLectExceptionBuilder setErrorMessages(List<String> errors) {
        this.errors = Lists.newArrayList(errors);
        return this;
    }

    public DLectExceptionBuilder addErrorMessages(String... errors) {
        if (this.errors == null) {
            setErrorMessages(errors);
        } else {
            Collections.addAll(this.errors, errors);
        }
        return this;
    }

    public DLectExceptionBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public DLectExceptionBuilder setCause(Throwable t) {
        this.cause = t;
        return this;
    }

    public DLectException build() {
        if (result == null) {
            Stores.LOG.error("WARNING: Result type is not defined for this exception - using default.", new IllegalArgumentException());
            this.result = ResultType.INTERNAL_ERROR;
        }
        if (this.errors == null || this.errors.isEmpty()) {
            Stores.LOG.error("WARNING: No error messages defined - using default.", new IllegalArgumentException());
            this.errors = Lists.newArrayList(DEFAULT_ERROR_MESSAGE);
        }

        if (cause == null) {
            if (message == null) {
                return new DLectException(result, errors);
            } else {
                return new DLectException(message, result, errors);
            }
        } else {
            if (message == null) {
                return new DLectException(cause, result, errors);
            } else {
                return new DLectException(message, cause, result, errors);
            }
        }
    }

}
