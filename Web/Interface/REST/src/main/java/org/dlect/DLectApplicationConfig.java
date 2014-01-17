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
package org.dlect;

import javax.ws.rs.ApplicationPath;
import org.dlect.config.DLectExceptionResponseConfig;
import org.dlect.config.HeaderConfig;
import org.dlect.config.ServeletExceptionResponseConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author lee
 */
@ApplicationPath("/rest")
public class DLectApplicationConfig extends ResourceConfig {


    public DLectApplicationConfig() {
        this.packages("org.glassfish.jersey.examples.multipart").register(MultiPartFeature.class);
        this.packages("org.dlect");
        this.packages("org.dlect.config").register(HeaderConfig.class).register(DLectExceptionResponseConfig.class).register(ServeletExceptionResponseConfig.class);
    }

}
