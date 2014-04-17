/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import org.dlect.model.Database;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class SubjectDisplaySettingHelper {

    private static final String KEY_PREFIX = "SUBJECT_DISPLAYED_BBID::";
    private final Database database;

    public SubjectDisplaySettingHelper(Database database) {
        this.database = database;
    }

    public String getKeyFor(Subject s) {
        return getKeyFor(s.getId());
    }

    public String getKeyFor(String bbid) {
        return KEY_PREFIX + bbid;
    }

    public boolean isSubjectDisplayed(Subject s) {
        String displayedKey = getKeyFor(s);
        String displayed = database.getSetting(displayedKey);
        if (displayed == null) {
            setSubjectDisplayed(s, false);
            return false;
        }
        return Boolean.valueOf(displayed);
    }

    public void setSubjectDisplayed(Subject s, boolean b) {
        String displayedKey = getKeyFor(s);

        database.addSetting(displayedKey, Boolean.toString(b));
    }
}
