/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.dlect.logging.ControllerLogger;

/**
 *
 * @author lee
 */
public class ApplicationInformation {

    public static final String OS_TYPE;
    public static final String OS_VERSON;
    public static final String OS_ARCHITECTURE;
    public static final String JAVA_VERSION;
    public static final String APPLICATION_VERSION = getApplicationVersion();

    static {
        // OS INFORMATION
        OS_TYPE = System.getProperty("os.name", "?");
        OS_VERSON = System.getProperty("os.version", "?");
        OS_ARCHITECTURE = System.getProperty("os.arch", "?");

        JAVA_VERSION = System.getProperty("java.version", "?");
    }

    public static String getApplicationVersion() {
        try {
            Enumeration<URL> r = ClassLoader.getSystemClassLoader().getResources("META-INF/MANIFEST.MF");

            while (r.hasMoreElements()) {
                URL url = r.nextElement();

                Manifest mf = new Manifest(url.openStream());
                Attributes attr = mf.getMainAttributes();
                String version = attr.getValue("DLect-Build-Version");
                if (version != null) {
                    return version;
                }
            }
            return "3.0.?(V)";
        } catch (IOException ex) {
            ControllerLogger.LOGGER.error("Failed to load manifest", ex);
            return "3.0.?(E)";
        }
    }

}
