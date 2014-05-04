/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.migration.version;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import java.util.List;
import org.dlect.helper.Conditions;

/**
 *
 * @author lee
 */
public class Version implements Comparable<Version> {

    private final ImmutableList<Integer> versionNumber;

    public Version(List<Integer> versionNumber) {
        Conditions.checkNonNull(versionNumber, "Version Number");
        Conditions.checkTrue(versionNumber.size() >= 1, "Version Number list is empty");
        for (int i = 0; i < versionNumber.size(); i++) {
            int v = versionNumber.get(i);
            Conditions.checkTrue(v >= 0, "The " + i + "th element of the list [" + versionNumber.toString()
                                         + "] (" + v + ") was negative.");
        }
        this.versionNumber = ImmutableList.copyOf(versionNumber);
    }

    public Version(int... versionNumber) {
        this(Ints.asList(Conditions.checkNonNull(versionNumber, "Version Number")));
    }

    @Override
    public int compareTo(Version o) {
        ImmutableList<Integer> thisVer = this.getVersionNumbers();
        ImmutableList<Integer> otherVer = o.getVersionNumbers();

        boolean inverse = thisVer.size() > otherVer.size();
        int multiplier = inverse ? -1 : 1;

        ImmutableList<Integer> shorter = inverse ? otherVer : thisVer;
        ImmutableList<Integer> longer = inverse ? thisVer : otherVer;
        int i = 0;
        for (; i < shorter.size(); i++) {
            Integer s = shorter.get(i);
            Integer l = longer.get(i);

            int cmp = s.compareTo(l);

            if (cmp != 0) {
                return multiplier * cmp;
            }
        }

        Integer z = 0;

        for (; i < longer.size(); i++) {
            int cmp = z.compareTo(longer.get(i));

            if (cmp != 0) {
                return multiplier * cmp;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Version other = (Version) obj;

        return this.compareTo(other) == 0;
    }

    public ImmutableList<Integer> getVersionNumbers() {
        return ImmutableList.copyOf(versionNumber);
    }

    public String getVersionString() {
        return Joiner.on('.').join(versionNumber);
    }

    /**
     * Hash code is consistent with equals.
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        for (Integer i : versionNumber) {
            hash += 17 * i;
        }
        return hash;
    }

    @Override
    public String toString() {
        return getClass() + "[" + getVersionString() + "]";
    }

    /**
     * This implementation is strict. Any invalid characters, multiple dots in a row and an initial dot will cause this
     * to throw an illegal argument exception. To parse a version in laissez faire mode, call {@link
     *
     * @param versionString
     *
     * @return
     */
    public static Version parseVersion(String versionString) {
        return parseVersion(versionString, true);
    }

    /**
     * This implementation is strict. Any invalid characters, multiple dots in a row and an initial dot will cause this
     * to throw an illegal argument exception. To parse a version in laissez faire mode, call {@link
     *
     * @param versionString
     *
     * @return
     */
    public static Version parseVersionNonStrict(String versionString) {
        return parseVersion(versionString, false);
    }

    /**
     * This implementation is very laissez faire. The version string can be riddled with invalid characters and still
     * produce a
     *
     * @param versionString
     * @param strictMode
     *
     * @return
     */
    protected static Version parseVersion(String versionString, boolean strictMode) {
        // REmove everything except dots and numbers
        String cleaned = versionString;
        if (!strictMode) {
            cleaned = cleaned.replaceAll("[^0-9\\.]", "");
        }

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Empty string after cleaning. Input String: \"" + versionString + "\"");
        }

        if (!strictMode && cleaned.startsWith(".")) {
            cleaned = "0" + cleaned;
        }

        String[] versionNums = cleaned.split("\\.");
        List<Integer> versionInts = Lists.newArrayList();
        for (String v : versionNums) {
            Integer i = Ints.tryParse(v);
            if (i == null) {
                if (strictMode) {
                    throw new IllegalArgumentException("Failed to parse the version string \"" + versionString
                                                       + "\" as a substring failed to parse(Substring: \"" + v + "\"");
                }
                continue;
            }
            if (strictMode && i < 0) {
                throw new IllegalArgumentException("Failed to parse the version string \"" + versionString
                                                   + "\" as a substring returned a negative number(Substring: \""
                                                   + v + "\"");
            }
            versionInts.add(i);
        }
        return new Version(versionInts);
    }
}
