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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author lee
 */
public class ConnectionBuilder {

    public HttpURLConnection connect(URL u) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setUseCaches(false);
        conn.connect();
        return conn;
    }
}
