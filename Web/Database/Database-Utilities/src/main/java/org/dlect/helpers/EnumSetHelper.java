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

import java.util.EnumSet;

/**
 *
 * @author lee
 */
public class EnumSetHelper {

    public static <E extends Enum<E>> EnumSet<E> without(EnumSet<E> ew, E exclude) {
        EnumSet<E> ret = ew.clone();
        ret.remove(exclude);
        return ret;
    }

    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> without(EnumSet<E> ew, E exclude, E... excludes) {
        EnumSet<E> ret = ew.clone();
        ret.removeAll(EnumSet.of(exclude, excludes));
        return ret;
    }

    public static <E extends Enum<E>> EnumSet<E> with(EnumSet<E> ew, E exclude) {
        EnumSet<E> ret = ew.clone();
        ret.add(exclude);
        return ret;
    }

    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> with(EnumSet<E> ew, E exclude, E... excludes) {
        EnumSet<E> ret = ew.clone();
        ret.addAll(EnumSet.of(exclude, excludes));
        return ret;
    }
    
    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> retain(EnumSet<E> ew, E exclude, E... excludes) {
        EnumSet<E> ret = ew.clone();
        ret.retainAll(EnumSet.of(exclude, excludes));
        return ret;        
    }

    
    
    
    
    private EnumSetHelper() {
    }

}
