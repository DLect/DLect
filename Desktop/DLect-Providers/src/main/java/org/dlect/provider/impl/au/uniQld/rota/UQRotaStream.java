/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author lee
 */
public class UQRotaStream {

    private String seriesName;
    private String groupName;
    private final List<UQRotaStreamSession> session = Lists.newArrayList();

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<UQRotaStreamSession> getSession() {
        return ImmutableList.copyOf(session);
    }

    public void setSession(Collection<UQRotaStreamSession> c) {
        session.clear();
        session.addAll(c);
    }

    @Override
    public String toString() {
        return "UQRotaStream{" + "seriesName=" + seriesName + ", groupName=" + groupName + ", session=" + Joiner.on("\n\t").useForNull("null").join(session) + '}';
    }

}
