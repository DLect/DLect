/*
 * This file is part of DLect. DLect is a suite of code that facilitates the downloading of lecture recordings.
 *
 * Copyright © 2014 Lee Symes.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dlect.provider.common.blackboard.xml;

import org.dlect.provider.common.blackboard.xml.adapters.BlackboardXmlDateTypeAdapter;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import static org.dlect.helpers.DataHelpers.*;

/**
 *
 * @author lee
 */@XmlAccessorType(XmlAccessType.FIELD)
public class BlackboardCourseMapItem {

    @XmlAttribute(name = "contentid")
    private String contentid;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "viewurl")
    private String viewurl;
    @XmlAttribute(name = "isAvail")
    private boolean isAvail;
    @XmlAttribute(name = "linktype")
    private String linktype;
    @XmlAttribute(name = "linktarget")
    private String linktarget;
    @XmlAttribute(name = "datemodified")
    @XmlJavaTypeAdapter(BlackboardXmlDateTypeAdapter.class)
    private Date datemodified;

    @XmlElementWrapper(name = "children")
    @XmlElement(name = "map-item")
    private List<BlackboardCourseMapItem> children;

    public List<BlackboardCourseMapItem> getChildren() {
        return wrap(children);
    }

    public void setChildren(List<BlackboardCourseMapItem> children) {
        this.children = copyReplaceNull(children);
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public Date getDatemodified() {
        return copy(datemodified);
    }

    public void setDatemodified(Date datemodified) {
        this.datemodified = copy(datemodified);
    }

    public String getLinktarget() {
        return linktarget;
    }

    public void setLinktarget(String linktarget) {
        this.linktarget = linktarget;
    }

    public String getLinktype() {
        return linktype;
    }

    public void setLinktype(String linktype) {
        this.linktype = linktype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewurl() {
        return viewurl;
    }

    public void setViewurl(String viewurl) {
        this.viewurl = viewurl;
    }

    public boolean isAvail() {
        return isAvail;
    }

    public void setAvail(boolean isAvail) {
        this.isAvail = isAvail;
    }

    @Override
    public String toString() {
        return "BlackboardCourseMapItem{" + "contentid=" + contentid + ", name=" + name + ", viewurl=" + viewurl + ", isAvail=" + isAvail + ", linktype=" + linktype + ", linktarget=" + linktarget + ", datemodified=" + datemodified + ", children=" + children + '}';
    }

}
