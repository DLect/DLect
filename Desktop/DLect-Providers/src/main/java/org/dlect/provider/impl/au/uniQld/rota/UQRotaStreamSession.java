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
import java.util.Date;
import java.util.List;

/**
 *
 * @author lee
 */
public class UQRotaStreamSession {
    
    private int startMins;
    private int endMins;
    
    private String room;
    private String buildingNum;
    private String buildingCampus;
    
    private final List<Date> dates = Lists.newArrayList();
    
    public int getStartMins() {
        return startMins;
    }
    
    public void setStartMins(int startMins) {
        this.startMins = startMins;
    }
    
    public int getEndMins() {
        return endMins;
    }
    
    public void setEndMins(int endMins) {
        this.endMins = endMins;
    }
    
    public String getRoom() {
        return room;
    }
    
    public void setRoom(String room) {
        this.room = room;
    }
    
    public String getBuildingNum() {
        return buildingNum;
    }
    
    public void setBuildingNum(String buildingNum) {
        this.buildingNum = buildingNum;
    }
    
    public String getBuildingCampus() {
        return buildingCampus;
    }
    
    public void setBuildingCampus(String buildingCampus) {
        this.buildingCampus = buildingCampus;
    }
    
    public List<Date> getDates() {
        return ImmutableList.copyOf(dates);
    }
    
    public void setDates(Collection<Date> d) {
        dates.clear();
        dates.addAll(d);
    }
    
    @Override
    public String toString() {
        return "UQRotaStreamSession{" + "startMins=" + startMins + ", endMins=" + endMins + ", room=" + room + ", buildingNum=" + buildingNum + ", buildingCampus=" + buildingCampus + ", dates=" + Joiner.on("\n\t\t").useForNull("null").join(dates) + '}';
    }
    
}
