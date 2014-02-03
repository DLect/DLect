/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.util;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author lee
 */
public final class ExceptionReporter {

    private static final ExecutorService exceptionPoster = Executors.newFixedThreadPool(2);
    private static final String EXCEPTION_REPORTER_CLASS_NAME = ExceptionReporter.class.getName();
    private static final Set<ExceptionListener> listeners = new HashSet<ExceptionListener>();
    private static boolean executeOnSeperateThread = false;

    static {
        listeners.add(new ExceptionListener() {
            @Override
            public void exceptionThrown(ExceptionReporter.ReportedException e) {
                System.out.println("Reporting the exception: "
                        + e.getExplaination() + " that occured at "
                        + e.getTime() + ". If it had been implemented");
                System.out.println("Caught: " + e.getCaughtAt());
                e.printStackTrace(System.out);
            }
        });
    }

    public static void reportException(String name, StackTraceElement[] elms) {
        postException(new ReportedNonException(elms, name, System.currentTimeMillis()));
    }

    public static <T extends Throwable> T reportException(T ex) {
        postException(new ReportedException(ex, System.currentTimeMillis()));
        return ex;
    }

    public static void reportException(String t) {
        reportException(new Exception(t));
    }

    private static void postException(ReportedException r) {
        ExceptionReporterTask task = new ExceptionReporterTask(r);
        synchronized (exceptionPoster) {
            if (executeOnSeperateThread) {
                exceptionPoster.execute(task);
            } else {
                task.run();
            }
        }
    }

    public static <T extends Throwable> T reportException(T ex, String string) {
        reportException(new Exception(string, ex));
        return ex;
    }

    @Deprecated
    private ExceptionReporter() {

    }

    public static void setExecuteOnSeperateThread(boolean onSeperateThread) {
        synchronized (exceptionPoster) {
            ExceptionReporter.executeOnSeperateThread = onSeperateThread;
        }
    }

    /**
     * Adds the argument to a list of listeners that will be fired when an
     * exception is reported. This method will not re-add listeners that are
     * already in the list.
     *
     * When this method returns; The listener provided is guaranteed to be fired
     * until
     *
     * @param e The listener to add
     */
    public static void addExceptionListener(ExceptionListener e) {
        synchronized (listeners) {
            listeners.add(e);
        }
    }

    /**
     * Removes the argument to a list of listeners that will be fired when an
     * exception is reported.
     *
     * When this method returns; The listener provided is guaranteed not to be
     * fired until.
     *
     * @param e The listener to remove.
     */
    public static void removeExceptionListener(ExceptionListener e) {
        synchronized (listeners) {
            listeners.remove(e);
        }
    }

    private static class ExceptionReporterTask implements Runnable {

        private final ReportedException r;

        private ExceptionReporterTask(ReportedException r) {
            this.r = r;
        }

        @Override
        public void run() {
            for (ExceptionListener exceptionListener : listeners) {
                exceptionListener.exceptionThrown(r);
            }
        }
    }

    public static class ReportedException {

        private final Throwable exception;
        private final long time;
        private final StackTraceElement elm;

        public ReportedException(Throwable exception, long time) {
            this.exception = exception;
            this.time = time;
            StackTraceElement[] arr = Thread.currentThread().getStackTrace();
            for (int i = 1; i < arr.length; i++) {
                StackTraceElement object = arr[i];
                if (!object.getClassName().equals(EXCEPTION_REPORTER_CLASS_NAME)) {
                    elm = object;
                    return;
                }
            }
            elm = null;
        }

        public ReportedException(Throwable exception, long time, StackTraceElement elm) {
            this.exception = exception;
            this.time = time;
            this.elm = elm;
        }

        public Throwable getException() {
            return exception;
        }

        public long getTime() {
            return time;
        }

        public StackTraceElement getCaughtAt() {
            return elm;
        }

        public StackTraceElement[] getStackTrace() {
            return exception.getStackTrace();
        }

        public String getExplaination() {
            return exception.getClass().getName() + ": " + exception.getMessage();
        }

        public void printStackTrace(PrintStream s) {
            System.out.println(getExplaination());
            for (StackTraceElement traceElement : getStackTrace()) {
                s.println("\tat " + traceElement);
            }
        }

    }

    public static class ReportedNonException extends ReportedException {

        private final StackTraceElement[] trace;
        private final String explain;

        public ReportedNonException(StackTraceElement[] trace, String explain, long time, StackTraceElement elm) {
            super(null, time, elm);
            this.trace = trace;
            this.explain = explain;
        }

        public ReportedNonException(StackTraceElement[] trace, String explain, long time) {
            super(null, time);
            this.trace = trace;
            this.explain = explain;
        }

        @Override
        public String getExplaination() {
            return explain;
        }

        @Override
        public StackTraceElement[] getStackTrace() {
            return trace;
        }

    }
}
