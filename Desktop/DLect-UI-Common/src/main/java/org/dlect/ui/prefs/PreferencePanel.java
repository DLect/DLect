/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs;

import javax.swing.JPanel;
import org.dlect.controller.MainController;

/**
 *
 * @author lee
 */
public abstract class PreferencePanel extends JPanel {

    private final MainController ctl;

    /**
     *
     * @param ctl
     */
    public PreferencePanel(MainController ctl) {
        this.ctl = ctl;
    }

    public abstract void doSave();

    public void doCancel() {
        // No Op, but overridable if needed.
    }

    public abstract void doPreShow();

    public abstract String getTabName();

    public abstract String getTabTooltip();

    public abstract boolean isModified();

    public final MainController getController() {
        return ctl;
    }
}
