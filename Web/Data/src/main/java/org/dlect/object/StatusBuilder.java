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

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.dlect.export.Status;

import static org.dlect.helpers.DataHelpers.copy;

/**
 *
 * @author lee
 */
@Deprecated
public class StatusBuilder {

    public static Status ok() {
        return builder(ResultType.OK).build();
    }

    @Deprecated
    public static StatusBuilder builder(ResultType result) {
        return new StatusBuilder(result);
    }

    @Deprecated
    public static StatusBuilder builderFrom(Status status) {
        StatusBuilder b = builder(status.getStatus());
        b.setErrors(status.getErrors()).setWarnings(status.getWarnings());
        return b;
    }

    @Deprecated
    public static Status build(ResultType result) {
        return builder(result).build();
    }

    @Deprecated
    public static Status buildFrom(Status result) {
        return builderFrom(result).build();
    }

    private final ResultType status;
    private List<String> errors;
    private List<String> warnings;

    public StatusBuilder(ResultType status) {
        this.status = status;
    }

    public StatusBuilder setErrors(List<String> errors) {
        this.errors = copy(errors);
        return this;
    }

    public StatusBuilder setWarnings(List<String> warnings) {
        this.warnings = copy(warnings);
        return this;
    }

    public StatusBuilder addErrors(String... errors) {
        if (this.errors == null) {
            this.errors = Lists.newArrayList(errors);
        } else {
            Collections.addAll(this.errors, errors);
        }
        return this;
    }

    public StatusBuilder addWarnings(String... warnings) {
        if (this.warnings == null) {
            this.warnings = Lists.newArrayList(warnings);
        } else {
            Collections.addAll(this.warnings, warnings);
        }
        return this;
    }

    public Status build() {
        return new Status(status, errors, warnings);
    }

}
