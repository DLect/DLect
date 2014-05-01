/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.migration.version.range;

import com.google.common.collect.ImmutableSortedSet;
import java.util.Set;
import org.dlect.migration.version.Version;

import static org.dlect.helper.Conditions.*;


/**
 *
 * @author lee
 */
public class InListVersionRange implements VersionRange {

    private final ImmutableSortedSet<Version> versions;
    
    public InListVersionRange(ImmutableSortedSet<Version> v) {
        versions = checkNonEmpty(v, "Version");
    }
    
    public InListVersionRange(Set<Version> v) {
        this(ImmutableSortedSet.copyOf(checkNonNull(v, "Version")));
    }

    @Override
    public Version firstVersion() {
        return versions.first();
    }

    @Override
    public Version lastVersion() {
        return versions.last();
    }

    @Override
    public boolean contains(Version v) {
        return versions.contains(v);
    }
    
    
    
}
