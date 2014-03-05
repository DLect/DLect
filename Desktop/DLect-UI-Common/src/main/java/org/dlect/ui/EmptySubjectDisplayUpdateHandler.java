/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.ui;

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
        Semester newest = getDatabase().getSemesters().last();
        System.out.println("Updating: " + newest);
        setSemesterEnabled(newest, true);    
    }
    
}
