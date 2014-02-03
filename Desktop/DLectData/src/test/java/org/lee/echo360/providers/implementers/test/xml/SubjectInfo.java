/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.test.xml;

import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lee
 */
public class SubjectInfo implements  Comparable<SubjectInfo>{

    @XmlAttribute(name = "bbid")
    public String bbid;
    @XmlAttribute(name = "name")
    public String name;
    @XmlAttribute(name = "courseid")
    public String courseid;
    @XmlAttribute(name = "role")
    public String role;
    @XmlAttribute(name = "isAvail")
    public boolean isAvail;
    @XmlAttribute(name = "locale")
    public String locale;
    @XmlAttribute(name = "enrollmentdate")
    public Date enrollmentdate;
    @XmlAttribute(name = "roleIdentifier")
    public String roleIdentifier;
    @XmlTransient
    public SubjectData data;

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        StringBuilder b = new StringBuilder(indent).append("SubjectInfo\n");
        b.append(indent).append("\tbbid=          ").append(bbid).append("\n");
        b.append(indent).append("\tname=          ").append(name).append("\n");
        b.append(indent).append("\tcourseid=      ").append(courseid).append("\n");
        b.append(indent).append("\trole=          ").append(role).append("\n");
        b.append(indent).append("\tisAvail=       ").append(isAvail).append("\n");
        b.append(indent).append("\tlocale=        ").append(locale).append("\n");
        b.append(indent).append("\tenrollmentdate=").append(enrollmentdate).append("\n");
        b.append(indent).append("\troleIdentifier=").append(roleIdentifier);
        return b.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.bbid != null ? this.bbid.hashCode() : 0);
        hash = 41 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 41 * hash + (this.courseid != null ? this.courseid.hashCode() : 0);
        hash = 41 * hash + (this.role != null ? this.role.hashCode() : 0);
        hash = 41 * hash + (this.isAvail ? 1 : 0);
        hash = 41 * hash + (this.locale != null ? this.locale.hashCode() : 0);
        hash = 41 * hash + (this.enrollmentdate != null ? this.enrollmentdate.hashCode() : 0);
        hash = 41 * hash + (this.roleIdentifier != null ? this.roleIdentifier.hashCode() : 0);
        hash = 41 * hash + (this.data != null ? this.data.hashCode() : 0);
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
        final SubjectInfo other = (SubjectInfo) obj;
        if ((this.bbid == null) ? (other.bbid != null) : !this.bbid.equals(other.bbid)) {
            return false;
        }
        if ((this.courseid == null) ? (other.courseid != null) : !this.courseid.equals(other.courseid)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(SubjectInfo o) {
        return this.enrollmentdate.compareTo(o.enrollmentdate);
    }
}
