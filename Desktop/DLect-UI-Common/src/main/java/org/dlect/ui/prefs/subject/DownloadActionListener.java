/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs.subject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.dlect.controller.MainController;
import org.dlect.model.Lecture;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 * TODO 
 */
@Deprecated
public class DownloadActionListener implements ActionListener {

    private static final ExecutorService service = Executors.newCachedThreadPool();

    public static DownloadActionListener create(Subject s, Lecture l, DownloadType dt, MainController c, PropertyChangeListener... ls) {
        final DownloadActionListener dal = new DownloadActionListener(s, l, dt, c);
        for (PropertyChangeListener pcl : ls) {
            dal.pcs.addPropertyChangeListener(pcl);
        }
        return dal;
    }
    private boolean active = false;
    private final Subject s;
    private final Lecture l;
    private final DownloadType dt;
    private final MainController c;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     *
     * @param s
     * @param l
     * @param dt
     * @param c
     */
    public DownloadActionListener(Subject s, Lecture l, DownloadType dt, MainController c) {
        this.s = s;
        this.l = l;
        this.dt = dt;
        this.c = c;
    }

    public Lecture getL() {
        return l;
    }

    public DownloadType getDt() {
        return dt;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() instanceof JCheckBox) {
//            boolean sel = ((JCheckBox) e.getSource()).isSelected();
//            l.setDownloadEnabled(dt, sel);
//            if (sel) {
//                l.setEnabled(sel);
//            }
//        } else if (e.getSource() instanceof JButton) {
//            doDownloadLecture();
//        }
    }

    public void doDownloadLecture() {
//        if (!active) {
//            active = true;
//            service.submit(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        boolean downloadEnabled = l.isDownloadEnabledOrPresentAndEnabled(dt);
//                        boolean lectureEnabled = l.isEnabled();
//                        pcs.firePropertyChange("Lecture", downloadEnabled, true);
//                        l.setDownloadEnabled(dt, true);
//                        l.setEnabled(true);
//                        try {
//                            c.getDownloadController().downloadLecture(s, l, dt);
//                        } finally {
//                            l.setEnabled(lectureEnabled);
//                            active = false;
//                            if (!l.isFilePresent(dt)) {
//                                l.setDownloadEnabled(dt, downloadEnabled);
//                                pcs.firePropertyChange("Lecture", true, downloadEnabled);
//                            }
//                        }
//                    } catch (Throwable t) {
//                        ExceptionReporter.reportException(t);
//                    }
//                }
//            });
//        }
    }
}
