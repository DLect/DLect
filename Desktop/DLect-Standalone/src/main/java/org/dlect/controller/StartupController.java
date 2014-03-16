/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.dlect.logging.ControllerLogger;
import org.dlect.ui.MainFrame;

/**
 *
 * @author Lee Symes
 */
public class StartupController {

    private static long openTime;

    protected static void initLogging() {
        try {
            LogManager.getLogManager().reset();

            DLectLoggingHandler handler = new DLectLoggingHandler();
            Logger root = Logger.getLogger("");

            for (Handler h : root.getHandlers()) {
                root.removeHandler(h);
            }

            root.addHandler(handler);
        } catch (SecurityException ex) {
            Logger.getLogger(StartupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static MainFrame initMainFrame(final GUIController ctl) {
        MainFrame mf = new MainFrame(ctl);
        mf.setLoginLocked(true);
        mf.setCoursesLocked(true);
        mf.setLocationRelativeTo(null);
        mf.setVisible(true);
        openTime = System.currentTimeMillis();
        ControllerLogger.LOGGER.info("Loaded main frame at: " + openTime);
        return mf;
    }

}
