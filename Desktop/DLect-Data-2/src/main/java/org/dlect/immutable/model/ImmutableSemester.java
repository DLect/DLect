/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.immutable.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import org.dlect.model.Semester;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class ImmutableSemester {

    private final int num;
    private final String longName;
    private final String coursePostfixName;
    private final ImmutableSet<ImmutableSubject> subject;

    public ImmutableSemester(int num, String longName, String coursePostfixName, Collection<ImmutableSubject> subject) {
        this.num = num;
        this.longName = longName;
        this.coursePostfixName = coursePostfixName;
        this.subject = ImmutableSet.copyOf(subject);
    }

    public int getNum() {
        return num;
    }

    public String getLongName() {
        return longName;
    }

    public String getCoursePostfixName() {
        return coursePostfixName;
    }

    /**
     * An detached immutable copy of the subjects in this semester. The subjects are not copied so any changes made to
     * the subjects contained in this list will affect the objects stored in this object.
     *
     * @return An immutable copy of the subjects in this semester.
     */
    public ImmutableSet<ImmutableSubject> getSubject() {
        return ImmutableSet.copyOf(subject);
    }

    @Override
    public String toString() {
        return "Semester{" + "num=" + num + ", longName=" + longName + ", coursePostfixName=" + coursePostfixName + ", subject=" + subject + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(num);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImmutableSemester other = (ImmutableSemester) obj;
        return Objects.equal(this.getNum(), other.getNum());
    }

    public static ImmutableSemester from(Semester s) {
        Set<ImmutableSubject> subjects = Sets.newHashSet();
        for (Subject subject : s.getSubject()) {
            subjects.add(ImmutableSubject.from(subject));
        }

        return new ImmutableSemester(s.getNum(), s.getLongName(), s.getCoursePostfixName(), subjects);
    }

    public void copyTo(Semester s) {
        s.setNum(this.getNum());
        s.setLongName(this.getLongName());
        s.setCoursePostfixName(this.getCoursePostfixName());
    }

    public Semester copyToNew() {
        Semester s = new Semester();
        copyTo(s);
        return s;
    }

}
