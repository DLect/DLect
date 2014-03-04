package org.dlect.ui;

import com.google.common.collect.ImmutableSet;
import java.awt.GridBagConstraints;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.SwingUtilities;
import org.dlect.controller.MainController;
import org.dlect.controller.event.ControllerType;
import org.dlect.controller.helper.Controller;
import org.dlect.controller.helper.ControllerStateHelper;
import org.dlect.controller.helper.ControllerStateHelper.ControllerStateHelperEventID;
import org.dlect.controller.helper.SubjectDataHelper.DownloadState;
import org.dlect.controller.helper.subject.SubjectInformation;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.events.wrapper.Wrappers;
import org.dlect.model.Database;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Semester;
import org.dlect.model.Subject;
import org.dlect.ui.decorator.DownloadButtonDotter;
import org.dlect.ui.prefs.PreferencesDialog;
import org.dlect.ui.prefs.PreferencesDialogImpl;

import static org.dlect.controller.helper.SubjectDataHelper.DownloadState.*;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lee
 */
public class CoursesScreen extends javax.swing.JPanel implements
        EventListener {

    private static final long serialVersionUID = 1L;

    private final SortedSet<Subject> allSubjects;
    private final SortedMap<Subject, CoursePane> shownSubjects;
    private final GridBagConstraints defaultCourseConstraints;
    private final MainController controller;
    private final DownloadButtonDotter dbd;
    private PreferencesDialog prefsDialog;

    /**
     * Creates new form CoursesScreen
     *
     * @param controller
     */
    public CoursesScreen(MainController controller) {
        this.controller = controller;
        initComponents();
        defaultCourseConstraints = new GridBagConstraints();
        defaultCourseConstraints.gridx = 0;
        defaultCourseConstraints.weightx = 1;
        defaultCourseConstraints.fill = GridBagConstraints.HORIZONTAL;
        defaultCourseConstraints.anchor = GridBagConstraints.NORTH;
        shownSubjects = new TreeMap<>();
        allSubjects = new TreeSet<>();
        dbd = new DownloadButtonDotter(downloadAllButton);
        dbd.start();
        Wrappers.addSwingListenerTo(this, controller, Database.class, Semester.class, Subject.class, Lecture.class, LectureDownload.class, Controller.class);
    }

    public void addAllSubjects(final Collection<Subject> itt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                allSubjects.addAll(itt);
                updateCoursePanelPositions();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        bottomPanel = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        downloadAllButton = new javax.swing.JButton();
        coursesScrollPane = new javax.swing.JScrollPane();
        scrollPanelContainer = new javax.swing.JPanel();
        courseContainer = new javax.swing.JPanel();

        setMinimumSize(new java.awt.Dimension(400, 300));
        setLayout(new java.awt.GridBagLayout());

        bottomPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        bottomPanel.add(jSeparator1, gridBagConstraints);

        jButton1.setMnemonic('p');
        jButton1.setText("Preferences");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        bottomPanel.add(jButton1, gridBagConstraints);

        downloadAllButton.setText("Download All");
        downloadAllButton.setEnabled(false);
        downloadAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadAllButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        bottomPanel.add(downloadAllButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(bottomPanel, gridBagConstraints);

        coursesScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        scrollPanelContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPanelContainer.setLayout(new java.awt.GridBagLayout());

        courseContainer.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        scrollPanelContainer.add(courseContainer, gridBagConstraints);

        coursesScrollPane.setViewportView(scrollPanelContainer);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(coursesScrollPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void downloadAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadAllButtonActionPerformed
//    TODO    ThreadUtil.runExecution(new Runnable() {
//            @Override
//            public void run() {
//                //controller.getDownloadController().downloadAllSelected(new ArrayList<Subject>(shownSubjects.keySet()));
//            }
//        });
    }//GEN-LAST:event_downloadAllButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (prefsDialog == null) {
            prefsDialog = new PreferencesDialogImpl(SwingUtilities.getWindowAncestor(this), controller);
            prefsDialog.setLocationRelativeTo(this);
        }
        prefsDialog.setLocationByPlatform(true);
        prefsDialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel courseContainer;
    private javax.swing.JScrollPane coursesScrollPane;
    private javax.swing.JButton downloadAllButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel scrollPanelContainer;
    // End of variables declaration//GEN-END:variables

    private void updateCoursePanelPositions() {
        // TODO improve this method to react to events better.
        GridBagConstraints tC = (GridBagConstraints) defaultCourseConstraints.clone();
        tC.gridy = 0;
        for (Subject subject : allSubjects) {
            if (controller.getSubjectDisplayHelper().isSubjectDisplayed(subject)) {
                CoursePane pane = shownSubjects.get(subject);
                if (pane == null) {
                    pane = new CoursePane(controller);
                    pane.setSubject(subject);
                    shownSubjects.put(subject, pane);
                }
                courseContainer.remove(pane);
                courseContainer.add(pane, tC);
                tC.gridy++;
            } else {
                if (shownSubjects.containsKey(subject)) {
                    courseContainer.remove(shownSubjects.remove(subject));
                }
            }
        }

        // Remove all subjects that don't exist anymore.
        for (Iterator<Map.Entry<Subject, CoursePane>> it = shownSubjects.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Subject, CoursePane> es = it.next();
            if (!allSubjects.contains(es.getKey())) {
                courseContainer.remove(es.getValue());
                it.remove();
            }
        }
        courseContainer.validate();
        courseContainer.invalidate();
        coursesScrollPane.invalidate();
        coursesScrollPane.validate();
    }

    private void updateButtonState() {
        ControllerStateHelper csh = controller.getControllerStateHelper();
        Set<Subject> shown = ImmutableSet.copyOf(shownSubjects.keySet());
        for (Subject subject : shown) {
            if (csh.isDownloading(subject)) {
                dbd.updateAndStart("Downloading");
                return;
            }
            if (!csh.hasCompleted(ControllerType.LECTURE, subject)) {
                dbd.updateAndStart("Loading Data");
                return;
            }
        }

        int notDownloaded = 0;
        int selected = 0;

        for (Subject subject : shown) {
            SubjectInformation i = new SubjectInformation();
            i.setSubject(subject);
            // TODO simplify this by storing the information and invalidating it as events come in.
            selected += i.getDownloadsSelected();
            notDownloaded += i.getNotDownloadedCount();
            if (SubjectInformation.getDownloadedStatusFromCounts(notDownloaded, selected) == NOT_ALL_DOWNLOADED) {
                // >1 selected and >1 not downloaded.
                break;
            }
        }

        DownloadState s = SubjectInformation.getDownloadedStatusFromCounts(notDownloaded, selected);

        switch (s) {
            case ALL_DOWNLOADED:
                dbd.stop();
                downloadAllButton.setText("All Downloaded");
                downloadAllButton.setEnabled(false);
                break;
            case NONE_SELECTED:
                dbd.stop();
                downloadAllButton.setText("None Selected");
                downloadAllButton.setEnabled(false);
                break;
            default:
                dbd.stop();
                downloadAllButton.setText("Download All");
        }
    }

//    @Override
//    public void finished(final ControllerAction action, final ActionResult ar) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                PropertiesController c = controller.getPropertiesController();
//     TODO           final Blackboard b = c.getBlackboard();
//                switch (action) {
//                    case COURSES:
//                        allSubjects.clear();
//                        addAllSubjects(b.getSubjects());
//                        break;
//                }
//                updateButtonState();
//            }
//        });
//    }
    @Override
    public void processEvent(Event e) {
        if (e.getEventID().equals(ControllerStateHelperEventID.DOWNLOAD)) {
            updateButtonState(); // TODO update this.
        }
        if (e.getEventID().equals(ControllerStateHelperEventID.CONTROLLER)) {
            updateButtonState();
            /*
             * Update subject listing.
             */
            updateCoursePanelPositions();
        }
        if (true /*
                 * Subject's displayed status changes
                 */) {
            updateCoursePanelPositions();
        }

    }

}