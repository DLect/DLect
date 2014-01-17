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

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author lee
 */
public enum UniversitySupport implements Serializable {

    HTTPS(TimeUnit.DAYS), HTTP_AUTH(TimeUnit.DAYS), HTTP(TimeUnit.DAYS), NONE(TimeUnit.HOURS);
    private final long timeout;

    private UniversitySupport(TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(1);
    }

    private UniversitySupport(long num, TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(num);
    }

    public static UniversitySupport toSupport(boolean canSslLogin, boolean httpAuth) {
        if (canSslLogin) {
            return HTTPS;
        } else if (httpAuth) {
            return HTTP_AUTH;
        } else {
            return HTTP;
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public long offsetFromNow() {
        return getTimeout() + System.currentTimeMillis();
    }
}
