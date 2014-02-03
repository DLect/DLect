/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import org.lee.echo360.util.SetUtils;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author lee
 */
public class Semester implements Comparable<Semester>, Serializable {

    protected static final long serialVersionUID = 6555706723595657456L;
    private final int num;
    private final String longName;
    private final String coursePostfixName;
    /**
     * USE WITH CAUTION - THIS IS A DIRECT COPY OF THE BB OBJECT'S SUBJECT.
     * SYNCHRONIZE ALWAYS.
     */
    private final Set<Subject> allSubjectsReference;

    public Semester(int num, String longName, String coursePostfixName, Set<Subject> allSubjects) {
        this.num = num;
        this.longName = longName;
        this.coursePostfixName = coursePostfixName;
        this.allSubjectsReference = allSubjects;

    }

    public String getCoursePostfixName() {
        return coursePostfixName;
    }

    public String getLongName() {
        return longName;
    }

    public int getCode() {
        return num;
    }

    @Override
    public String toString() {
        return "Semester{" + "num=" + num + ", longName=" + longName + ", coursePostfixName=" + coursePostfixName + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.num;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Semester other = (Semester) obj;
        if (this.num != other.num) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Semester o) {
        return (this.num < o.num) ? -1 : ((this.num == o.num) ? 0 : 1);
    }

    public Set<Subject> getSubjects() {
        synchronized (allSubjectsReference) {
            return ImmutableSet.copyOf(Collections2.filter(allSubjectsReference, new SemesterPredicate()));
        }
    }

    private class SemesterPredicate implements SetUtils.Predicate<Subject> {

        public SemesterPredicate() {
        }

        @Override
        public boolean apply(Subject input) {
            return input.getSemester().equals(Semester.this);
        }
    }
}
