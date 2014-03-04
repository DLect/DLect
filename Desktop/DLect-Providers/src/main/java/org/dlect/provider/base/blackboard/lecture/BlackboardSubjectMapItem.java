/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author lee
 */
public class BlackboardSubjectMapItem {

    @XmlElement(name = "contentid")
    private String contentId;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "viewurl")
    private String viewUrl;

    @XmlElement(name = "linktype")
    private String linkType;

    @XmlElement(name = "isAvail")
    private boolean availiable;

    @XmlElementWrapper(name = "children")
    @XmlElement(name = "map-item")
    private List<BlackboardSubjectMapItem> children;

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

}
