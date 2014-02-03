/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui.advanced;

import java.awt.Window;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.model.Subject;
import org.lee.echo360.ui.prefs.PreferencePanel;
import org.lee.echo360.ui.prefs.PreferencesDialog;

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
