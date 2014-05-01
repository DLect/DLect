/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.migration.version.range;

import org.dlect.helper.Conditions;
import org.dlect.migration.version.Version;

/**
 *
 * @author lee
 */
public class SubVersionRange implements VersionRange {

    private final Version parent;

    public SubVersionRange(Version parent) {
        this.parent = Conditions.checkNonNull(parent, "Parent");
    }

    @Override
    public boolean contains(Version v) {
        Conditions.checkNonNull(v, "Contains Version");
        return v.isSubversion(parent);
    }

    @Override
    public Version firstVersion() {
        return parent;
    }

    @Override
    public Version lastVersion() {
        return null;
    }

}
