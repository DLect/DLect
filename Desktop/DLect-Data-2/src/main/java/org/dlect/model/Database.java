/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.events.EventID;
import org.dlect.model.helper.XmlListenable;

@XmlRootElement(name = "dlect")
@XmlAccessorType(XmlAccessType.FIELD)
public class Database extends XmlListenable<Database> {

    @XmlElementWrapper(name = "semesters")
    @XmlElement(name = "semester")
    private Set<Semester> semesters;

    @XmlElementWrapper(name = "settings")
    @XmlElement(name = "setting")
    private Map<String, String> settings;

    public Database() {
        this.semesters = newWrappedListenableSet(DatabaseEventID.SEMESTER);
        this.settings = newWrappedMap(DatabaseEventID.SETTING);
    }

    public ImmutableSet<Semester> getSemesters() {
        return copyOf(semesters);
    }

    public void setSemesters(Set<Semester> semesters) {
        setSet(this.semesters, semesters);
    }

    public void addSemester(Semester s) {
        this.semesters.add(s);
    }

    public void removeSemester(Semester s) {
        this.semesters.remove(s);
    }

    public ImmutableMap<String, String> getSettings() {
        return copyOf(settings);
    }

    public void addSetting(String key, String value) {
        this.settings.put(key, value);
    }

    public String removeSetting(String key) {
        return this.settings.remove(key);
    }

    public String getSetting(String key) {
        return this.settings.get(key);
    }

    public void setSettings(Map<String, String> settings) {
        setMap(this.settings, settings);
    }

    @Override
    public String toString() {
        return "Database{" + "semesters=" + semesters + ", settings=" + settings + '}';
    }


    public  static enum DatabaseEventID implements EventID {

        SEMESTER,
        SETTING;

        @Override
        public Class<?> getAppliedClass() {
            return Database.class;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public boolean isUniqueId() {
            return false;
        }

    }

}