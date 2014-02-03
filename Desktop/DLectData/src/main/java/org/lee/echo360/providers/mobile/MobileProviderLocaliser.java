/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import java.net.URI;
import java.util.Date;
import java.util.List;
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;

/**
 *
 * @author lee
 */
public interface MobileProviderLocaliser {

    /**
     *
     * @param name
     *
     * @return The name of the course, or `null` if the course should be
     * excluded
     */
    public String getCourseName(String name);

    /**
     *
     * @param name
     * @param courseId
     * @param bbid
     * @param enrollmentdate
     *
     * @return The semester number(arbitrary or otherwise) where a smaller
     * semester number indicates a semester further in the past. Return
     * {@linkplain Integer#MIN_VALUE} to discard this course.
     */
    public int getSemesterNumber(String name, String courseId, String bbid, Date enrollmentdate);

    /**
     *
     * @param name
     * @param courseId
     * @param bbid
     * @param enrollmentdate
     *
     * @return A new semester based on the semester number . Return {@code null}
     * to discard this course.
     */
    public String getSemesterCoursePostfixName(int semesterNum);

    public String getSemesterLongName(int semesterNum);

    public Date getLectureTime(URI u, String title, String xmlData);

    public List<Stream> getLectureStream(URI u, String title, String rawXml, Subject s);

    public String getVideoFileName(URI u, String title, Date d, Subject sub, List<Stream> streams);

    public String getAudioFileName(URI u, String title, Date d, Subject sub, List<Stream> streams);

    public String getCourseFileName(String name, String courseName);
}
