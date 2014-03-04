/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture;

import com.google.common.collect.ImmutableList;
import java.util.Collection;

/**
 *
 * @author lee
 */
public class BlackboardLectureRecordingPage {

    private final ImmutableList<BlackboardLectureRecordingItem> items;

    public BlackboardLectureRecordingPage(Collection<BlackboardLectureRecordingItem> items) {
        this.items = ImmutableList.copyOf(items);
    }

    public ImmutableList<BlackboardLectureRecordingItem> getItems() {
        return ImmutableList.copyOf(items);
    }

}
