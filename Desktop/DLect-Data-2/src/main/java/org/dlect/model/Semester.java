/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import java.util.Set;
import javax.security.auth.Subject;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Semester {

    @XmlElement(name = "number")
    private int num;
    
    @XmlElement(name = "name")
    private String longName;
    
    @XmlElement(name = "prefix")
    private String coursePostfixName;
    
    @XmlElementWrapper(name= "subjects")
    @XmlElement(name="subject")
    private Set<Subject> subject;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getCoursePostfixName() {
        return coursePostfixName;
    }

    public void setCoursePostfixName(String coursePostfixName) {
        this.coursePostfixName = coursePostfixName;
    }

    public Set<Subject> getSubject() {
        return subject;
    }

    public void setSubject(Set<Subject> subject) {
        this.subject = subject;
    }

    
    
}
