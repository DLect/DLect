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
package org.lee.echo360.control.controllers;

import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.lee.echo360.ui.MainFrame;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class GUIController extends MainController {

    private static DebuggingTimingListener debuggingTimingListener;

    public static void main(String[] args) throws IOException {
        long t = System.currentTimeMillis();
        doStartup(t, args);
    }

    public static void doStartup(long t, String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ExceptionReporter.reportException(ex);
        } catch (InstantiationException ex) {
            ExceptionReporter.reportException(ex);
        } catch (IllegalAccessException ex) {
            ExceptionReporter.reportException(ex);
        } catch (UnsupportedLookAndFeelException ex) {
            ExceptionReporter.reportException(ex);
        }
        System.out.println(t);
        GUIController mc = new GUIController();
        debuggingTimingListener = new DebuggingTimingListener(t);
        mc.addControllerListener(debuggingTimingListener);
        StartupController c = mc.getStartupController();
        c.startup(t);
    }
    private final StartupController startupController;
    private final MainFrame mainFrame;

    public GUIController() {
        this.mainFrame = StartupController.initMainFrame(this);
        this.startupController = new StartupController(this);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public StartupController getStartupController() {
        return startupController;
    }

    public void closeApplication() {
        System.exit(0);
    }
}
