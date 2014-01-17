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
package org.dlect.test.helper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lee
 */
public class LoggingSetup {

    public static LoggingSetupReset disableLogging(org.slf4j.Logger... loggers) {
        Map<Logger, Level> levels = Maps.newHashMap();

        for (org.slf4j.Logger logger : loggers) {
            Logger l = Logger.getLogger(logger.getName());

            Logger original = l;

            Level oldLevel = original.getLevel();
            while (oldLevel == null) {
                if (l.getParent() != null) {
                    l = l.getParent();
                    oldLevel = l.getLevel();
                } else {
                    // No parent and no levels - PANIC; just assume that the logger is using it's default of INFO
                    oldLevel = Level.INFO;
                    l = original;
                }
            }

            l.setLevel(Level.OFF);
            levels.put(l, oldLevel);
        }
        return new LoggingSetupReset(levels);
    }

    public static class LoggingSetupReset {

        private final Map<Logger, Level> levels;

        // Private constructor only
        private LoggingSetupReset(Map<Logger, Level> levels) {
            this.levels = ImmutableMap.copyOf(levels);
        }

        public void reset() {
            for (Entry<Logger, Level> entry : levels.entrySet()) {
                Logger logger = entry.getKey();
                Level level = entry.getValue();
                logger.setLevel(level);
            }
        }

    }

}
