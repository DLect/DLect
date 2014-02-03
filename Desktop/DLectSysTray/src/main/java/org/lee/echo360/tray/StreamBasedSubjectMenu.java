/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;
import org.lee.echo360.tray.util.prefix.MenuPrefixUpdater;
import org.lee.echo360.tray.util.prefix.StreamMenuPrefixUpdater;

/**
 *
 * @author lee
 */
public class StreamBasedSubjectMenu extends SubjectMenu {

    BiMap<MenuPrefixUpdater, Lecture> menuMap = HashBiMap.create();
    Map<Stream, MenuPrefixUpdater> streamMenuMap = new HashMap<Stream, MenuPrefixUpdater>();

    public StreamBasedSubjectMenu(Subject s, MainController mc) {
        super(s, mc);
        updateSubject();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void downloadingStarted(Subject subject) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void downloadingFinished(Subject subject) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void downloadStarting(Subject s, Lecture l, DownloadType t) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void downloadCompleted(Subject s, Lecture l, DownloadType t) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start(ControllerAction action) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void finished(ControllerAction action, ActionResult r) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void error(Throwable e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o instanceof MenuItem) {
            MenuItem m = (MenuItem) o;
            MenuPrefixUpdater wrapped = MenuPrefixUpdater.wrapForEquality(m);
            if (menuMap.containsKey(wrapped)) {
                Lecture l = menuMap.get(wrapped);
                openLecture(l);
            }
        }
    }

    private void updateSubject() {
        this.getMenu().removeAll();
        TreeSet<Lecture> otherLectures = new TreeSet<Lecture>();
        for (Stream stream : getSubject().getStreams()) {
            if (stream.isActualStream()) {
                SortedSet<Lecture> lectures = stream.getLectures();
                if (!lectures.isEmpty()) {
                    String label = stream.getName();
                    Menu m = new Menu(label);

                    StreamMenuPrefixUpdater smpu = new StreamMenuPrefixUpdater(m, label, getSubject(), stream);
                    streamMenuMap.put(stream, smpu);
                    getMainCtl().addControllerListener(smpu);
                    getMainCtl().addDownloadProgressListener(smpu);

                    addAllLectures(lectures, m, menuMap.inverse());
                    this.getMenu().add(m);
                }
            } else {
                otherLectures.addAll(stream.getLectures());
            }
        }
        if (!otherLectures.isEmpty()) {
            String label = "Other";
            Menu m = new Menu(label);

            StreamMenuPrefixUpdater smpu = new StreamMenuPrefixUpdater(m, label, getSubject(), null); // Null has special meaning(I.E. Other)
            streamMenuMap.put(null, smpu);
            getMainCtl().addControllerListener(smpu);
            getMainCtl().addDownloadProgressListener(smpu);

            addAllLectures(otherLectures, m, menuMap.inverse());
            this.getMenu().addSeparator();
            this.getMenu().add(m);
        }
    }
}
