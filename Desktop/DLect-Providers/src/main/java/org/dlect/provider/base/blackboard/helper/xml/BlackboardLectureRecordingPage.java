/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.xml;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BlackboardLectureRecordingPage {

    private final ImmutableList<BlackboardLectureRecordingItem> items;

    public BlackboardLectureRecordingPage(Collection<BlackboardLectureRecordingItem> items) {
        this.items = ImmutableList.copyOf(items);
    }

    public ImmutableList<BlackboardLectureRecordingItem> getItems() {
        return ImmutableList.copyOf(items);
    }

}
