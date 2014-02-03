/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray;

import java.awt.AWTException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.control.controllers.PropertiesSavingController;

/**
 *
 * @author lee
 */
public class ApplicationController extends MainController {

    public static final TimeUnit SCHEDULE_TIME_UNIT = TimeUnit.HOURS;
    public static final long SCHEDULE_TIME_VALUE = 2; // Hours
    public static final long DELAY_MILLIS = SCHEDULE_TIME_UNIT.toMillis(SCHEDULE_TIME_VALUE);
    private static ApplicationController ctl;
    private static TrayManager man;
    public static final ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) throws AWTException {
        doStartup(System.currentTimeMillis(), args);
    }

    public static void doStartup(long startTime, String[] args) throws AWTException {
        ApplicationController c = new ApplicationController();
        PropertiesSavingController.loadProperties(c);
        man = new TrayManager(c);
        DownloadExecutor ex = new DownloadExecutor(c);
        s.scheduleAtFixedRate(ex, 0, SCHEDULE_TIME_VALUE, SCHEDULE_TIME_UNIT);
    }

    public ApplicationController() {
    }
}
