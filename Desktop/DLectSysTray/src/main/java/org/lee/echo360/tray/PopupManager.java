/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import javax.swing.Timer;
import org.apache.commons.lang3.StringUtils;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.control.controllers.DownloadProgressListener;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.lee.echo360.ui.AboutDialog;

/**
 *
 * @author lee
 */
public class PopupManager implements ControllerListener, DownloadProgressListener, ActionListener {
//TODO Write

    /*
     * LAYOUT:
     *
     * | %Signed In: s42...    %
     * | %Last Check: Just Now %
     * | %Downloading 1 of 3   %
     * | -----------------------
     * | CSSE2002              >
     *  -\ 2012-05-07 12:00
     * | DECO2500              >
     *  -\ 2012-05-06 08:00
     * | MATH2000              >
     *  -\ L1                  >
     *    -\ 2012-05-07 12:00
     *  -\ L2                  >
     *    -\ 2012-02-27 16:00
     * | -----------------------
     * | Preferences
     * | About
     * | Quit
     */
    private final PopupMenu menu;
    private final MenuItem userStatus;
    private final MenuItem lastCheck;
    private final MenuItem currentProcess;
    private final Map<Subject, SubjectMenu> subjectMenus;
    private final List<Subject> downloadsInProgress;
    private final MenuItem noSubjects;
    private final MenuItem preferences;
    private final MenuItem about;
    private final MenuItem quit;
    private final MainController ctl;
    private long lastUpdateTime = Long.MAX_VALUE;
    private boolean processing = true;
    private ActionResult loginStatus = ActionResult.SUCCEDED;
    private AboutDialog aboutDialog = new AboutDialog() {
        @Override
        public String getProductName() {
            return "DLect Tray Icon";
        }

        @Override
        public String getDescription() {
            return "The DLect Tray Icon is designed to provide you"
                    + " with easy access to all your lectures and"
                    + " will automatically download any new ones.";
        }
    };

    public PopupManager(MainController ctl) {
        this.ctl = ctl;
        this.ctl.addControllerListener(this);
        this.ctl.addDownloadProgressListener(this);

        this.subjectMenus = new TreeMap<Subject, SubjectMenu>();
        this.downloadsInProgress = new ArrayList<Subject>(4);
        this.menu = new PopupMenu();

        userStatus = new MenuItem("Not Signed In");
        userStatus.setEnabled(false);
        lastCheck = new MenuItem("Next Check: Now");
        lastCheck.setEnabled(false);
        currentProcess = new MenuItem("Starting Up");
        currentProcess.setEnabled(false);
        updateUserStatus();

        noSubjects = new MenuItem("No Subjects Yet");
        noSubjects.setEnabled(false);

        preferences = new MenuItem("Preferences");
        preferences.addActionListener(this);
        about = new MenuItem("About DLect");
        about.addActionListener(this);
        quit = new MenuItem("Quit");
        quit.addActionListener(this);


        menu.add(userStatus);
        menu.add(lastCheck);
        menu.add(currentProcess);
        menu.addSeparator();
        updateCourseMap(); // This adds the bottom part of the menu

        Timer t = new Timer(60 * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long currTime = System.currentTimeMillis();
                long millisPast = currTime - lastUpdateTime;
                if (!processing && millisPast > 0) {
                    lastCheck.setLabel("Last Check: " + toNiceString(millisPast));
                } else {
                    lastCheck.setLabel("Last Check: Just Now");
                }
            }
        });
        t.start();
    }

    public PopupMenu getPopup() {
        return menu;
    }

    @Override
    public void start(ControllerAction action) {
        updateLastUpdateTime();
        processing = true;
        switch (action) {
            case LOGIN:
                lastCheck.setLabel("Just Now");
                currentProcess.setLabel("Logging In...");
                break;
            case COURSES:
                currentProcess.setLabel("Finding Courses...");
                break;
            case LECTURES:
                currentProcess.setLabel("Finding Lectures...");
                break;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public void finished(ControllerAction action, ActionResult r) {
        updateLastUpdateTime();
        processing = false;
        switch (action) {
            case LOGIN:
                loginStatus = r;
                updateUserStatus();
                break;
            case COURSES:
                updateCourses(r);
                currentProcess.setLabel("Finding Lectures...");
                break;
            case LECTURES:
                currentProcess.setLabel("Download Starting");
                break;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public void error(Throwable e) {
    }

    @Override
    public void downloadingStarted(Subject subject) {
        updateLastUpdateTime();
        currentProcess.setLabel("Downloading");
        downloadsInProgress.add(subject);
    }

    @Override
    public void downloadingFinished(Subject subject) {
        updateLastUpdateTime();
        downloadsInProgress.remove(subject);
        if (downloadsInProgress.isEmpty()) {
            currentProcess.setLabel("Idle");
        }
    }

    @Override
    public void downloadStarting(Subject s, Lecture l, DownloadType t) {
        updateLastUpdateTime();
    }

    @Override
    public void downloadCompleted(Subject s, Lecture l, DownloadType t) {
        updateLastUpdateTime();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(preferences)) {
            // TODO open Preferences
        } else if (e.getSource().equals(about)) {
            aboutDialog.setVisible(true);
        } else if (e.getSource().equals(quit)) {
            System.exit(0); // TODO change this to be more graceful.
        }
    }

    private void updateCourses(ActionResult r) {
        switch (r) {
            case SUCCEDED:
                updateCourseMap();
                break;
            default:
                loginStatus = r;
                updateUserStatus();
        }
    }

    private void updateCourseMap() {
        try {
            Set<Subject> s = ctl.getPropertiesController().getBlackboard().getSubjects();
            for (Subject subject : s) {
                if (!subjectMenus.containsKey(subject)) {
                    subjectMenus.put(subject, SubjectMenu.createMenuFor(subject, ctl));
                }
            }
            while (menu.getItemCount() > 4) {
                menu.remove(4);
            }
            subjectMenus.keySet().retainAll(s); // Keep all the newer ones
            for (Map.Entry<Subject, SubjectMenu> entry : subjectMenus.entrySet()) {
                menu.add(entry.getValue().getMenu());
            }
            addMenuBottom();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void addMenuBottom() {
        menu.addSeparator();

        menu.add(preferences);
        menu.add(about);
        menu.add(quit);
    }

    private void updateUserStatus() {
        ctl.getPropertiesController().initBlackboard();
        Blackboard b = ctl.getPropertiesController().getBlackboard();
        if (b == null || b.getUsername().isEmpty() || b.getPassword().isEmpty()) {
            userStatus.setLabel("Not Signed In");
        }
        switch (loginStatus) {
            case NOT_LOGGED_IN:
                userStatus.setLabel("Not Signed In");
                break;
            case SUCCEDED:
                userStatus.setLabel("Signed In: " + b.getUsername());
                break;
            case FATAL:
            case FAILED:
            case INVALID_CREDENTIALS:
                userStatus.setLabel("Bad Credentials");
                break;
            case NOT_CONNECTED:
                userStatus.setLabel("No Connection");
            default:
                throw new AssertionError();
        }
    }

    private String toNiceString(long millisPast) {
        long mins = TimeUnit.MILLISECONDS.toMinutes(millisPast);
        long hrs = TimeUnit.MILLISECONDS.toHours(millisPast);
        StringBuilder b = new StringBuilder();
        if (hrs > 0) {
            b.append(hrs).append(" Hour");
            if (hrs > 1) {
                b.append('s');
            }
            if (mins > 0) {
                b.append(", ");
            }
        }
        if (mins > 0) {
            b.append(mins).append(" Minute");
            if (mins > 1) {
                b.append('s');
            }
        }
        if (b.length() == 0) {
            b.append("1 Minute");
        }
        b.append(" Ago");
        return b.toString();
    }

    public void updateLastUpdateTime() {
        lastUpdateTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1);
    }
}
