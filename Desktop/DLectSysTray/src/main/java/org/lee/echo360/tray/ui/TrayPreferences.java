/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray.ui;

import java.awt.Window;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.ui.prefs.ApplicationPreferencePanel;
import org.lee.echo360.ui.prefs.CoursePreferencePanel;
import org.lee.echo360.ui.prefs.PreferencePanel;
import org.lee.echo360.ui.prefs.PreferencesDialog;

/**
 *
 * @author lee
 */
public class TrayPreferences extends PreferencesDialog  {

    private final MainController controller;

    public TrayPreferences(Window parent, MainController controller) {
        super(parent, controller);
        this.controller = controller;
    }

    @Override
    public PreferencePanel[] getPreferencePanels() {
        return new PreferencePanel[]{
            new CoursePreferencePanel(controller), new ApplicationPreferencePanel(controller)};
    }

}
