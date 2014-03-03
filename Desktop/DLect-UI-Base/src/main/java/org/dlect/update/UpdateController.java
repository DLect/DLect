/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update;

import org.dlect.controller.helper.Initilisable;
import org.dlect.controller.MainController;
import org.dlect.controller.data.DatabaseHandler;
import org.dlect.events.EventID;
import org.dlect.events.listenable.Listenable;

/**
 *
 * @author lee
 */
public class UpdateController extends Listenable<UpdateController> implements Initilisable {

    public static final String NO_UPDATE_CHECK_SETTING = "NoUpdateCheck?";
    private final MainController ctl;

    private UpdateCheckingHelper helper;

    public UpdateController(MainController ctl) {
        this.ctl = ctl;
    }

    @Override
    public void init() {
        helper = new UpdateCheckingHelper();
    }

    /**
     * This method checks with the server for updates and if any are avaliable then commences the download in a separate
     * thread.
     *
     *
     * @return {@code true} if the update was successully checked. NOT an update is avaliable. This method will return
     *         false if a program state did not allow for the update to be checked. This method will return true if
     *         "Don't Check for Updates" is checked.
     */
    public boolean checkForUpdates() {
        if (ctl.getDatabaseHandler().getDatabase() == null) {
            return false;
        }
        DatabaseHandler d = ctl.getDatabaseHandler();

        String dontCheck = d.getSetting(NO_UPDATE_CHECK_SETTING);
        if (dontCheck == null) {
            // Defaults to false - I.E. check unless otherwise instructed.
            dontCheck = "false";
        }
        // Invalid string will return false; causing a check.
        if (Boolean.parseBoolean(dontCheck)) {
            // User requested that the updates not be checked. 
            return true;
        } else {

            // TODO get UpdateStyle from DB.
            UpdateStyle us = null;

            try {
                helper.doUpdate(us);
                fireEvent(UpdateControllerEventID.UPDATE_CHECK_COMPLETED);
            } catch (UpdateException ex) {
                // TODO debug
            }
            return true;
        }
    }

    public static enum UpdateControllerEventID implements EventID {

        UPDATE_CHECK_COMPLETED;

        @Override
        public Class<?> getAppliedClass() {
            return UpdateController.class;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public boolean isUniqueId() {
            return false;
        }

    }

}
