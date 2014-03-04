/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller;

import org.dlect.controller.helper.SubjectDataHelper;
import org.dlect.controller.data.DatabaseHandler;
import org.dlect.controller.helper.ControllerStateHelper;
import org.dlect.controller.helper.Initilisable;
import org.dlect.controller.helper.Initilisables;
import org.dlect.controller.helper.subject.SubjectDisplaySettingHandler;
import org.dlect.controller.provider.ProviderHelper;
import org.dlect.events.listenable.Listenable;

/**
 *
 * @author lee
 */
public class MainController extends Listenable<MainController> implements Initilisable {

    private final long uuid;
    private DatabaseHandler databaseHandler;
    private ProviderHelper providerHelper;
    private LoginController loginController;
    private SubjectController subjectController;
    private LectureController lectureController;
    private ControllerStateHelper controllerStateHelper;
    private SubjectDataHelper subjectDataHelper;
    private SubjectDisplaySettingHandler subjectDisplayHelper;

    public MainController() {
        this.uuid = Double.doubleToRawLongBits(System.currentTimeMillis() * Math.random());
    }

    @Override
    public void init() {
        this.databaseHandler = new DatabaseHandler();
        this.providerHelper = new ProviderHelper(this);
        this.loginController = new LoginController(this);
        this.subjectController = new SubjectController(this);
        this.lectureController = new LectureController(this);
        this.controllerStateHelper = new ControllerStateHelper(this);

        this.addChild(databaseHandler, loginController, subjectController, lectureController, controllerStateHelper);
        Initilisables.doInit(databaseHandler, providerHelper, loginController, subjectController, lectureController, controllerStateHelper);
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public ProviderHelper getProviderHelper() {
        return providerHelper;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public SubjectController getSubjectController() {
        return subjectController;
    }

    public LectureController getLectureController() {
        return lectureController;
    }

    public ControllerStateHelper getControllerStateHelper() {
        return controllerStateHelper;
    }

    public SubjectDataHelper getSubjectDataHelper() {
        return subjectDataHelper;
    }

    public SubjectDisplaySettingHandler getSubjectDisplayHelper() {
        return subjectDisplayHelper;
    }

    public long getUuid() {
        return uuid;
    }

}
