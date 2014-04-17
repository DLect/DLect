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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TimeZone;

/**
 *
 * @author lee
 */
public class DataHelpers {

    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static Calendar getZeroCalendar() {
        Calendar c = new GregorianCalendar(UTC);
        c.clear();

        // Calculate the fields before returning - it's the nice thing to do.
        c.getTime();
        return c;
    }

    public static final Date wrapDate(Date d) {
        return d == null ? null : new Date(d.getTime());
    }

    public static final Date wrapDate(Date d, Date ifNull) {
        return d == null ? (ifNull == null ? null : new Date(ifNull.getTime())) : new Date(d.getTime());
    }

    public static final Date wrapDate(Date d, long ifNull) {
        return d == null ? new Date(ifNull) : new Date(d.getTime());
    }

    public static Date copy(Date d) {
        return wrapDate(d);
    }

    public static Date copy(Date d, Date ifNull) {
        return wrapDate(d, ifNull);
    }
    public static Date copy(Date d, long ifNull) {
        return wrapDate(d, ifNull);
    }

    public static final String storeEnum(Enum<?> value) {
        return storeEnum(value, null);
    }

    public static final String storeEnum(Enum<?> value, String ifNull) {
        return value == null ? ifNull : value.name();
    }

    public static final <T extends Enum<T>> T readEnum(Class<T> cls, String s) {
        return readEnum(cls, s, null);

    }

    public static final <T extends Enum<T>> T readEnum(Class<T> cls, String s, T ifInvalid) {
        if (s == null) {
            return ifInvalid;
        }
        if (cls == null) {
            throw new IllegalArgumentException("Class is null - I can't do anything.");
        }
        try {
            return Enum.valueOf(cls, s.trim());
        } catch (IllegalArgumentException e) {
            return ifInvalid;
        }

    }

    private DataHelpers() {
    }

    /**
     *
     * @param <T>
     * @param offerings
     *
     * @return
     *
     * @deprecated Use {@link #wrap(java.util.List) } instead as it is shorter and supports null collections.
     */
    @Deprecated
    public static <T> List<T> wrapList(Collection<T> offerings) {
        return ImmutableList.copyOf(offerings);
    }

    /**
     *
     * @param <T>
     * @param offerings
     *
     * @return
     *
     * @deprecated Use {@link #copy(java.util.List) } instead as it is shorter and supports null collections.
     */
    @Deprecated
    public static <T> List<T> copyList(Collection<T> offerings) {
        return Lists.newArrayList(offerings);
    }

    /**
     *
     * @param <T>
     * @param offerings
     *
     * @return
     *
     * @deprecated Use {@link #wrap(java.util.SortedSet) } instead as it is shorter and supports null collections.
     */
    @Deprecated
    public static <T extends Comparable<T>> SortedSet<T> wrapSortedSet(Collection<T> offerings) {
        return ImmutableSortedSet.copyOf(offerings);
    }

    /**
     *
     * @param <T>
     * @param offerings
     *
     * @return
     *
     * @deprecated Use {@link #copy(java.util.SortedSet) } instead as it is shorter and supports null collections.
     */
    @Deprecated
    public static <T extends Comparable<T>> SortedSet<T> copySortedSet(Collection<T> offerings) {
        return Sets.newTreeSet(offerings);
    }

    public static <T> List<T> wrap(Collection<T> offerings) {
        return offerings == null ? null : wrapList(offerings);
    }

    public static <T> List<T> wrapReplaceNull(Collection<T> offerings) {
        return offerings == null ? ImmutableList.<T>of() : copy(offerings);
    }

    public static <T> List<T> copy(Collection<T> offerings) {
        return offerings == null ? null : copyList(offerings);
    }

    public static <T> List<T> copyReplaceNull(Collection<T> offerings) {
        return offerings == null ? Lists.<T>newArrayList() : copy(offerings);
    }

    public static <K, V> Map<K, V> wrap(Map<K, V> offerings) {
        return offerings == null ? null : ImmutableMap.<K, V>copyOf(offerings);
    }

    public static <K, V> Map<K, V> wrapReplaceNull(Map<K, V> offerings) {
        return offerings == null ? ImmutableMap.<K, V>of() : ImmutableMap.<K, V>copyOf(offerings);
    }

    public static <K, V> Map<K, V> copy(Map<K, V> offerings) {
        return offerings == null ? null : Maps.newHashMap(offerings);
    }

    public static <K, V> Map<K, V> copyReplaceNull(Map<K, V> offerings) {
        return offerings == null ? Maps.<K, V>newHashMap() : Maps.newHashMap(offerings);
    }

}
