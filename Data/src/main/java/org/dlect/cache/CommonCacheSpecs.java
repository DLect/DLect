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

package org.dlect.cache;

import com.google.common.cache.CacheBuilderSpec;

/**
 *
 * @author lee
 */
public class CommonCacheSpecs {

    /**
     * This specification is for a short term cache whose timer does not get re-set on each read, only on write. Use this specification to
     * build caches that hold 'hard' to aquire data for a short period of time before invalidating it. This specification does not use Weak
     * or Soft keys or values as the objects stored are not likely to be stored else-where and are only stored over the short term.
     */
    public static final CacheBuilderSpec SHORT_TERM_WRITE_SPEC = CacheBuilderSpec.parse("expireAfterWrite=30m");

    private CommonCacheSpecs() {
    }

}
