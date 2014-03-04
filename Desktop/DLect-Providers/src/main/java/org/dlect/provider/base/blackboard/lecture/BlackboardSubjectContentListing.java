/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "mobileresponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlackboardSubjectContentListing {

    @XmlElementWrapper(name = "map")
    @XmlElement(name = "map-item")
    private List<BlackboardSubjectMapItem> mapItems;

    @XmlAttribute(name = "rooturl")
    private String rootUrl;

    public ImmutableList<BlackboardSubjectMapItem> getMapItems() {
        return ImmutableList.copyOf(mapItems);
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public ImmutableList<BlackboardSubjectMapItem> getAllItems() {
        ArrayList<BlackboardSubjectMapItem> toExplore = Lists.newArrayList(mapItems);
        ArrayList<BlackboardSubjectMapItem> encountered = Lists.newArrayList(mapItems);

        while (!toExplore.isEmpty()) {
            BlackboardSubjectMapItem item = toExplore.remove(0);
            toExplore.addAll(item.getChildren());
            encountered.addAll(item.getChildren());
        }

        return ImmutableList.copyOf(encountered);
    }

}
