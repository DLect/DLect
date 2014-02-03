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
package org.dlect.ejb.internal;

public abstract class AbstractPrioritisable<T extends Prioritisable<T>> implements Prioritisable<T> {

    private final int priority;

    public AbstractPrioritisable(int priority) {
        this.priority = priority;
    }

    public AbstractPrioritisable() {
        this.priority = 0;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * This method guarantees that if the the parameters are not equal, but have
     * the same priority then they are consitantly sorted and not declared
     * equal.
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(T o) {
        if (o == null) {
            return -1;
        }
        if (this.equals(o)) {
            return 0;
        }

        // Lower values come first, so reverse the sort.
        int r = -Integer.compare(this.getPriority(), o.getPriority());
        if (r == 0) {
            r = this.getClass().getName().compareTo(o.getClass().getName());
            // If they are the same priority, sort on class name.
            if (r == 0) {
                r = Integer.compare(this.hashCode(), o.hashCode());
                // Last ditch attempt. If this fails then I just give up.
            }
        }
        return r;
    }

}
