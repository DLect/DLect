/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller;

import org.dlect.controller.data.DatabaseHandler;
import org.dlect.controller.data.DatabaseSavingHandler;
import org.dlect.controller.download.DownloadController;
import org.dlect.controller.helper.ControllerStateHelper;
import org.dlect.controller.helper.Initilisable;
import org.dlect.controller.helper.Initilisables;
import org.dlect.controller.helper.lecture.LectureDownloadStateUpdater;
import org.dlect.controller.helper.lecture.LectureStateUpdater;
import org.dlect.controller.helper.subject.SubjectDataHelper;
import org.dlect.controller.helper.subject.SubjectDisplaySettingHandler;
import org.dlect.controller.helper.subject.SubjectDisplayUpdater;
import org.dlect.controller.provider.ProviderHelper;
import org.dlect.events.listenable.Listenable;
import org.dlect.file.FileController;
import org.dlect.model.helper.CommonSettingNames;

import static java.util.UUID.randomUUID;

/**
 *
 * @author lee
 */
public abstract class MainController extends Listenable<MainController> implements Initilisable {

    private LoginController loginController;
    private SubjectController subjectController;
    private LectureController lectureController;
    private DownloadController downloadController;

    private DatabaseHandler databaseHandler;
    private ProviderHelper providerHelper;
    private ControllerStateHelper controllerStateHelper;
    private SubjectDataHelper subjectDataHelper;
    private SubjectDisplaySettingHandler subjectDisplayHelper;

    public MainController() {
    }

    public abstract FileController getFileController();

    @Override
    public void init() {
        this.databaseHandler = new DatabaseHandler();
        this.providerHelper = new ProviderHelper(this);

        this.loginController = new LoginController(this);
        this.subjectController = new SubjectController(this);
        this.lectureController = new LectureController(this);
        this.downloadController = new DownloadController(this);

        this.controllerStateHelper = new ControllerStateHelper(this);
        this.subjectDisplayHelper = new SubjectDisplaySettingHandler(this);
        this.subjectDataHelper = new SubjectDataHelper(this);

        databaseHandler.init();
        databaseHandler.addTemporarySetting(CommonSettingNames.UUID, randomUUID().toString());

        if (databaseHandler.getSetting(CommonSettingNames.BBID) == null) {
            databaseHandler.addSetting(CommonSettingNames.BBID, randomUUID().toString());
        }

        this.addChild(databaseHandler, loginController, subjectController,
                      lectureController, downloadController, controllerStateHelper,
                      subjectDataHelper);
        Initilisables.doInit(databaseHandler, providerHelper, loginController,
                             subjectController, lectureController, downloadController,
                             controllerStateHelper, subjectDisplayHelper, subjectDataHelper);

        DatabaseSavingHandler.registerOn(this);
        LectureStateUpdater.registerOn(this);
        LectureDownloadStateUpdater.registerOn(this);
        SubjectDisplayUpdater.registerOn(this);
    }

    public DownloadController getDownloadController() {
        return downloadController;
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

}
