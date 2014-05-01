/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.migration.version.range;

import org.dlect.migration.version.Version;

/**
 *
 * If a non-null version is returned by both {@linkplain #firstVersion()} and {@linkplain #lastVersion()}, then
 * {@code firstVersion() <= lastVersion()}.
 *
 * @author lee
 */
public interface VersionRange {

    /**
     *
     * @return The first version that this range can match or {@code null} if there is no lower bound or the lower bound
     *         is undefinable.
     */
    public Version firstVersion();

    /**
     *
     *
     * @return The last version that this range can match or {@code null} if there is no upper bound or the upper bound
     *         is undefinable.
     */
    public Version lastVersion();

    /**
     * Test if the version is inside this range. Must conform to the following conditions:
     *
     * <ul>
     * <li>{@code if (firstVersion() != null) } then any version {@code v < firstVersion()} must cause this method to
     * return {@code false}.</li>
     * <li>{@code if (firstVersion() != null) } then {@code firstVersion()} must cause this method to return
     * {@code true}.</li>
     * <li>{@code if (lastVersion() != null) } then any version {@code v > lastVersion()} must cause this method to
     * return {@code false}.</li>
     * <li>{@code if (lastVersion() != null) } then {@code lastVersion()} must cause this method to return
     * {@code true}.</li>
     * </ul>
     *
     * For values between {@code firstVersion()} and {@code lastVersion()}, the return value is defined by the
     * implementer.
     *
     * @param v
     *
     * @return {@code true} if this range contains this version. The implementer will define how this is implemented.
     */
    public boolean contains(Version v);

}
