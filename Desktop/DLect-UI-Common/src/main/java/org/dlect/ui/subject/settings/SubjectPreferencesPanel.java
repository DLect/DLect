/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs.subject;

import org.dlect.controller.MainController;
import org.dlect.model.Subject;
import org.dlect.ui.prefs.PreferencePanel;

/**
 *
 * @author lee
 */
public abstract class SubjectPreferencesPanel extends PreferencePanel {

    private final Subject s;

    public SubjectPreferencesPanel(Subject s, MainController ctl) {
        super(ctl);
        this.s = s;
    }

    public Subject getSubject() {
        return s;
    }

    @Override
    public final void doSave() {
    }

    @Override
    public final boolean isModified() {
        return false;
    }
}
