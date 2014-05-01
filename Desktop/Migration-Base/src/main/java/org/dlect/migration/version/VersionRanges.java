/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.migration.version;

import org.dlect.migration.version.range.SubVersionRange;
import org.dlect.migration.version.range.InListVersionRange;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.dlect.migration.version.range.VersionRange;

import static org.dlect.helper.Conditions.*;

/**
 *
 * @author lee
 */
public class VersionRanges {
    
    public static VersionRange oneOf(Version v, Version... versions) {
        return oneOf(ImmutableSet.<Version>builder().add(v).add(versions).build());
    }
    public static VersionRange oneOf(Iterable<Version> versions) {
        checkNonNull(versions, "Versions");
        Set<Version> v = ImmutableSet.copyOf(versions);
        checkNonEmpty(v, "Versions(As a set)");
        
        return new InListVersionRange(v);
    }
    
    public static VersionRange subVersion(Version parent) {
        checkNonNull(parent, "Version");
        
        return new SubVersionRange(parent);
    }
    
    
    
}
