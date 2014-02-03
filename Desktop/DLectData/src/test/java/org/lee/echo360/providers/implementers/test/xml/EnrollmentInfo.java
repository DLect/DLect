/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.test.xml;

import java.util.SortedSet;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "mobileresponse")
public class EnrollmentInfo {

    @XmlAttribute(name = "status")
    public String status;
    @XmlAttribute(name = "coursesDisplayName")
    public String coursesDisplayName;
    @XmlAttribute(name = "orgsDisplayName")
    public String orgsDisplayName;
    @XmlAttribute(name = "defaultLocale")
    public String defaultLocale;
    @XmlAttribute(name = "assessments3.0")
    public boolean assessments3_0;
    @XmlElementWrapper(name = "courses")
    @XmlElements(
            @XmlElement(name = "course", type = SubjectInfo.class))
    public SortedSet<SubjectInfo> courses;

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        String nextIndent = indent + "\t  ";
        StringBuilder b = new StringBuilder(indent).append("EnrollmentInfo\n");
        b.append(indent).append("\tstatus=            ").append(status).append("\n");
        b.append(indent).append("\tcoursesDisplayName=").append(coursesDisplayName).append("\n");
        b.append(indent).append("\torgsDisplayName=   ").append(orgsDisplayName).append("\n");
        b.append(indent).append("\tdefaultLocale=     ").append(defaultLocale).append("\n");
        b.append(indent).append("\tassessments3_0=    ").append(assessments3_0).append("\n");
        b.append(indent).append("\tcourses=            Size:").append(courses.size());
        for (SubjectInfo s : courses) {
            b.append("\n").append(s.toString(nextIndent));
        }
        return b.toString();
    }
}
