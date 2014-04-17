/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.subject.settings;

import java.awt.Window;
import org.dlect.controller.MainController;
import org.dlect.model.Subject;
import org.dlect.ui.prefs.PreferencePanel;
import org.dlect.ui.prefs.PreferencesDialog;

/**
 *
 * @author lee
 */
public class AdvancedSubjectPreferencesDialog extends PreferencesDialog {

    private final PreferencePanel[] preferencesPanels;

    /**
     * Creates new form AdvancedSelectionDialog1
     *
     * @param parent
     * @param s
     * @param mc
     */
    public AdvancedSubjectPreferencesDialog(Window parent, Subject s, MainController mc) {
        super(parent, mc);
        this.setTitle("Advanced settings for " + s.getName());
        preferencesPanels = new PreferencePanel[]{new AdvancedLecturePanel(s, mc)};
        this.setLocationRelativeTo(parent);
        removeCancelButton();
    }

    @Override
    public PreferencePanel[] getPreferencePanels() {
        return preferencesPanels;
    }
}
