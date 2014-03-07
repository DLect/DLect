/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import org.dlect.model.Database;
import org.dlect.model.Semester;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public abstract class SubjectDisplayUpdateHandler {

    private final SubjectDisplaySettingHelper displayHelper;
    private final Database database;

    public SubjectDisplayUpdateHandler(Database database) {
        this.database = database;
        this.displayHelper = new SubjectDisplaySettingHelper(database);
    }

    public SubjectDisplaySettingHelper getDisplayHelper() {
        return displayHelper;
    }

    public Database getDatabase() {
        return database;
    }

    public abstract void updateSubjects();

    protected boolean isSemesterEnabled(Semester semester) {
        if (semester.getSubjects().isEmpty()) {
            return false;
        }
        for (Subject s : semester.getSubjects()) {
            if (!getDisplayHelper().isSubjectDisplayed(s)) {
                return false;
            }
        }
        return true;
    }

    protected void setSemesterEnabled(Semester semester, boolean enabled) {
        for (Subject s : semester.getSubjects()) {
            getDisplayHelper().setSubjectDisplayed(s, enabled);
        }
    }

}
