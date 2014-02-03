/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.util;

import java.util.Collection;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;

/**
 *
 * @author lee
 */
public class SubjectHelper {

    public static SelectionState getSubjectSelectionState(Subject subject) {
        int selected = 0;
        int notDled = 0;
        for (Lecture lecture : subject.getLectures()) {
            for (DownloadType dt : DownloadType.values()) {
                if (lecture.isDownloadEnabledOrPresentAndEnabled(dt)) {
                    selected++;
                    if (!lecture.isFilePresent(dt)) {
                        notDled++;
                    }
                }
            }
        }
        if (notDled == 0) {
            return SelectionState.ALL_DOWNLOADED;
        } else if (selected == 0) {
            return SelectionState.NONE_SELECTED;
        } else {
            return SelectionState.NOT_ALL_DOWNLOADED;
        }
    }

    public static SelectionState getSubjectsSelectionState(Collection<Subject> allSubjects) {
        if (allSubjects.isEmpty()) {
            return SelectionState.NONE_SELECTED;
        } else {
            int selected = 0;
            int notDled = 0;
            for (Subject s : allSubjects) {
                if (s.isDownloadEnabled()) {
                    switch (getSubjectSelectionState(s)) {
                        case NOT_ALL_DOWNLOADED:
                            notDled++;
                        case ALL_DOWNLOADED:
                            selected++;
                            break;
                    }
                }
            }
            if (selected == 0) {
                return SelectionState.NONE_SELECTED;
            } else if (notDled == 0) {
                return SelectionState.ALL_DOWNLOADED;
            } else {
                return SelectionState.NOT_ALL_DOWNLOADED;
            }
        }
    }

    public static enum SelectionState {

        NONE_SELECTED, NOT_ALL_DOWNLOADED, ALL_DOWNLOADED;
    }
}
