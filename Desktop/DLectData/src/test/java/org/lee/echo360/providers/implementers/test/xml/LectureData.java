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
public class LectureData implements Comparable<LectureData> {

    @XmlAttribute(name = "contentid")
    public String contentid;
    @XmlAttribute(name = "name")
    public String name;
    @XmlAttribute(name = "viewurl")
    public String viewurl;
    @XmlAttribute(name = "isAvail")
    public boolean isAvail;
    @XmlAttribute(name = "linktype")
    public String linktype;
    @XmlAttribute(name = "isfolder")
    public boolean isfolder;
    @XmlAttribute(name = "datemodified")
    public Date datemodified;
    @XmlTransient()
    public LectureContent lc;

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        StringBuilder b = new StringBuilder(indent).append("LectureData\n");
        b.append(indent).append("\tcontentid=  ").append(contentid).append("\n");
        b.append(indent).append("\tname= ").append(name).append("\n");
        b.append(indent).append("\tviewurl=  ").append(viewurl).append("\n");
        b.append(indent).append("\tisAvail= ").append(isAvail).append("\n");
        b.append(indent).append("\tlinktype=  ").append(linktype).append("\n");
        b.append(indent).append("\tisfolder= ").append(isfolder).append("\n");
        b.append(indent).append("\tdatemodified=  ").append(datemodified).append("\n");
        return b.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.contentid != null ? this.contentid.hashCode() : 0);
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (this.viewurl != null ? this.viewurl.hashCode() : 0);
        hash = 29 * hash + (this.isAvail ? 1 : 0);
        hash = 29 * hash + (this.linktype != null ? this.linktype.hashCode() : 0);
        hash = 29 * hash + (this.isfolder ? 1 : 0);
        hash = 29 * hash + (this.datemodified != null ? this.datemodified.hashCode() : 0);
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
        final LectureData other = (LectureData) obj;
        if (this.datemodified != other.datemodified && (this.datemodified == null || !this.datemodified.equals(other.datemodified))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(LectureData o) {
        return this.lc.content.body.captureTime.compareTo(o.lc.content.body.captureTime);
    }
}
