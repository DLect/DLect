package org.lee.echo360;

import java.io.IOException;
import org.dlect.controller.GUIController;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lee
 */
public class StandaloneLauncher implements Launcher {

    public StandaloneLauncher() {
    }

    @Override
    public void launch(long startTime, String... args) {
        try {
            GUIController.doStartup(startTime, args);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
