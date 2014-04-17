/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lee.echo360.test.builders;

import java.beans.PropertyChangeEvent;
import org.hamcrest.Matcher;

/**
 *
 * @author lee
 */
class PceMatcher {

    private final Matcher<?> source;
    private final Matcher<String> name;
    private final Matcher<?> old;
    private final Matcher<?> newM;

    protected PceMatcher(Matcher<?> source, Matcher<String> name, Matcher<?> old, Matcher<?> newM) {
        assert source != null;
        assert name != null;
        assert old != null;
        assert newM != null;
        this.source = source;
        this.name = name;
        this.old = old;
        this.newM = newM;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 59 * hash + (this.old != null ? this.old.hashCode() : 0);
        hash = 59 * hash + (this.newM != null ? this.newM.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            if (obj instanceof PropertyChangeEvent) {
                return checkEvent((PropertyChangeEvent) obj);
            }
            return false;
        }
        final PceMatcher other = (PceMatcher) obj;
        if (this.source != other.source && (this.source == null || !this.source.equals(other.source))) {
            return false;
        }
        if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        if (this.old != other.old && (this.old == null || !this.old.equals(other.old))) {
            return false;
        }
        if (this.newM != other.newM && (this.newM == null || !this.newM.equals(other.newM))) {
            return false;
        }
        return true;
    }

    public boolean checkEvent(PropertyChangeEvent p) {
        if (!source.matches(p.getSource())) {
            return false;
        }
        if (!name.matches(p.getPropertyName())) {
            return false;
        }
        if (!old.matches(p.getOldValue())) {
            return false;
        }
        if (!newM.matches(p.getNewValue())) {
            return false;
        }
        return true;
    }

    public Matcher<?> getSource() {
        return source;
    }

    public Matcher<?> getNew() {
        return newM;
    }

    public Matcher<?> getOld() {
        return old;
    }

    public Matcher<String> getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Matches: {source=" + source + ", name=" + name + ", old=" + old + ", newM=" + newM + "}\n";
    }
}
