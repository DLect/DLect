/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.model.ActionResult;

/**
 *
 * @author lee
 */
public class DownloadExecutor implements Runnable, ControllerListener {
    
    private final ApplicationController ctl;
    private ControllerAction currentState;
    private static final Runnable BLANK_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            // No Op
        }
    };
    
    public DownloadExecutor(ApplicationController ctl) {
        this.ctl = ctl;
        this.ctl.addControllerListener(this);
    }
    
    @Override
    public void run() {
        perform(ControllerAction.LOGIN);
    }
    
    @Override
    public void start(ControllerAction action) {
        this.currentState = action;
    }
    
    @Override
    public void finished(ControllerAction action, ActionResult r) {
        switch (r) {
            case FAILED:
            case FATAL:
            case NOT_CONNECTED:
                organiseRetryLater(action);
                break;
            case INVALID_CREDENTIALS:
            case NOT_LOGGED_IN:
                break;
            case SUCCEDED:
                ApplicationController.s.submit(performNextInRunnable(action));
        }
    }
    
    @Override
    public void error(Throwable e) {
        organiseRetryLater(this.currentState);
    }
    
    private void organiseRetryLater(final ControllerAction action) {
        ApplicationController.s.schedule(performNextInRunnable(action), 10, TimeUnit.MINUTES);
    }
    
    private void perform(ControllerAction controllerAction) {
        if (controllerAction == null) {
            return;
        }
        switch (controllerAction) {
            case LOGIN:
                ctl.getLoginController().doLogin();
                break;
            case COURSES:
                ctl.getSubjectController().getSubjects();
                break;
            case LECTURES:
                ctl.getLectureController().getAllLectures();
                break;
        }
    }
    
    public Runnable performNextInRunnable(final ControllerAction prevAction) {
        if (prevAction == ControllerAction.LECTURES) {
            // TODO start Download soon.
            return new Runnable() {
                @Override
                public void run() {
                    ctl.getDownloadController().downloadAllSelected(ctl.getPropertiesController().getBlackboard().getSubjects());
                }
            };
        } else if (prevAction != null) {
            return new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DownloadExecutor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    perform(prevAction.next());
                }
            };
        } else {
            return BLANK_RUNNABLE;
        }
    }
}
