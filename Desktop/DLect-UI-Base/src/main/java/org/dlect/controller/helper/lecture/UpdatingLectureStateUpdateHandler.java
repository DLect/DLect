/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.lecture;

import java.util.SortedSet;
import org.dlect.model.Semester;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class UpdatingLectureStateUpdateHandler extends LectureStateUpdateHandler {

    private SortedSet<Semester> enabledSemesters;
    private int mostRecentSemesterCode;

    public UpdatingLectureStateUpdateHandler(Subject d) {
        super(d);
    }

    @Override
    protected void initImpl() {
//        Builder<Semester> builder = ImmutableSortedSet.naturalOrder();
//        int sem = Integer.MIN_VALUE;
//
//        for (Semester semester : d.getSemesters()) {
//            if (semester.getNum() > sem) {
//                sem = semester.getNum();
//            }
//            if (isSemesterEnabled(semester)) {
//                builder.add(semester);
//            }
//        }
//        this.enabledSemesters = builder.build();
//        this.mostRecentSemesterCode = sem;
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateLecturesImpl() {
        // TODO implement updating the subject's lectures.
//        for (Semester s : getSubject().getSemesters()) {
//            // TODO
//        }

    }

}
