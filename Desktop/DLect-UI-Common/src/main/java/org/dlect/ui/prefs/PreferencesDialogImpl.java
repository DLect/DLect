/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs;

import java.awt.Window;
import org.dlect.controller.MainController;

public class PreferencesDialogImpl extends PreferencesDialog {

    private final PreferencePanel[] preferencePanels;

    public PreferencesDialogImpl(Window parent, MainController controller) {
        super(parent, controller);
        preferencePanels = new PreferencePanel[]{
            new CoursePreferencePanel(controller), new ApplicationPreferencePanel(controller)
        };
    }

    @Override
    public PreferencePanel[] getPreferencePanels() {
        return preferencePanels;
    }
}
