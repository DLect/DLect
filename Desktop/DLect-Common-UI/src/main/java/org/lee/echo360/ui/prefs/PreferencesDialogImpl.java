/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui.prefs;

import java.awt.Window;
import org.lee.echo360.control.controllers.MainController;

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
