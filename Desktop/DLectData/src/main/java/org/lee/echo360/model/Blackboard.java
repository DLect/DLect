/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class Blackboard implements Serializable {
    
    static final long serialVersionUID = 0x20def5;
    public static final String PROP_USERNAME = "PROP_USERNAME";
    public static final String PROP_PASSWORD = "PROP_PASSWORD";
    public static final String PROP_SUBJECT_ADD = "PROP_SUBJECT_ADD";
    public static final String PROP_SUBJECT_CLEAR = "PROP_SUBJECT_CLEAR";
    public static final String PROP_SEMESTER_ADD = "PROP_SEMESTER_ADD";
    public static final String PROP_SEMESTER_CLEAR = "PROP_SEMESTER_CLEAR";
    private String username;
    private String password;
    private final TreeSet<Subject> subjects;
    private final TreeSet<Semester> semesters;
    private transient PropertyChangeSupport pcs;
    private final long instanceID;

    public Blackboard() {
        subjects = new TreeSet<Subject>();
        semesters = new TreeSet<Semester>();
        this.instanceID = Double.doubleToLongBits(System.currentTimeMillis() * Math.random() * Math.random());
    }

    private void initPCS() {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);
        }
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        initPCS();
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        initPCS();
        pcs.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        initPCS();
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        initPCS();
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public String getPassword() {
        return (password == null ? "" : password);
    }

    public void addSubject(Subject c) {
        if (c != null) {
            initPCS();
            // TODO change this 
            synchronized (subjects) {
                if (!subjects.contains(c)) {
                    subjects.add(c);
                    addSemester(c.getSemester());
                    pcs.firePropertyChange(PROP_SUBJECT_ADD, null, c);
                }
            }
        }
    }

    public Semester getSemester(int num, String longName, String coursePostfixName) {
        synchronized (semesters) {
            for (Semester s : semesters) {
                if (s.getCode() == num && s.getLongName().equals(longName) && s.getCoursePostfixName().equals(coursePostfixName)) {
                    return s;
                }
            }
            Semester s = new Semester(num, longName, coursePostfixName, subjects);
            if (!semesters.add(s)) {
                ExceptionReporter.reportException("Semester Set in Blackboard in inconsitent state. Tried Adding: " + s);
            }
            return s;
        }
    }

    private void addSemester(Semester s) {
        synchronized (semesters) {
            if (s != null) {
                initPCS();
                if (!semesters.contains(s)) {
                    semesters.add(s);
                    pcs.firePropertyChange(PROP_SEMESTER_ADD, null, s);
                }
            }
        }
    }

    public void clearSubjects() {
        initPCS();
        synchronized (subjects) {
            subjects.clear();
        }
        synchronized (semesters) {
            semesters.clear();
        }
        pcs.firePropertyChange(PROP_SUBJECT_CLEAR, null, null);
        pcs.firePropertyChange(PROP_SEMESTER_CLEAR, null, null);
    }

    public SortedSet<Subject> getSubjects() {
        synchronized (subjects) {
            return ImmutableSortedSet.copyOf(subjects);
        }
    }

    public SortedSet<Semester> getSemesters() {
        synchronized (semesters) {
            return ImmutableSortedSet.copyOf(semesters);
        }
    }

    public void setUsername(String username) {
        if (username != null) {
            initPCS();
            String old = this.username;
            this.username = username;
            pcs.firePropertyChange(PROP_USERNAME, old, this.username);
        }
    }

    public void setPassword(String password) {
        if (password != null) {
            initPCS();
            String old = this.password;
            this.password = password;
            pcs.firePropertyChange(PROP_PASSWORD, old, this.username);
        }
    }

    @Override
    public String toString() {
        return "Blackboard{" + "username=" + getUsername() + ", password=***PASSWORD-OBSCURED***, subject=" + getSubjects() + '}';
    }

    public long getBlackboardID() {
        return instanceID;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return (username == null ? "" : username);
    }

    public boolean hasCredentials() {
        return !getUsername().isEmpty() && !getPassword().isEmpty();
    }
}
