/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray;

import org.lee.echo360.tray.util.prefix.MenuPrefixUpdater;
import java.awt.Desktop;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.control.controllers.DownloadProgressListener;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;
import org.lee.echo360.tray.util.prefix.LectureMenuPrefixUpdater;
import org.lee.echo360.tray.util.prefix.SubjectMenuPrefixUpdater;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public abstract class SubjectMenu implements PropertyChangeListener, DownloadProgressListener, ControllerListener, Comparable<SubjectMenu>, ActionListener {

    private static final DownloadType[] openingDownloadTypes = new DownloadType[]{DownloadType.VIDEO, DownloadType.AUDIO};

    public static SubjectMenu createMenuFor(Subject s, MainController mc) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        int numStreams = 0;
        boolean otherStream = false;
        for (Stream st : s.getStreams()) {
            if (st.isActualStream()) {
                numStreams++;
            } else if (!otherStream) {
                otherStream = true;
                numStreams++;
            }
        }
        //if (numStreams > 1) {
        return new StreamBasedSubjectMenu(s, mc);
        //} else {
        //return new BasicSubjectMenu(s, mc); // TODO write basic menu
        //}
    }
    private final Menu thisMenu = new Menu();
    private final Subject s;
    private final MainController mc;
    private final String defaultLabel;
    private final SubjectMenuPrefixUpdater menuPrefixUpdater;

    public SubjectMenu(Subject s, MainController mc) {
        this.s = s;
        this.mc = mc;
        defaultLabel = s.getName();
        thisMenu.setLabel(defaultLabel);
        menuPrefixUpdater = new SubjectMenuPrefixUpdater(thisMenu, defaultLabel, s);
        mc.addDownloadProgressListener(menuPrefixUpdater);
        mc.addControllerListener(menuPrefixUpdater);
        mc.addDownloadProgressListener(this);
        mc.addControllerListener(this);
    }

    public final MainController getMainCtl() {
        return mc;
    }

    public final Menu getMenu() {
        return thisMenu;
    }

    public final Subject getSubject() {
        return s;
    }

    protected final void openLecture(Lecture l) {
        for (DownloadType dt : openingDownloadTypes) {
            if (l.isFilePresent(dt)) {
                openLecture(l, dt);
                return;
            }
        }
    }

    protected final void openLecture(Lecture l, DownloadType dt) {
        if (l.isFilePresent(dt)) {
            try {
                File f = mc.getPropertiesController().getFileFor(s, l, dt);
                Desktop.getDesktop().open(f);
            } catch (IOException ex) {
                ExceptionReporter.reportException(ex);
            }
        }
    }

    protected final void addAllLectures(SortedSet<Lecture> lectures, Menu menu, Map<Lecture, MenuPrefixUpdater> map) {
        if (map == null) {
            map = new HashMap<Lecture, MenuPrefixUpdater>();
        }
        for (Lecture lecture : lectures) {
            MenuPrefixUpdater lmpu;
            MenuItem m;
            if (map.get(lecture) != null) {
                lmpu = map.get(lecture);
                m = lmpu.getMenuItem();
            } else {
                final String label = lecture.getTime().toString();
                m = new MenuItem(label);
                m.addActionListener(this);
                lmpu = new LectureMenuPrefixUpdater(m, label, s, lecture);
                mc.addControllerListener(lmpu);
                mc.addDownloadProgressListener(lmpu);
                map.put(lecture, lmpu);
            }
            m.setEnabled(isAnyFilePresent(lecture));
            menu.add(m);
        }
    }

    @Override
    public final int compareTo(SubjectMenu o) {
        return this.s.compareTo(o.s);
    }

    private boolean isAnyFilePresent(Lecture lecture) {
        for (DownloadType dt : openingDownloadTypes) {
            if (lecture.isFilePresent(dt)) {
                return true;
            }
        }
        return false;
    }
}
