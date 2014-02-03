/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.test.builders;

import org.lee.echo360.control.controllers.ApplicationPropertiesController;
import org.lee.echo360.control.controllers.DownloadController;
import org.lee.echo360.control.controllers.LectureController;
import org.lee.echo360.control.controllers.LoginController;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.control.controllers.PropertiesController;
import org.lee.echo360.control.controllers.SubjectController;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
public class ControllerHelper {

    public static ControllerHelper build() {
        return new ControllerHelper();
    }

    private ControllerHelper() {
        mainController = mock(MainController.class);
        propertiesController = mock(PropertiesController.class);
        applicationPropertiesController = mock(ApplicationPropertiesController.class);
        loginController = mock(LoginController.class);
        subjectController = mock(SubjectController.class);
        lectureController = mock(LectureController.class);
        downloadController = mock(DownloadController.class);
        initMock();
    }
    private final MainController mainController;
    private final PropertiesController propertiesController;
    private final ApplicationPropertiesController applicationPropertiesController;
    private final LoginController loginController;
    private final SubjectController subjectController;
    private final LectureController lectureController;
    private final DownloadController downloadController;

    private void initMock() {
        when(mainController.getPropertiesController()).thenReturn(propertiesController);
        when(mainController.getApplicationPropertiesController()).thenReturn(applicationPropertiesController);
        when(mainController.getLoginController()).thenReturn(loginController);
        when(mainController.getSubjectController()).thenReturn(subjectController);
        when(mainController.getLectureController()).thenReturn(lectureController);
        when(mainController.getDownloadController()).thenReturn(downloadController);
    }

    public ApplicationPropertiesController getApplicationPropertiesController() {
        return applicationPropertiesController;
    }

    public DownloadController getDownloadController() {
        return downloadController;
    }

    public LectureController getLectureController() {
        return lectureController;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public PropertiesController getPropertiesController() {
        return propertiesController;
    }

    public SubjectController getSubjectController() {
        return subjectController;
    }

    public void verifyNoMoreInteractions() {
        verify(mainController, atLeast(0)).getApplicationPropertiesController();
        verify(mainController, atLeast(0)).getPropertiesController();
        verify(mainController, atLeast(0)).getLoginController();
        verify(mainController, atLeast(0)).getSubjectController();
        verify(mainController, atLeast(0)).getLectureController();
        verify(mainController, atLeast(0)).getDownloadController();
        Mockito.verifyNoMoreInteractions(mainController, propertiesController, applicationPropertiesController, loginController, subjectController, lectureController, downloadController);
    }
}
