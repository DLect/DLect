/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import com.google.common.collect.ImmutableSortedSet;
import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.dlect.events.EventID;
import org.dlect.events.listenable.EventBuilder;
import org.dlect.model.helper.XmlListenable;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Semester extends XmlListenable<Semester> implements Comparable<Semester> {

    @XmlElement(name = "number")
    private int num;

    @XmlElement(name = "name")
    private String longName;

    @XmlElement(name = "prefix")
    private String coursePostfixName;

    @XmlElementWrapper(name = "subjects")
    @XmlElement(name = "subject")
    private final SortedSet<Subject> subjects;

    public Semester() {
        subjects = newWrappedListenableSortedSet(SemesterEventID.SUBJECT);
    }

    @Override
    public int compareTo(Semester o) {
        if (o == null) {
            return -1;
        }
        return Integer.compare(this.getNum(), o.getNum());
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        EventBuilder<Integer> b = event(SemesterEventID.NUMBER).before(getNum());
        this.num = num;
        b.after(getNum()).fire();
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        EventBuilder<String> b = event(SemesterEventID.NUMBER).before(getLongName());
        this.longName = longName;
        b.after(getLongName()).fire();
    }

    public String getCoursePostfixName() {
        return coursePostfixName;
    }

    public void setCoursePostfixName(String coursePostfixName) {
        EventBuilder<String> b = event(SemesterEventID.NUMBER).before(getCoursePostfixName());
        this.coursePostfixName = coursePostfixName;
        b.after(getCoursePostfixName()).fire();
    }

    /**
     * An detached immutable copy of the subjects in this semester. The subjects are not copied so any changes made to
     * the subjects contained in this list will affect the objects stored in this object.
     *
     * @return An immutable copy of the subjects in this semester.
     */
    public ImmutableSortedSet<Subject> getSubjects() {
        return copyOf(subjects);
    }

    /**
     * Store only these subjects in this semester objects. This function will fire events correctly.
     *
     * @param subjects The set of subjects to copy and store in this semester.
     */
    public void setSubjects(Collection<Subject> subjects) {
        setSet(this.subjects, subjects);
    }

    @Override
    public Map<String, Object> getObjectsToFormat() {
        return build()
                .put("num", getNum())
                .put("longName", getLongName())
                .put("coursePostfixName", getCoursePostfixName())
                .put("subject", getSubjects())
                .build();
    }

    /**
     * The Event IDs for the semester object.
     */
    public static enum SemesterEventID implements EventID {

        /**
         * An event representing a change in the semester ID. This is the unique ID.
         */
        NUMBER,
        /**
         * An event representing a change in the semester name.
         */
        LONG_NAME,
        /**
         * An event representing a change in the semester's course prefix..
         */
        COURSE_PREFIX,
        /**
         * A <b>list</b> event representing a change in the subjects set contained in the semester object.
         */
        SUBJECT;

        @Override
        public Class<?> getAppliedClass() {
            return Semester.class;
        }

        @Override
        public String getName() {
            return name();
        }

    }

}
