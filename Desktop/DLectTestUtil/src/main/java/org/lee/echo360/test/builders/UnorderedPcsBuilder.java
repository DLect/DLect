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
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
public final class UnorderedPcsBuilder {

    private final int max;
    private int current = 0;
    private final Set<PceMatcher> validPCE = new HashSet<PceMatcher>();
    private final Listener listener = new Listener();

    protected UnorderedPcsBuilder() {
        this(Integer.MAX_VALUE);
    }

    protected UnorderedPcsBuilder(int max) {
        this.max = max;
    }

    public UnorderedPcsBuilder addEvent(Object source, String name, Object old, Object newO) {
        return addMatchedEvent(new IsEqual<Object>(source),
                               new IsEqual<String>(name),
                               new IsEqual<Object>(old),
                               new IsEqual<Object>(newO));
    }

    public UnorderedPcsBuilder addMatchedEvent(Matcher<?> source, Matcher<String> name, Matcher<?> old, Matcher<?> newM) {
        validPCE.add(new PceMatcher(source, name, old, newM));
        return this;
    }

    public PropertyChangeListener build() {
        return listener;
    }

    private class Listener implements PropertyChangeListener {

        public Listener() {
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (!validPCE.contains(evt)) {
                // Woops
                fail("The event \"" + evt + "\" is not valid.\nMatchers:\n\t" + validPCE);
            }
            if (current >= max) {
                fail("Property Change fired too many times(max=" + max + ", This fire: " + evt);
            }
            current++;
        }
    }
}
