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
package org.dlect.controller;

import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.ui.MainFrame;

/**
 *
 * @author lee
 */
public class GUIController extends MainController {

    public static void main(String[] args) throws IOException {
        long t = System.currentTimeMillis();
        doStartup(t, args);
    }

    public static void doStartup(long t, String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Never happens.
        }
        System.out.println(t);
        GUIController mc = new GUIController();
        mc.addListener(new DebuggingEventListener());
        mc.init();

        System.out.println(mc.getDatabaseHandler().getDatabase());

        StartupController c = mc.getStartupController();
        c.startup(t);
    }
    private StartupController startupController;
    private MainFrame mainFrame;

    public GUIController() {
        super();
    }

    @Override
    public void init() {
        super.init();
        this.mainFrame = StartupController.initMainFrame(this);
        this.startupController = new StartupController(this);
        this.mainFrame.setLoginLocked(false);
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

    private static final class DebuggingEventListener implements EventListener {

        @Override
        public void processEvent(Event e) {
            System.out.println(e);
        }

    }
}
