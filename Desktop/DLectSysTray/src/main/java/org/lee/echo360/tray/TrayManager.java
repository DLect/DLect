/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray;

import org.lee.echo360.tray.icon.TrayIconManager;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.control.controllers.DownloadProgressListener;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.model.ActionResult;
import static org.lee.echo360.model.ActionResult.FAILED;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;

/**
 *
 * @author lee
 */
public class TrayManager implements ControllerListener, DownloadProgressListener {

    private final TrayIcon icon;
    private final PopupManager manager;
    private final MainController ctl;
    private ControllerAction currentState = null;
    private final List<Subject> downloadingSubjects = new ArrayList<Subject>();

    public TrayManager(MainController ctl) throws AWTException {
        this.ctl = ctl;
        icon = new TrayIcon(TrayIconManager.DEFAULT.getImage());
        manager = new PopupManager(ctl);
        icon.setPopupMenu(manager.getPopup());
        ctl.addControllerListener(this);
        ctl.addDownloadProgressListener(this);
        SystemTray.getSystemTray().add(icon);
    }

    public void setIcon(TrayIconManager type) {
        icon.setImage(type.getImage());
    }

    public void setToolTip(String tooltip) {
        icon.setToolTip(tooltip);
    }

    @Override
    public void start(ControllerAction action) {
        currentState = action;
        setIcon(TrayIconManager.PROCESSING);
    }

    @Override
    public void finished(ControllerAction action, ActionResult r) {
        currentState = null;
        switch (r) {
            case SUCCEDED:
                setIcon(TrayIconManager.DEFAULT);
                break;
            case FAILED:
            case FATAL:
                setIcon(TrayIconManager.ERROR);
                setToolTip("Unknown Error");
                break;
            case INVALID_CREDENTIALS:
            case NOT_LOGGED_IN:
                setIcon(TrayIconManager.ERROR);
                setToolTip("Credential Error");
                break;
            case NOT_CONNECTED:
                setIcon(TrayIconManager.NO_CONNECTION);
                setToolTip("No Connection Detected");
        }
    }

    @Override
    public void error(Throwable e) {
        if (e instanceof IOException) {
            setIcon(TrayIconManager.NO_CONNECTION);
        } else {
            setIcon(TrayIconManager.ERROR);
        }
    }

    @Override
    public void downloadingStarted(Subject subject) {
        setIcon(TrayIconManager.DOWNLOADING);
        downloadingSubjects.add(subject);
    }

    @Override
    public void downloadingFinished(Subject subject) {
        downloadingSubjects.remove(subject);
        if (downloadingSubjects.isEmpty()) {
            setIcon(TrayIconManager.DEFAULT);
        }
    }

    @Override
    public void downloadStarting(Subject s, Lecture l, DownloadType t) {
        // TODO change
    }

    @Override
    public void downloadCompleted(Subject s, Lecture l, DownloadType t) {
        // TODO change
    }
}
