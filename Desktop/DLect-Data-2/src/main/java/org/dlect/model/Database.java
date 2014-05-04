/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.dlect.events.EventID;
import org.dlect.helper.ApplicationInformation;
import org.dlect.model.helper.XmlListenable;

@XmlRootElement(name = "dlect")
@XmlAccessorType(XmlAccessType.FIELD)
public class Database extends XmlListenable<Database> {

    @XmlAttribute(name = "desktop-version")
    private String versionString = ApplicationInformation.APPLICATION_VERSION;

    @XmlElementWrapper(name = "semesters")
    @XmlElement(name = "semester")
    private final SortedSet<Semester> semesters;

    @XmlElementWrapper(name = "settings")
    @XmlElement(name = "setting")
    private final Map<String, String> settings;

    public Database() {
        this.semesters = newWrappedListenableSortedSet(DatabaseEventID.SEMESTER);
        this.settings = newWrappedMap(DatabaseEventID.SETTING);
    }

    public ImmutableSortedSet<Semester> getSemesters() {
        return copyOf(semesters);
    }

    public void setSemesters(Collection<Semester> semesters) {
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

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    @Override
    public String toString() {
        return "Database{" + "version=" + versionString + "semesters=" + getSemesters() + '}';
    }

    public static enum DatabaseEventID implements EventID {

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

    }

}
