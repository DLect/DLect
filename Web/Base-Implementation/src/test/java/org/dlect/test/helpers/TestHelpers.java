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
package org.dlect.test.helpers;

import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;
import org.dlect.except.NoSupportingProvidersException;
import org.dlect.export.LoginResult;
import org.dlect.object.StatusBuilder;
import org.dlect.object.ResultType;

/**
 *
 * @author Lee
 */
public class TestHelpers {

    public static final String TEST_USERNAME = "Username!";
    public static final String TEST_PASSWORD = "Password!";

    public static final Class[] ABORT_EXCEPTION = {AbortExecutionException.class};
    public static final Class[] NO_SUPPORT_EXCEPTION = {NoSupportingProvidersException.class};
    public static final Class[] IO_EXCEPTION = {IOException.class};

    public static LoginResult createLoginResult(ResultType res) {
        return new LoginResult(StatusBuilder.builder(res).build());
    }

    public static LoginResult createUniqueLoginResult(ResultType res) {
        StatusBuilder builder = StatusBuilder.builder(res);
        for (int i = 0; i < 10; i++) {
            builder.addWarnings(RandomStringUtils.randomAlphanumeric(20));
            builder.addErrors(RandomStringUtils.randomAlphanumeric(20));
        }
        return new LoginResult(builder.build());
    }

    public static final class AbortExecutionException extends RuntimeException {
    }

    private TestHelpers() {
    }
}
