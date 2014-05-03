/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin.impl.echo;

import com.google.common.base.Objects;
import java.util.Date;
import org.dlect.model.helper.ImmutableDate;

import static org.dlect.model.helper.ImmutableDate.of;

/**
 *
 * @author lee
 */
public class EchoCenterLecture {

    private final String id;
    private final String title;
    private final Date startTime;
    private final String baseUrl;

    public EchoCenterLecture(String id, String title, Date startTime, String baseUrl) {
        this.id = id;
        this.title = title;
        this.startTime = ImmutableDate.of(startTime);
        this.baseUrl = baseUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ImmutableDate getStartTime() {
        return of(startTime);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public String toString() {
        return "EchoCenterLecture{" + "id=" + id + ", title=" + title + ", startTime=" + startTime + ", baseUrl=" + baseUrl + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.title, this.startTime, this.baseUrl);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EchoCenterLecture other = (EchoCenterLecture) obj;
        return Objects.equal(this.id, other.id) && Objects.equal(this.title, other.title)
               && Objects.equal(this.startTime, other.startTime) && Objects.equal(this.baseUrl, other.baseUrl);
    }

}
