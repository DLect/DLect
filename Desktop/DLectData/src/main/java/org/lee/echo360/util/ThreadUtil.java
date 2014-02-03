/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Lee Symes
 */
public class ThreadUtil {

    private static final ThreadPoolExecutor CONTROLLER_EXECUTOR_SERVICE = new ThreadPoolExecutor(1, 100, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    public static Future runExecution(Runnable runnable) {
        synchronized (CONTROLLER_EXECUTOR_SERVICE) {
            return CONTROLLER_EXECUTOR_SERVICE.submit(runnable);
        }
    }

    public static <T> Future<T> runExecution(Callable<T> runnable) {
        synchronized (CONTROLLER_EXECUTOR_SERVICE) {
            return CONTROLLER_EXECUTOR_SERVICE.submit(runnable);
        }
    }

    public static void waitForCompletion(ExecutorService service) {
        service.shutdown();
        while (!service.isTerminated()) {
            try {
                service.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                //No Op
            }
        }
    }

    public static void waitForExecutionCompletion() {
        while (CONTROLLER_EXECUTOR_SERVICE.getQueue().peek() == null) {
            try {
                synchronized (CONTROLLER_EXECUTOR_SERVICE) {
                    CONTROLLER_EXECUTOR_SERVICE.wait(1000);
                }
            } catch (InterruptedException ex) {
                ExceptionReporter.reportException(ex);
            }
        }
    }

    /**
     * Creates an executor service with the specified minimum and maximum thread
     * counts. It is recommended that this be shutdown after use by calling
     * {@linkplain ExecutorService#shutdown()}.
     *
     * @param minThreads the number of threads to keep in the pool, even if they
     * are idle.
     * @param maxThreads the maximum number of threads to allow in the pool.
     * @return A new executor service.
     * @see ThreadPoolExecutor#ThreadPoolExecutor(int, int, long,
     * java.util.concurrent.TimeUnit, java.util.concurrent.BlockingQueue)
     */
    public static ExecutorService createPrivateExecutorService(int minThreads, int maxThreads) {
        return createPrivateExecutorService(minThreads, maxThreads, 10, TimeUnit.SECONDS);
    }

    /**
     * Creates an executor service with the specified minimum and maximum thread
     * counts, and the time to live for the threads. It is recommended that this
     * be shutdown after use by calling {@linkplain ExecutorService#shutdown()}.
     *
     * @param minThreads the number of threads to keep in the pool, even if they
     * are idle.
     * @param maxThreads the maximum number of threads to allow in the pool.
     * @return A new executor service.
     * @see ThreadPoolExecutor#ThreadPoolExecutor(int, int, long,
     * java.util.concurrent.TimeUnit, java.util.concurrent.BlockingQueue)
     */
    public static ExecutorService createPrivateExecutorService(int minThreads, int maxThreads, long time, TimeUnit unit) {
        return new ThreadPoolExecutor(minThreads, maxThreads, time, unit, new LinkedBlockingDeque<Runnable>());
    }

    private ThreadUtil() {
    }
}
