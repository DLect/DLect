/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.dlect.logging.ProviderLogger;

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

    private final Set<Date> dates = Sets.newHashSet();

    public UQRotaStreamSession() {
    }

    public UQRotaStreamSession(int startMins, int endMins, String room, String buildingNum, String buildingCampus, Collection<Date> dates) {
        this.startMins = startMins;
        this.endMins = endMins;
        this.room = room;
        this.buildingNum = buildingNum;
        this.buildingCampus = buildingCampus;
        this.dates.addAll(dates);
    }

    public void addDate(Date parse) {
        this.dates.add(parse);
    }

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
        return "UQRotaStreamSession{" + "startMins=" + startMins + ", endMins=" + endMins + ", room=" + room + ", buildingNum=" + buildingNum + ", buildingCampus=" + buildingCampus + ", dates=" + dates + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startMins, this.endMins, this.room, this.buildingNum, this.buildingCampus, this.dates);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            ProviderLogger.LOGGER.error("NULL");
            return false;
        }
        if (getClass() != obj.getClass()) {
            ProviderLogger.LOGGER.error("Class");
            return false;
        }
        final UQRotaStreamSession other = (UQRotaStreamSession) obj;
        if (this.startMins != other.startMins) {
            ProviderLogger.LOGGER.error(startMins + "startMins" + other.startMins);
            return false;
        }
        if (this.endMins != other.endMins) {
            ProviderLogger.LOGGER.error(endMins + "endMins" + other.endMins);
            return false;
        }
        if (!Objects.equals(this.room, other.room)) {
            ProviderLogger.LOGGER.error(room + "room" + other.room);
            return false;
        }
        if (!Objects.equals(this.buildingNum, other.buildingNum)) {
            ProviderLogger.LOGGER.error(buildingNum + "buildingNum" + other.buildingNum);
            return false;
        }
        if (!Objects.equals(this.buildingCampus, other.buildingCampus)) {
            ProviderLogger.LOGGER.error(buildingCampus + "buildingCampus" + other.buildingCampus);
            return false;
        }
        if (!Objects.equals(this.dates, other.dates)) {
            ProviderLogger.LOGGER.error(dates + "dates" + other.dates);
            return false;
        }
        return true;
    }

}
