/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.matcher;

import org.dlect.events.Event;
import org.dlect.events.EventMatcher;

public class TrueEventMatcher<T extends Event> implements EventMatcher<T> {

    public static final TrueEventMatcher<?> MATCHER = new TrueEventMatcher<>();

    @SuppressWarnings("unchecked")
    public static <M extends Event> TrueEventMatcher<M> getMatcher() {
        return (TrueEventMatcher<M>) MATCHER;
    }

    @Override
    public boolean supports(Event e) {
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TrueEventMatcher;
    }

}
