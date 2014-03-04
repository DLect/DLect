/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard;

import com.google.common.base.Optional;
import java.util.Date;
import org.dlect.immutable.model.ImmutableSemester;

/**
 *
 * @author lee
 */
public interface BlackboardSubjectCustomiser {

    /**
     *
     * @param name
     *
     * @return The name of the course, or an absent optional to ignore this subject
     */
    public Optional< String> getSubjectName(String name, String courseId, String bbid);

    /**
     *
     * @param name
     * @param courseId
     * @param bbid
     * @param enrollmentdate
     *
     * @return The semester number(arbitrary or otherwise) where a smaller
     *         semester number indicates a semester further in the past.
     */
    public ImmutableSemester getSemesterFor(String name, String courseId, String bbid, Date enrollmentdate);

}
