/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.xml;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author lee
 */
public class BlackboardSubjectMapItem {

    @XmlAttribute(name = "contentid")
    private String contentId;

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "viewurl")
    private String viewUrl;

    @XmlAttribute(name = "linktype")
    private String linkType;

    @XmlAttribute(name = "isAvail")
    private boolean availiable;

    @XmlElementWrapper(name = "children")
    @XmlElement(name = "map-item")
    private final List<BlackboardSubjectMapItem> children = Lists.newArrayList();

    public String getContentId() {
        return contentId;
    }

    public String getName() {
        return name;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public String getLinkType() {
        return linkType;
    }

    public boolean isAvaliable() {
        return availiable;
    }

    public ImmutableList<BlackboardSubjectMapItem> getChildren() {
        return ImmutableList.copyOf(children);
    }

    @Override
    public String toString() {
        return "BlackboardSubjectMapItem{" + "contentId=" + contentId + ", name=" + name + ", viewUrl=" + viewUrl + ", linkType=" + linkType + ", availiable=" + availiable + ", children=" + children + '}';
    }

    

}
