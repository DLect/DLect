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
public class SubjectData implements Comparable<SubjectData> {

    @XmlAttribute(name = "status")
    public String status;
    @XmlAttribute(name = "rooturl")
    public String rooturl;
    @XmlElementWrapper(name = "map")
    @XmlElements(
            @XmlElement(name = "map-item", type = LectureData.class))
    public SortedSet<LectureData> lectures;

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        String nextIndent = indent + "\t  ";
        StringBuilder b = new StringBuilder(indent).append("EnrollmentInfo\n");
        b.append(indent).append("\tstatus=  ").append(status).append("\n");
        b.append(indent).append("\trooturl= ").append(rooturl).append("\n");
        b.append(indent).append("\tlectures=Size:").append(lectures.size());
        for (LectureData l : lectures) {
            b.append("\n").append(l.toString(nextIndent));
        }
        return b.toString();
    }

    @Override
    public int compareTo(SubjectData o) {
        if (this.equals(o)) {
            return 0;
        } else {
            return this.hashCode() - o.hashCode(); // GHAAA
        }
    }
}
