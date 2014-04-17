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
package org.dlect.internal.data.merge.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.persistence.metamodel.SingularAttribute;

public abstract class PartialDataConfiguratorSingleFK<T, FK> implements PartialDataConfigurator<T> {

    public PartialDataConfiguratorSingleFK(SingularAttribute<T, FK> key, FK fk) {
        this.fkMap = ImmutableMap.<SingularAttribute<T, ?>, Object>of(key, fk);
        this.clz = key.getDeclaringType().getJavaType();
        this.fk = fk;
    }

    private final Class<T> clz;
    private final Map<SingularAttribute<T, ?>, Object> fkMap;
    private final FK fk;

    @Override
    public Map<SingularAttribute<T, ?>, Object> getForeignKey() {
        return Maps.newHashMap(fkMap);
    }

    protected FK getKey() {
        return fk;
    }

    @Override
    public Class<T> getObjectClass() {
        return clz;
    }

}
