/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class DebuggingTimingListener implements ControllerListener {

    private BufferedWriter w;
    private Map<ControllerAction, Long> controllerStartTime = new HashMap<ControllerAction, Long>();
    private long loginStartTime = -1;
    private final long startupTime;

    public DebuggingTimingListener(long t) throws IOException {
        w = new BufferedWriter(new FileWriter("timings.txt"));
        startupTime = t;
    }

    @Override
    public void start(ControllerAction action) {
        ExceptionReporter.reportException("Started: " + action);
        controllerStartTime.put(action, System.currentTimeMillis());
        if (action == ControllerAction.LOGIN) {
            loginStartTime = System.currentTimeMillis();
        }
    }

    @Override
    public void finished(ControllerAction action, ActionResult r) {
        ExceptionReporter.reportException("Finished: " + action + "; Result: " + r);
        try {
            long end = System.currentTimeMillis();
            Long start = controllerStartTime.get(action);
            String time = (start == null) ? "No Start Time(Finished at " + end + ")" : "Total Time: " + (end - start);
            w.append(action + " Completed:\n");
            w.append("\tResult: " + r + "\n");
            w.append("\t" + time + "ms\n");
            w.append("\tSince Start: " + (end - startupTime) + "ms\n");
            if (loginStartTime > 0) {
                w.append("\tSince Login: " + (end - loginStartTime) + "ms\n");
            }
            w.append("\n");
            w.flush();
        } catch (IOException ex) {
            Logger.getLogger(DebuggingTimingListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void error(Throwable e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
