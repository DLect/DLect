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
package org.dlect.ejb.internal.provder.impl.blackboard.au.uq.rota.objects;

import com.google.common.base.CaseFormat;
import java.util.regex.Pattern;

/**
 *
 * @author lee
 */
public enum UQCampus {

    ST_LUCIA("St Lucia", "^ST.*"),
    GATTON("Gatton", "^GA.*"),
    IPSWICH("Ipswich", "^IP.*"),
    HERSTON("Herston", "^HE.*"),;
    
    private final Pattern regexp;
    private final String name;
    private UQCampus(String name, String pattern) {
        this.name= name;
this.        regexp = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    public String getDisplayName() {
        return name;
    }

    public boolean matches(String toMatch) {
        return regexp.matcher(toMatch).find();
    }

    public static UQCampus getMatching(String matching) {
        for (UQCampus c : UQCampus.values()) {
            if (c.matches(matching)) {
                return c;
            }
        }
        return null;
    }

}
