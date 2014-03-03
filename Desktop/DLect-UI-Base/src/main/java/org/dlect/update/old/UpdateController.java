/*
 *  Copyright (C) 2012 lee
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
import org.dlect.update.UpdateLogger;
import org.dlect.update.UpdateStyle;

/**
 *
 * @author lee
 */
public class UpdateController {

    public static final int NO_UPDATES_RESPONCE_CODE = 304;
    public static final int UPDATES_AVALIABLE_RESPONCE_CODE = 200;
    private static ConnectionBuilder builder = new ConnectionBuilder();

    public static void doUpdates(UpdateStyle us) {
        System.out.println("Do Updates Called");
        try {
            final int updateCode = checkForUpdates();
            if (updateCode == UPDATES_AVALIABLE_RESPONCE_CODE) {
                InternalJarExecuter.spawnUpdaterProcess("--"); // TODO
            } else if (updateCode == NO_UPDATES_RESPONCE_CODE) {
                System.out.println("No Updates Avaliable");
            } else {
                UpdateLogger.LOGGER.error("Server gave an invalid responce code: " + updateCode);
            }
        } catch (IOException ex) {
            UpdateLogger.LOGGER.error("", ex);
        }
    }

    /**
     *
     * @return true if updates are available.
     */
    public static int checkForUpdates() throws IOException {
        URL u = new URL(VersionInformation.UPDATE_CHECKING_URL);
        System.out.println("Update: " + u);
        HttpURLConnection conn = null;
        try {
            conn = builder.connect(u);
            System.out.println("Repsonce Code:  " + conn.getResponseCode());
            System.out.println("Latest Version: " + conn.getHeaderField("X-Current-Version"));
            return conn.getResponseCode();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private UpdateController() {
    }
}
