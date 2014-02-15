/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import java.util.SortedSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.dlect.events.EventID;
import org.dlect.events.listenable.Listenable;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Subject extends Listenable<Subject> implements Comparable<Subject> {

    @XmlElement(name = "bbid")
    private String bbid;

    @XmlElement(name = "courseID")
    private String courseID;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "folderName")
    private String folderName;

    @XmlElementWrapper(name = "lectures")
    @XmlElement(name = "lecture")
    private SortedSet<Lecture> lectures;

    @XmlElementWrapper(name = "streams")
    @XmlElement(name = "stream")
    private SortedSet<Stream> streams;

    public Subject() {
        lectures = newWrappedListenableSortedSet(SubjectEventID.LECTURE);
        streams = newWrappedListenableSortedSet(SubjectEventID.STREAM);
    }

    @Override
    public int compareTo(Subject o) {
        if (o == null) {
            // I am always greater than a null value.
            return 1; 
        }
        return name.compareTo(o.name);
    }

    public static enum SubjectEventID implements EventID {

        BBID, COURSE_ID, NAME, FOLDER_NAME, LECTURE, STREAM;

        @Override
        public Class<?> getAppliedClass() {
            return Subject.class;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public boolean isUniqueId() {
            return this == BBID;
        }

    }

}
