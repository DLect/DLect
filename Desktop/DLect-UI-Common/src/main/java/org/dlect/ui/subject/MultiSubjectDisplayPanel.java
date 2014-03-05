/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.subject;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import javax.swing.JPanel;
import org.dlect.controller.MainController;
import org.dlect.controller.helper.subject.SubjectListingHandler;
import org.dlect.events.Event;
import org.dlect.events.EventID;
import org.dlect.events.EventListener;
import org.dlect.events.ListEvent;
import org.dlect.events.ListEventType;
import org.dlect.events.wrapper.Wrappers;
import org.dlect.model.Database;
import org.dlect.model.Database.DatabaseEventID;
import org.dlect.model.Semester;
import org.dlect.model.Semester.SemesterEventID;
import org.dlect.model.Subject;
import org.dlect.ui.CoursePane;
import org.dlect.ui.SubjectDisplayUpdater;

/**
 *
 * @author lee
 */
public class MultiSubjectDisplayPanel extends JPanel implements EventListener {
    
    private static final ImmutableSet<EventID> SUBJECT_REMOVE_EVENT_ID = ImmutableSet
            .<EventID>builder().add(DatabaseEventID.SEMESTER, SemesterEventID.SUBJECT).build();
    
    private static final long serialVersionUID = 1L;
    
    private final SubjectListingHandler listingHandler;
    private final SortedSet<Subject> shownSubjects;
    private final Map<Subject, CoursePane> subjectPanes;
    private final MainController controller;

    /**
     *
     * @param controller
     */
    public MultiSubjectDisplayPanel(MainController controller) {
        this.controller = controller;
        this.shownSubjects = Sets.newTreeSet();
        this.subjectPanes = Maps.newHashMap();
        this.listingHandler = new SubjectListingHandler(controller.getDatabaseHandler().getDatabase());
        SubjectDisplayUpdater.registerOn(controller);
        this.setLayout(new GridBagLayout());
        Wrappers.addSwingListenerTo(this, controller, Database.class, Semester.class);
    }
    
    private GridBagConstraints getDefaultContraints() {
        GridBagConstraints dcs = new GridBagConstraints();
        dcs.gridx = 0;
        dcs.weightx = 1;
        dcs.fill = GridBagConstraints.HORIZONTAL;
        dcs.anchor = GridBagConstraints.NORTH;
        return dcs;
    }
    
    public Set<Subject> getShownSubjects() {
        return ImmutableSet.copyOf(shownSubjects);
    }
    
    @Override
    public void processEvent(Event e) {
        if (e.getSourceClass().equals(Database.class) || e.getEventID().equals(SemesterEventID.SUBJECT)) {
            updateCoursePanelPositions();
        }
        if (e instanceof ListEvent && SUBJECT_REMOVE_EVENT_ID.contains(e.getEventID())) {
            ListEvent le = (ListEvent) e;
            if (le.getListEventType() == ListEventType.REMOVED || le.getListEventType() == ListEventType.REPLACED) {
                updateForRemoved();
            }
        }
    }
    
    protected void updateCoursePanelPositions() {
        // TODO(Later) improve this method to react to events better.
        GridBagConstraints tC = getDefaultContraints();
        tC.gridy = 0;
        SortedSet<Semester> semsesters = controller.getDatabaseHandler().getDatabase().getSemesters();
        for (Semester semester : semsesters) {
            for (Subject subject : semester.getSubject()) {
                CoursePane pane = subjectPanes.get(subject);
                if (controller.getSubjectDisplayHelper().isSubjectDisplayed(subject)) {
                    if (pane == null) {
                        pane = new CoursePane(controller);
                        pane.setSubject(subject);
                        subjectPanes.put(subject, pane);
                    }
                    this.remove(pane);
                    this.add(pane, tC);
                    tC.gridy++;
                    pane.loadLectures();
                } else {
                    if (pane != null) {
                        this.remove(pane);
                    }
                }
            }
        }
        this.validate();
    }
    
    protected void updateForRemoved() {
        Set<Subject> allSubjects = listingHandler.getAllSubjects();
        
        for (Entry<Subject, CoursePane> entry : subjectPanes.entrySet()) {
            Subject subject = entry.getKey();
            CoursePane coursePane = entry.getValue();
            if (!allSubjects.contains(subject)) {
                this.remove(coursePane);
                // TODO consider removing this entry from map as all insertations are well behaved.
            }
        }
    }
    
}
