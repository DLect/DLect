/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class DeadlockReportingThread extends Thread implements ControllerListener {

    private static final AtomicInteger COUNT = new AtomicInteger();
    private final Object WAIT = new Object();
    private final MainController ctl;
    private final ControllerAction controllerAction;
    private final Thread reportingThread;
    private final int timeBeforeReporting;
    private final int timeBetweenReporting;
    private boolean reporting = true;

    public DeadlockReportingThread(MainController ctl, ControllerAction controllerAction, Thread currentThread, int timeBeforeReporting, int timeBetweenReporting) {
        super("Deadlock-Reporting-Thread" + COUNT.getAndIncrement());
        this.ctl = ctl;
        this.controllerAction = controllerAction;
        this.reportingThread = currentThread;
        this.timeBeforeReporting = timeBeforeReporting;
        this.timeBetweenReporting = timeBetweenReporting;
        this.setDaemon(true);
        ctl.addControllerListener(this);
    }

    public DeadlockReportingThread build(MainController ctl, ControllerAction controllerAction, int timeBeforeReporting) {
        return new DeadlockReportingThread(ctl, controllerAction, Thread.currentThread(), timeBeforeReporting, 1000);
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis() + timeBeforeReporting;
        while (time > System.currentTimeMillis() && reporting) {
            try {
                synchronized (WAIT) {
                    WAIT.wait(time - System.currentTimeMillis());
                }
            } catch (InterruptedException ex) {
            }
        }
        while (reporting) {
            reportStackTraces();
            time = System.currentTimeMillis() + timeBetweenReporting;
            while (time > System.currentTimeMillis() && reporting) {
                try {
                    synchronized (WAIT) {
                        WAIT.wait(time - System.currentTimeMillis());
                    }
                } catch (InterruptedException ex) {
                }
            }
        }
        ctl.removeControllerListener(this);
    }

    private void reportStackTraces() {
        ThreadGroup t = reportingThread.getThreadGroup();
        while (true) {
            ThreadGroup t1 = t.getParent();
            if (t1 == null) {
                break;
            } else {
                t = t1;
            }
        }
        Thread[] threads = new Thread[t.activeCount() + 2];
        int res;
        do {
            threads = new Thread[threads.length * 2];

            res = t.enumerate(threads);
        } while (res == threads.length);

        for (Thread thread : threads) {
            if(thread == null) break;
            ExceptionReporter.reportException("Deadlock Detected" + (thread == reportingThread ? " On Creation Thread" : "") + ". Stack Information for Thread: " + thread.getName(), thread.getStackTrace());
        }
    }

    @Override
    public void start(ControllerAction action) {
        // No Op
    }

    @Override
    public void finished(ControllerAction action, ActionResult r) {
        if (action == controllerAction) {
            reporting = false;
            ExceptionReporter.reportException(reportingThread.getName() + " Completed " + action + " - " + r);
            reportStackTraces();
            synchronized (WAIT) {
                WAIT.notify();
            }
        }

    }

    @Override
    public void error(Throwable e) {
    }

}
