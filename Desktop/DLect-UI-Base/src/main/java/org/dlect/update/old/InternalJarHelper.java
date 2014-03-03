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
package org.dlect.update.old;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author lee
 */
public class InternalJarHelper {

    protected Process exec(String... cmds) throws IOException {
        return new ProcessBuilder(cmds).start();
    }

    protected String getCurrentJarLocation() {
        return URLDecoder.decode(InternalJarExecuter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    protected void copy(URL from, File to) throws IOException {
        IOUtils.copy(from.openStream(), new FileOutputStream(to));
    }

    protected URL getUpdaterJarLocation() {
        return InternalJarExecuter.class.getResource("/updater/updater.jar");
    }
}
