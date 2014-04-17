/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.helper;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.dlect.controller.event.ControllerType;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.logging.ControllerLogger;
import org.dlect.model.Lecture;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.model.helper.ThreadLocalDateFormat;

/**
 *
 * @author lee
 */
public class ControllerErrorBoxHelper {

    private static final String HTML_PREFIX = "<html><p style='width: 200px;'>";
    private static final ThreadLocalDateFormat LECTURE_DATE_FORMAT = new ThreadLocalDateFormat("yyyy-MM-dd HH:mm");

    public static void showErrorBox(Component parent, ControllerType type, Object parameter, DLectExceptionCause get) {
        showErrorBox(parent, get, generateProgramStage(type, parameter));
    }

    public static void showDownloadError(Component parent, Subject subject, Lecture lecture, DownloadType downloadType, DLectExceptionCause failureCause) {
        String programStage = "to download the ";
        switch (downloadType) {
            case AUDIO:
                programStage += "audio";
                break;
            case VIDEO:
                programStage += "video";
                break;
            default:
                programStage += "unknown";
        }

        programStage += " file for the " + subject.getName() + "(id=" + subject.getId() + ") lecture on "
                        + LECTURE_DATE_FORMAT.format(lecture.getTime()) + "(id=" + lecture.getContentID() + ")";

        showErrorBox(parent, failureCause, programStage);
    }

    public static void showErrorBox(Component parent, DLectExceptionCause get, String programStage) {
        String title = get.toString();
        JLabel content = new JLabel(title + " - " + programStage);
        switch (get) {
            case BAD_CREDENTIALS:
                content = new JLabel(HTML_PREFIX + "The provider has rejected your credentials whilst attempting " + programStage + ". Please ensure they are correct and try again.");
                title = "Invalid Credentials";
                break;
            case DISK_ERROR:
                content = new JLabel(HTML_PREFIX + "Failed to access the disk whilst attempting " + programStage + ", please check disk permissions and space.");
                title = "Error Accessing Disk";
                break;
            case ILLEGAL_PROVIDER_STATE:
                content = new JLabel(HTML_PREFIX + "The provider was not configured as expected and found out whilst attempting " + programStage + ". This is most likely a problem with DLect and should be reported.");
                title = "Internal Provider Error";
                break;
            case ILLEGAL_SERVICE_RESPONCE:
                content = new JLabel(HTML_PREFIX + "The external provider failed to return valid data whilst attempting " + programStage + ". This is most likely a problem with the servers themselves and not with DLect.");
                title = "TODO";
                break;
            case NO_CONNECTION:
                content = new JLabel(HTML_PREFIX + "No internet connection detected whilst attempting " + programStage + ".");
                title = "No Internet Connection";
                break;
            case UNCAUGHT_UNKNOWN_EXCEPTION:
            case PROVIDER_CONTRACT:
                content = new JLabel(HTML_PREFIX + "The provider failed whilst attempting " + programStage + ". This is a problem with DLect and should be reported.");
                title = "TODO";
                break;
            case INVALID_DATA_FORMAT:
                content = new JLabel("An unkown error occured with the provider.");
                title = "Unknown Error";
                break;
        }
        ControllerLogger.LOGGER.error("Title: {}\nContent: {}", title, content.getText());
        JOptionPane.showMessageDialog(parent, content, title, JOptionPane.ERROR_MESSAGE);
    }

    private static String generateProgramStage(ControllerType type, Object parameter) {
        switch (type) {
            case LOGIN:
                return "to login";
            case SUBJECT:
                return "to locate subjects";
            case LECTURE:
                if (parameter instanceof Subject) {
                    Subject s = (Subject) parameter;

                    return "to find lectures in a subject called " + s.getName() + "(id=" + s.getId() + ")";
                } else {
                    ControllerLogger.LOGGER.error("Failed to convert parameter to subject for a lecture controller error.");
                    ControllerLogger.LOGGER.error("Parameter as recieved: " + parameter);

                    return "to find lectures in an unknown subject.";
                }
            default:
                throw new IllegalStateException("Reached default case statement.");
        }
    }

}
