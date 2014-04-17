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

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author lee
 */
public class TimeHelper {

    public static long ago(int duration, TimeUnit unit) {
        return ago(unit.toMillis(duration));
    }

    public static long ago(long time) {
        return now() - time;
    }

    public static long in(int duration, TimeUnit unit) {
        return in(unit.toMillis(duration));
    }

    public static long in(long time) {
        return now() + time;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    private TimeHelper() {
    }

}
