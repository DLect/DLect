/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.utils;

import java.util.Timer;

/**
 *
 * @author lee
 */
public class AdvancedTimer extends Timer {

    private boolean canceled = false;

    public AdvancedTimer(boolean isDaemon) {
        super(isDaemon);
    }

    public AdvancedTimer() {
    }

    public AdvancedTimer(String name) {
        super(name);
    }

    public AdvancedTimer(String name, boolean isDaemon) {
        super(name, isDaemon);
    }

    @Override
    public void cancel() {
        super.cancel(); //To change body of generated methods, choose Tools | Templates.
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
