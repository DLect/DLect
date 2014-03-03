/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs.subject;

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

    private final Subject s;
    private final MainController mc;
    private final PreferencePanel[] preferencesPanels;

    /**
     * Creates new form AdvancedSelectionDialog1
     */
    public AdvancedSubjectPreferencesDialog(Window parent, Subject s, MainController mc) {
        super(parent, mc);
        this.setTitle("Advanced settings for " + s.getName());
        this.s = s;
        this.mc = mc;
        preferencesPanels = new PreferencePanel[]{new AdvancedLecturePanel(s, mc), new AdvancedPlaylistPreferencesPanel(s, mc)};
        this.setLocationRelativeTo(parent);
        removeCancelButton();
    }

    @Override
    public PreferencePanel[] getPreferencePanels() {
        return preferencesPanels;
    }
}
