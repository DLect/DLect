/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.matcher;

import java.util.Objects;
import org.dlect.events.DataEvent;
import org.dlect.events.DataListEvent;
import org.dlect.events.EventMatcher;

/**
 * This class filters {@linkplain DataEvents} based on the given classes <b>equality</b> to
 * {@linkplain DataEvent#getOriginClass() }.
 *
 * @author lee
 */
public class DataListClassEventMatcher implements EventMatcher<DataListEvent> {

    private final Class<?> clz;

    public DataListClassEventMatcher(Class<?> clz) {
        if (clz == null) {
            throw new IllegalArgumentException("Null Class for Event Matcher");
        }
        this.clz = clz;
    }

    @Override
    public boolean supports(DataListEvent e) {
        // Assuming that there are no A extends B cases. If so - then write your own damn matcher.
        return clz.equals(e.getOriginClass());
    }

    public Class<?> getMatchingClass() {
        return clz;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.clz);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataListClassEventMatcher other = (DataListClassEventMatcher) obj;
        return Objects.equals(this.getMatchingClass(), other.getMatchingClass());
    }

}
