/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller;

import org.dlect.controller.data.DatabaseHandler;
import org.dlect.controller.event.ControllerListenable;
import org.dlect.controller.event.ControllerState;
import org.dlect.controller.event.ControllerType;
import org.dlect.controller.helper.Initilisable;
import org.dlect.events.EventID;
import org.dlect.exception.DLectException;
import org.dlect.model.helper.CommonSettingNames;
import org.dlect.provider.loader.ProviderDetail;

/**
 *
 * @author Lee Symes
 */
public class LoginController extends ControllerListenable<LoginController> implements Initilisable {

    private final MainController ctl;

    public LoginController(MainController ctl) {
        this.ctl = ctl;
    }

    public void configureLoginCredentials(ProviderDetail provider, String username, String password) {
        if (provider == null) {
            throw new IllegalArgumentException("Null provider.");
        }
        if (username == null) {
            throw new IllegalArgumentException("Null username.");
        }
        if (password == null) {
            throw new IllegalArgumentException("Null password.");
        }
        DatabaseHandler db = ctl.getDatabaseHandler();

        db.addEncryptedSetting(CommonSettingNames.USERNAME, username);
        db.addEncryptedSetting(CommonSettingNames.PASSWORD, password);
        db.addSetting(CommonSettingNames.PROVIDER_CODE, provider.getCode());
    }

    public void doLogin() throws DLectException {
        event(ControllerType.LOGIN).state(ControllerState.STARTED).fire();
        boolean event = false;
        try {
            ctl.getProviderHelper().getProvider().doLogin();
            event(ControllerType.LOGIN).state(ControllerState.COMPLETED).fire();
            event = true;
        } finally {
            // Always fire a fail event. Even if a non-DLectException was thrown.
            if (!event) {
                event(ControllerType.LOGIN).state(ControllerState.FAILED).fire();
            }
        }
    }

    @Override
    public void init() {
        // No Op.          
    }

    public static enum LoginControllerEventID implements EventID {

        LOGIN_SUCCEDED;

        @Override
        public Class<?> getAppliedClass() {
            return LoginController.class;
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
