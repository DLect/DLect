/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import java.util.SortedSet;
import org.dlect.model.Database;
import org.dlect.model.Semester;

/**
 *
 * @author lee
 */
public class UpdatingSubjectDisplayUpdateHandler extends SubjectDisplayUpdateHandler {

    private final SortedSet<Semester> enabledSemesters;
    private final int mostRecentSemesterCode;

    public UpdatingSubjectDisplayUpdateHandler(Database d) {
        super(d);
        Builder<Semester> builder = ImmutableSortedSet.naturalOrder();
        int sem = Integer.MIN_VALUE;

        for (Semester semester : d.getSemesters()) {
            if (semester.getNum() > sem) {
                sem = semester.getNum();
            }
            if (isSemesterEnabled(semester)) {
                builder.add(semester);
            }
        }
        this.enabledSemesters = builder.build();
        this.mostRecentSemesterCode = sem;
    }

    @Override
    public void updateSubjects() {
                ImmutableSortedSet<Semester> semesters = getDatabase().getSemesters();
        if (semesters.isEmpty()) {
            return;
        }
        Semester last = semesters.last();
        if (last.getNum() > mostRecentSemesterCode) {
            setSemesterEnabled(last, true);
        }
        for (Semester s : semesters) {
            if (enabledSemesters.contains(s)) {
                setSemesterEnabled(s, true);
            }
        }
    }

}
