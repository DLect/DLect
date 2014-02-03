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
package org.dlect.object;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import org.dlect.export.Status;

/**
 *
 * @author lee
 * @param <T>
 */
@Deprecated
public class StatusSet<T> extends ForwardingSet<T> {

    private final Set<T> delegate;

    private Status status;

    public StatusSet(Status status) {
        this();
        this.status = status;
    }

    public StatusSet() {
        delegate = Sets.newHashSet();
    }

    public StatusSet(Collection<T> delegate) {
        this.delegate = Sets.newHashSet(delegate);
    }

    public StatusSet(Collection<T> delegate, Status status) {
        this(delegate);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    protected Set<T> delegate() {
        return delegate;
    }

}
