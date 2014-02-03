/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.test.configure;

import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class Reporter {

    public static void setUp() {
        ExceptionReporter.setExecuteOnSeperateThread(false);
    }
    public static void tearDown() {
        ExceptionReporter.setExecuteOnSeperateThread(true);
    }
    
}
