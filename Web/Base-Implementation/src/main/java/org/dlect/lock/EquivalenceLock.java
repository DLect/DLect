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
package org.dlect.lock;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronize "on an equivalence class"; i.e., if you wish to lock not a
 * specific string, but anything that equals that string, you may
 *
 * <pre>EquivalenceLock<String> equivalenceLock = new EquivalenceLock<String>();
 * equivalenceLock.lock("frank");
 * try {
 * // whatever
 * } finally {
 * equivalenceLock.release("frank");
 * }</pre>
 *
 * @author Jonathan Feinberg &lt;jdf@pobox.com&gt;
 * @param <T>
 */
public class EquivalenceLock<T> {

    private static final Logger LOG = LoggerFactory.getLogger(EquivalenceLock.class.getName());

    private final Set<T> slots = new HashSet<>();

    public void lock(final T ticket) throws InterruptedException {
        final String threadName = Thread.currentThread().getName();
        LOG.trace(threadName + " acquiring lock on tickets");

        synchronized (slots) {
            LOG.trace(threadName + " acquired lock on tickets");

            while (slots.contains(ticket)) {
                LOG.trace(threadName + " waiting to toss " + ticket);
                slots.wait();
            }

            LOG.trace(threadName + " accepting " + ticket);
            slots.add(ticket);
        }
    }

    public void lockSafe(final T ticket) {
        while (true) {
            try {
                lock(ticket);
                return;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.trace("Exception caught in lockSafe", e);
            }
        }
    }

    public void release(final T ticket) {
        synchronized (slots) {
            LOG.trace(Thread.currentThread().getName() + " tossing " + ticket);

            slots.remove(ticket);
            slots.notifyAll();
        }
    }
}
