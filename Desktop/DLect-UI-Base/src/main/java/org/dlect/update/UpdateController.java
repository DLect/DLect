/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update;

import org.dlect.controller.MainController;
import org.dlect.controller.data.DatabaseHandler;
import org.dlect.controller.event.ControllerState;
import org.dlect.controller.helper.Initilisable;
import org.dlect.events.EventID;
import org.dlect.events.listenable.Listenable;
import org.dlect.model.Database;

/**
 *
 * @author lee
 */
public class UpdateController extends Listenable<UpdateController> implements Initilisable {

    public static final String UPDATE_STYLE_SETTING = "UpdateStyle";
    private final MainController ctl;

    private UpdateCheckingHelper helper;

    public UpdateController(MainController ctl) {
        this.ctl = ctl;
    }

    @Override
    public void init() {
        helper = new UpdateCheckingHelper(ctl);
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

        UpdateStyle us = getUpdateSetting(d.getDatabase());

        if (us != UpdateStyle.NONE) {
            try {
                helper.doUpdate(us);
                event(UpdateControllerEventID.UPDATE_CHECK_COMPLETED).before(null).after(ControllerState.COMPLETED).fire();
            } catch (UpdateException ex) {
                event(UpdateControllerEventID.UPDATE_CHECK_COMPLETED).before(null).after(ControllerState.FAILED).fire();
                UpdateLogger.LOGGER.error("Failed to updated due to an exception.", ex);
            }
            return true;
        } else {
            return false;
        }
    }

    public static void addUpdateSetting(UpdateStyle us, Database d) {
        d.addSetting(UPDATE_STYLE_SETTING, us.name());
    }

    public static UpdateStyle getUpdateSetting(Database d) {
        UpdateStyle us;
        String updateStyle = d.getSetting(UPDATE_STYLE_SETTING);
        try {
            us = UpdateStyle.valueOf(updateStyle);
        } catch (IllegalArgumentException | NullPointerException e) {
            us = UpdateStyle.AUTOMATIC;
            addUpdateSetting(us, d);
        }
        return us;
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

    }

}
