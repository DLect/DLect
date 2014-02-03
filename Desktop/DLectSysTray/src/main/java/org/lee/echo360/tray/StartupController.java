/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray;

/**
 *
 * @author lee
 */
public class StartupController {
//    private static long openTime;
//
//    protected static MainFrame initMainFrame(final GUIController ctl) {
//        MainFrame mf = new MainFrame(ctl);
//        mf.setLoginLocked(true);
//        mf.setCoursesLocked(true);
//        mf.setLocationRelativeTo(null);
//        mf.setVisible(true);
//        openTime = System.currentTimeMillis();
//        return mf;
//    }
//
//    private static void initStdInputOutputRedirect() {
//        try {
//            FileOutputStream output = new FileOutputStream("output.txt");
//            System.setErr(new RedirectingPrintStream(output, System.err, "Err:"));
//            System.setOut(new RedirectingPrintStream(output, System.out, "Out:"));
//        } catch (FileNotFoundException ex) {
//            ExceptionReporter.reportException(ex);
//        } catch (IOException ex) {
//            ExceptionReporter.reportException(ex);
//        }
//
//    }
//    private final GUIController ctl;
//
//    public StartupController(GUIController ctl) {
//        this.ctl = ctl;
//    }
//
//    protected void startup(long t) {
//        ctl.start(ControllerAction.STARTUP);
//        ctl.getPropertiesController().loadProperties();
//        long t2 = openTime;
//        //initStdInputOutputRedirect();
//        PropertiesController propCtl = ctl.getPropertiesController();
//        Blackboard b = propCtl.getBlackboard();
//        System.out.println("Startup : " + b);
//        System.out.println("Class   : " + propCtl.getProviderClass());
//        try {
//            if (b == null) {
//                propCtl.initBlackboard();
//                b = propCtl.getBlackboard();
//            }
//            BlackboardProviders.getProviders();
//        } finally {
//            ctl.finished(ControllerAction.STARTUP, ActionResult.SUCCEDED);
//            System.out.println("Do Updates ect.");
//            URLUtil.goTo(VersionInformation.getRunRegistrationURL(ctl.getUUID(), b.getBlackboardID(), t2 - t));
//            UpdateController.doUpdates();
//        }
//    }
}
