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
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsEqual;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author lee
 */
public final class OrderedPcsBuilder {

    private int max;
    private int current = 0;
    private final List<PceMatcher> validPCE = new ArrayList<PceMatcher>();
    private final Listener listener = new Listener();

    protected OrderedPcsBuilder() {
        this(Integer.MAX_VALUE);
    }

    protected OrderedPcsBuilder(int max) {
        this.max = max;
    }

    public OrderedPcsBuilder addEvent(Object source, String name, Object old, Object newO) {
        return addMatchedEvent(new IsEqual<Object>(source),
                               new IsEqual<String>(name),
                               new IsEqual<Object>(old),
                               new IsEqual<Object>(newO));
    }

    public OrderedPcsBuilder addEvent(String name, Object old, Object newO) {
        return addMatchedEvent(new IsEqual<String>(name),
                               new IsEqual<Object>(old),
                               new IsEqual<Object>(newO));
    }

    /**
     * Adds an event using the same {@code source} and {@code name} that uses
     * the previous matching event's {@code newValue}. If no previous events
     * match the name, any {@code previous} is used.
     * @param name Name to match events against.
     * @param newO New value to check for.
     * @return
     */
    public OrderedPcsBuilder addFollowingEvent(Object source, String name, Object newO) {
        return addFollowingMatchedEvent(new IsEqual<Object>(source),
                                        new IsEqual<String>(name),
                                        new IsEqual<Object>(newO));
    }

    /**
     * Adds an event using the same name as {@code name} that uses the previous
     * event's {@code newValue}. If no previous events match the name, any
     * {@code source} and {@code previous} is used.
     * @param name Name to match events against.
     * @param newO New value to check for.
     * @return
     */
    public OrderedPcsBuilder addFollowingEvent(String name, Object newO) {
        return addFollowingMatchedEvent(new IsEqual<String>(name), new IsEqual<Object>(newO));
    }

    public OrderedPcsBuilder addFollowingEvent(Object newO) {
        return addFollowingMatchedEvent(new IsEqual<Object>(newO));
    }

    public OrderedPcsBuilder addMatchedEvent(Matcher<?> source, Matcher<String> name, Matcher<?> old, Matcher<?> newM) {
        validPCE.add(new PceMatcher(source, name, old, newM));
        return this;
    }

    private OrderedPcsBuilder addMatchedEvent(Matcher<String> name, Matcher<?> old, Matcher<?> newM) {
        for (int i = validPCE.size() - 1; i >= 0; i--) {
            PceMatcher l = validPCE.get(i);
            return addMatchedEvent(l.getSource(), name, old, newM);
        }
        return addMatchedEvent(new IsAnything<Object>("Event Source"), name, old, newM);
    }

    private OrderedPcsBuilder addFollowingMatchedEvent(Matcher<Object> source, Matcher<String> name, Matcher<Object> newM) {
        for (int i = validPCE.size() - 1; i >= 0; i--) {
            PceMatcher l = validPCE.get(i);
            if (l.getSource().equals(source) && l.getName().equals(name)) {
                return addMatchedEvent(l.getSource(), name, l.getOld(), newM);
            }
        }
        return addMatchedEvent(new IsAnything<Object>("Event Source"), name, new IsAnything<Object>("Old Value"), newM);
    }

    private OrderedPcsBuilder addFollowingMatchedEvent(IsEqual<String> name, IsEqual<Object> newM) {
        for (int i = validPCE.size() - 1; i >= 0; i--) {
            PceMatcher l = validPCE.get(i);
            if (l.getName().equals(name)) {
                return addMatchedEvent(l.getSource(), name, l.getOld(), newM);
            }
        }
        return addMatchedEvent(new IsAnything<Object>(), name, new IsAnything<Object>(), newM);
    }

    private OrderedPcsBuilder addFollowingMatchedEvent(IsEqual<Object> newM) {
        for (int i = validPCE.size() - 1; i >= 0; i--) {
            PceMatcher l = validPCE.get(i);
            return addMatchedEvent(l.getSource(), l.getName(), l.getNew(), newM);
        }
        return addMatchedEvent(new IsAnything<Object>(), new IsAnything<String>(), new IsAnything<Object>(), newM);
    }

    /**
     * Takes last {@linkplain #addEvent(java.lang.Object, java.lang.String, java.lang.Object, java.lang.Object) } or {@linkplain #addMatchedEvent(org.hamcrest.Matcher, org.hamcrest.Matcher, org.hamcrest.Matcher, org.hamcrest.Matcher) } and repeats it {@code times} times
     * @param times The number of times to repeat the last event. If {@code times < 2} then does nothing.
     * @return this {@link OrderedPcsBuilder}
     */
    public OrderedPcsBuilder times(int times) {
        PceMatcher get = validPCE.get(validPCE.size() - 1);
        for (int i = 1; i < times; i++) {
            validPCE.add(get);
        }
        return this;
    }

    public PropertyChangeListener build() {
        if (max != Integer.MAX_VALUE) {
            assertEquals("Too Many or Too Few event matchers", max, validPCE.size());
        } else {
            max = validPCE.size();
        }
        return listener;
    }

    public void assertAllInteractions() {
        assertEquals("Too Many or Too Few event firings", max, current);
    }

    public int getEventCount() {
        return current;
    }

    private class Listener implements PropertyChangeListener {

        public Listener() {
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (current >= max || current >= validPCE.size()) {
                fail("Property Change fired too many times(max=" + max + ", This fire: " + evt);
            }
            if (!validPCE.get(current).checkEvent(evt)) {
                // Woops
                fail("The event \"" + evt + "\" is not valid. The matcher " + validPCE.get(current) + " (at " + current + ") has rejected it.\nMatchers:\n\t" + validPCE);
            }
            current++;
        }
    }
}
