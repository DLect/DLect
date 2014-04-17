/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller;

import javax.annotation.Nullable;
import org.dlect.controller.data.DatabaseHandler;
import org.dlect.controller.event.ControllerListenable;
import org.dlect.controller.event.ControllerState;
import org.dlect.controller.event.ControllerType;
import org.dlect.events.EventID;
import org.dlect.exception.DLectException;
import org.dlect.provider.WrappedProvider;
import org.dlect.provider.loader.ProviderDetail;

import static org.dlect.helper.Conditions.checkNonNull;
import static org.dlect.model.helper.CommonSettingNames.*;

/**
 *
 * @author Lee Symes
 */
public class LoginController extends ControllerListenable<LoginController> {

    private final MainController ctl;

    public LoginController(MainController ctl) {
        this.ctl = ctl;
    }

    public void configureLoginCredentials(ProviderDetail provider, String username, String password) {
        checkNonNull(provider, "Provider");
        checkNonNull(username, "Username");
        checkNonNull(password, "Password");
        DatabaseHandler db = ctl.getDatabaseHandler();

        db.addEncryptedSetting(USERNAME, username);
        db.addEncryptedSetting(PASSWORD, password);
        db.addSetting(PROVIDER_CODE, provider.getCode());
    }

    public void doLogin() throws DLectException {
        event(ControllerType.LOGIN).state(ControllerState.STARTED).fire();
        boolean event = false;
        try {
            WrappedProvider provider = ctl.getProviderHelper().getProvider();
            provider.doLogin();
            event(ControllerType.LOGIN).state(ControllerState.COMPLETED).fire();
            event = true;
        } finally {
            // Always fire a fail event. Even if a non-DLectException was thrown.
            if (!event) {
                event(ControllerType.LOGIN).state(ControllerState.FAILED).fire();
            }
        }
    }

    @Nullable
    public ProviderDetail getSelectedProviderDetail() {
        return ctl.getProviderHelper().getProviderDetail();
    }

    @Nullable
    public String getUsername() {
        return ctl.getDatabaseHandler().getEncryptedSetting(USERNAME).orNull();
    }

    @Nullable
    public String getPassword() {
        return ctl.getDatabaseHandler().getEncryptedSetting(PASSWORD).orNull();
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

    }

}
