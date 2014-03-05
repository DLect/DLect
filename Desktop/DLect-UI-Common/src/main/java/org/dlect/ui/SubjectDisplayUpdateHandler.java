/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui;

import org.dlect.controller.helper.subject.SubjectDisplaySettingHelper;
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
        if (semester.getSubject().isEmpty()) {
            return false;
        }
        for (Subject s : semester.getSubject()) {
            if (!getDisplayHelper().isSubjectDisplayed(s)) {
                return false;
            }
        }
        return true;
    }

}
