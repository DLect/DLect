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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.dlect.update.UpdateLogger;

/**
 *
 * @author lee
 */
public class InternalJarExecuter {

    /**
     * The command line call for the java executable. Usually just `java`
     * however if that is not supported will search in the `java.home` system
     * property.
     *
     * Probably should implement a full system search.
     */
    private String javaExec = null;
    private static InternalJarExecuter instance = new InternalJarExecuter();
    private static InternalJarHelper helper = new InternalJarHelper();

    /**
     * Extracts and executes the updater found in the distributed jar file.
     *
     * @return `true` if the updater was started successfully, `false`
     *         otherwise.
     */
    public static boolean spawnUpdaterProcess(String opt) {
        return instance.spawnUpdater(opt);
    }

    /**
     * Extracts and executes the updater found in the distributed jar file.
     *
     * @return `true` if the updater was started successfully, `false`
     *         otherwise.
     */
    public static boolean spawnUpdaterProcess() {
        return instance.spawnUpdater("");
    }

    protected boolean spawnUpdater(String styleOpt) {
        boolean success = false;
        String currentLocation = helper.getCurrentJarLocation();
        File tempJarFileForExec;
        try {
            URL updaterJarFile = helper.getUpdaterJarLocation();
            tempJarFileForExec = File.createTempFile("DLect-Updater", ".jar");
            System.out.println("Temp Location: " + tempJarFileForExec);
            System.out.println("Java Exec : " + javaExec);
            helper.copy(updaterJarFile, tempJarFileForExec);
        } catch (IOException ex) {
            UpdateLogger.LOGGER.error("Failed to create temp file or copy into the temp file.", ex);
            return false;
        }
        try {
            if ((checkJava() || searchForJava()) && javaExec != null) {
                System.out.println("Java Exec-: " + javaExec);
                helper.exec(javaExec, "-jar", tempJarFileForExec.getPath(), currentLocation, styleOpt);
                success = true;
            }
            return success;
        } catch (IOException ex) {
            UpdateLogger.LOGGER.error("Failed to create process from `java.home` java instance!", ex);
            return false;
        }
    }

    protected boolean checkJava() {
        boolean found = false;
        try {
            Process exec = helper.exec("java", "-version");
            // This will throw an IO Exception if it can't find `java`.
            exec.destroy();
            // So we can just destroy it, We don't care about the output or
            //  the exit code, and during testing it took some time to exit.
            // So just destroying it will save time and memory.
            javaExec = "java";
            found = true;
        } catch (IOException ex) {
            UpdateLogger.LOGGER.error("Failed to create process from plain java instance!", ex);
        }
        return found;
    }

    protected boolean searchForJava() {
        boolean found = false;
        String p = System.getProperty("java.home", null);
        // Use the `java.home` property. It points to the folder that the `bin`
        //  directory resides.
        // However it could be null, so check that.
        if (p != null) {
            p = FileUtils.getFile(p, "bin", "java").getPath();
            // Move into the bin directory and call java.
            // Could call javaw if on windows but it would make no difference,
            //  as this application controls the terminal/command line.
            if (System.getProperty("os.name", "Unknown").contains("Windows")) {
                // Add the `exe` extension on windows.
                p += ".exe";
            }
            try {
                // Don't need the quotes around the command, as the exec
                //  command calls it directly instead of through the terminal.
                Process exec = helper.exec(p, "-version");
                // If we get here, then it must have succedded, so destroy the
                //  process and exit.
                exec.destroy();
                javaExec = p;
                found = true;
            } catch (IOException ex) {
                UpdateLogger.LOGGER.error("Failed to create process from `java.home` java instance!", ex);
            }
        }
        return found;
    }

    private InternalJarExecuter() {
    }
}
