/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import org.dlect.model.Database;
import org.dlect.model.Semester;

/**
 *
 * @author lee
 */
public class EmptySubjectDisplayUpdateHandler extends SubjectDisplayUpdateHandler {

    public EmptySubjectDisplayUpdateHandler(Database d) {
        super(d);
    }

    @Override
    public void updateSubjects() {
        // Last semeseter is the most recent.
        if (getDatabase().getSemesters().isEmpty()) {
            // Nothing to update so don't try.
            return;
        }
        Semester newest = getDatabase().getSemesters().last();
        
        setSemesterEnabled(newest, true);
    }

}
