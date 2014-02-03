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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lee
 */
public class RegularExpressionHelper {

    /**
     *
     * @param p
     * @param s
     * @param group
     *
     * @return The match or throws an exception if fails.
     */
    public static String findGroup(Pattern p, String s, int group) throws IllegalStateException{
        Matcher m = p.matcher(s);
        m.find();
        return m.group(group);
    }
    /**
     *
     * @param p
     * @param s
     *
     * @return The match or throws an exception if fails.
     */
    public static String find(Pattern p, String s) {
        Matcher m = p.matcher(s);
        m.find();
        return m.group();
    }
    /**
     *
     * @param p
     * @param s
     *
     * @return The match or throws an exception if fails.
     */
    public static Matcher findMatcher(Pattern p, String s) {
        Matcher m = p.matcher(s);
        m.find();
        return m;
    }
    
}
