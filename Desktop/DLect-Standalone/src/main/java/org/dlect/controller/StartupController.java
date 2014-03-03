/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import org.dlect.ui.MainFrame;

/**
 *
 * @author Lee Symes
 */
public class StartupController {

    private static long openTime;

    protected static MainFrame initMainFrame(final GUIController ctl) {
        MainFrame mf = new MainFrame(ctl);
        mf.setLoginLocked(true);
        mf.setCoursesLocked(true);
        mf.setLocationRelativeTo(null);
        mf.setVisible(true);
        openTime = System.currentTimeMillis();
        return mf;
    }

    private void initStdInputOutputRedirect() {
//        try {
//            FileOutputStream output = new FileOutputStream(new File(ctl.getPropertiesController().getParentFolder(), "output.txt"));
//            System.setErr(new RedirectingPrintStream(output, System.err, "Err:"));
//            System.setOut(new RedirectingPrintStream(output, System.out, "Out:"));
//        } catch (FileNotFoundException ex) {
//            ExceptionReporter.reportException(ex);
//        } catch (IOException ex) {
//            ExceptionReporter.reportException(ex);
//        }

    }
    private final GUIController ctl;

    public StartupController(GUIController ctl) {
        this.ctl = ctl;
    }

    protected void startup(long t) {
//        ctl.start(ControllerAction.STARTUP);
//        PropertiesSavingController.loadProperties(ctl);
//        long t2 = openTime;
//        initStdInputOutputRedirect();
//        PropertiesController propCtl = ctl.getPropertiesController();
//        Blackboard b = propCtl.getBlackboard();
//        try {
//            if (b == null) {
//                propCtl.initBlackboard();
//                b = propCtl.getBlackboard();
//            }
//            BlackboardProviders.getProviders();
//        } finally {
//            ctl.finished(ControllerAction.STARTUP, ActionResult.SUCCEDED);
//            URLUtil.goTo(VersionInformation.getRunRegistrationURL(ctl.getUUID(), b.getBlackboardID(), t2 - t));
//            UpdateController.doUpdates(ctl.getApplicationPropertiesController().getUpdateStyle());
//        }
    }

    private static class RedirectingPrintStream extends PrintStream {

        protected RedirectingPrintStream(String s, PrintStream ps, String prefix) throws FileNotFoundException {
            this(new File(s), ps, prefix);
        }

        protected RedirectingPrintStream(File logFile, PrintStream ps, String prefix) throws FileNotFoundException {
            this(new FileOutputStream(logFile), ps, prefix);
            this.println("\n\n\n--------------------------------------------"
                    + "--------\n" + new Date() + "\n---------------------------"
                    + "-------------------------");
        }

        protected RedirectingPrintStream(OutputStream os, PrintStream ps, String prefix) {
            this(new DualOutputStream(os, ps, prefix));
        }

        protected RedirectingPrintStream(OutputStream os) {
            super(os);
        }
    }

    private static class DualOutputStream extends OutputStream {

        private static final Object lock = new Object();
        private final OutputStream os1, os2;
        private final byte[] prefix;

        protected DualOutputStream(OutputStream os1, OutputStream os2, String prefix) {
            synchronized (lock) {
                assert os1 != null;
                assert os2 != null;
                assert os1 == os2;
                this.os1 = os1;
                this.os2 = os2;
                this.prefix = (prefix + " ").getBytes();
            }
        }

        @Override
        public void write(int b) throws IOException {
            synchronized (lock) {
                this.os1.write(b);
                this.os2.write(b);
            }
            if (b == '\n') {
                //write(prefix);
                //write(Long.toString(System.nanoTime()).getBytes());
                //write('|');
            }
        }
    }
}
