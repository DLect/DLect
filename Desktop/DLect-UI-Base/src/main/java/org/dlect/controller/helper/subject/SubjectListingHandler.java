/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.logging.ControllerLogger;
import org.dlect.model.Database;
import org.dlect.model.Database.DatabaseEventID;
import org.dlect.model.Semester;
import org.dlect.model.Semester.SemesterEventID;
import org.dlect.model.Subject;
import org.dlect.model.Subject.SubjectEventID;

/**
 *
 * @author lee
 */
public class SubjectListingHandler implements EventListener {

    private final Map<String, Subject> subjects = Maps.newHashMap();

    public SubjectListingHandler(Database d) {
        for (Semester sem : d.getSemesters()) {
            addAll(sem.getSubjects());
        }

        d.addListener(this, Database.class, Semester.class, Subject.class);
    }

    public Map<String, Subject> getSubjectIdMap() {
        return ImmutableMap.copyOf(subjects);
    }

    public Set<Subject> getAllSubjects() {
        return ImmutableSet.copyOf(subjects.values());
    }

    @Override
    public void processEvent(Event e) {
        if (e.getEventID().equals(DatabaseEventID.SEMESTER)) {
            Semester before = (Semester) e.getBefore();
            Semester after = (Semester) e.getAfter();
            if (before != null) {
                subjects.values().removeAll(before.getSubjects());
            }
            if (after != null) {
                addAll(after.getSubjects());
            }
        } else if (e.getEventID().equals(SemesterEventID.SUBJECT)) {
            Subject before = (Subject) e.getBefore();
            Subject after = (Subject) e.getAfter();
            if (before != null) {
                subjects.values().remove(before);
            }
            if (after != null) {
                add(after);
            }
        } else if (e.getEventID().equals(SubjectEventID.ID)) {
            Subject source = (Subject) e.getSource();
            move(source);
        }

    }

    private void addAll(Collection<Subject> subjects) {
        for (Subject s : subjects) {
            add(s);
        }
    }

    private void add(Subject s) {
        Subject put = this.subjects.put(s.getId(), s);
        if (put != null && !s.equals(put)) {
            ControllerLogger.LOGGER.warn("Subject has identical ID as another.");
            ControllerLogger.LOGGER.warn("Old Subject: {}", put);
            ControllerLogger.LOGGER.warn("New Subject: {}", s);
        }
    }

    private void move(Subject source) {
        // Ensure source is removed.
        this.subjects.values().remove(source);
        // Now add it back in.
        add(source);
    }
}
