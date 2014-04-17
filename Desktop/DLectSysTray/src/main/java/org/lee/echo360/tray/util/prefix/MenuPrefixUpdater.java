/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray.util.prefix;

import java.awt.Font;
import java.awt.MenuItem;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.control.controllers.DownloadProgressListener;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;

/**
 *
 * @author lee
 */
public class MenuPrefixUpdater implements DownloadProgressListener, ControllerListener {
    //2193

    public static MenuPrefixUpdater wrapForEquality(MenuItem i) {
        return new MenuPrefixUpdater(i, null, null) {
            @Override
            public void downloadCompleted(Subject s, Lecture l, DownloadType t) {
                // NO OP
            }

            @Override
            public void downloadStarting(Subject s, Lecture l, DownloadType t) {
                // NO OP
            }

            @Override
            public void downloadingFinished(Subject subject) {
                // NO OP
            }

            @Override
            public void downloadingStarted(Subject subject) {
                // NO OP
            }

            @Override
            public void start(ControllerAction action) {
                // NO OP
            }

            @Override
            public void finished(ControllerAction action, ActionResult r) {
                // NO OP
            }

            @Override
            public void error(Throwable e) {
                // NO OP
            }
        };
    }
    private static final char downloadArrowCharCode = (char) 0x2193;
    private static final char updatingArrowCharCode = (char) 0x21BB;
    private String downloadPrefix = downloadArrowCharCode + " ";
    private String updatingPrefix = updatingArrowCharCode + " ";
    private boolean fontsInited = false;
    private final MenuItem m;
    private final String defaultLabel;
    private final Subject s;

    public MenuPrefixUpdater(MenuItem m, String defaultLabel, Subject s) {
        this.m = m;
        this.defaultLabel = defaultLabel;
        this.s = s;
        initFonts();
    }

    public Subject getSubject() {
        return s;
    }

    public MenuItem getMenuItem() {
        return m;
    }

    private void initFonts() {
        if (!fontsInited) {
            Font f = m.getFont();
            if (f != null) {
                if (!f.canDisplay(downloadArrowCharCode)) {
                    downloadPrefix = "(d) ";
                }
                if (!f.canDisplay(updatingArrowCharCode)) {
                    updatingPrefix = "";
                }
                fontsInited = true;
            }
        }
    }

    @Override
    public void downloadStarting(Subject s, Lecture l, DownloadType t) {
        // No Op
    }

    @Override
    public void downloadCompleted(Subject s, Lecture l, DownloadType t) {
        // No Op
    }

    @Override
    public void downloadingStarted(Subject subject) {
        // No Op
    }

    @Override
    public void downloadingFinished(Subject subject) {
        // No Op
    }

    @Override
    public void start(ControllerAction action) {
        if (action == ControllerAction.COURSES || action == ControllerAction.LECTURES) {
            appendUpdatingPrefix();
        }
    }

    @Override
    public void finished(ControllerAction action, ActionResult r) {
        if (action == ControllerAction.LECTURES || (action == ControllerAction.COURSES && !r.isSuccess())) {
            resetMenuLabel();
        }
    }

    @Override
    public void error(Throwable e) {
        // No Op
    }

    protected final void appendDownloadPrefix() {
        initFonts();
        m.setLabel(downloadPrefix + defaultLabel);
    }

    protected final void resetMenuLabel() {
        initFonts();
        m.setLabel(defaultLabel);
    }

    protected final void appendUpdatingPrefix() {
        initFonts();
        m.setLabel(updatingPrefix + defaultLabel);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.m != null ? this.m.hashCode() : 0);
        return hash;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MenuPrefixUpdater)) {
            return false;
        }
        final MenuPrefixUpdater other = (MenuPrefixUpdater) obj;
        if (this.m != other.m && (this.m == null || !this.m.equals(other.m))) {
            return false;
        }
        return true;
    }
}
