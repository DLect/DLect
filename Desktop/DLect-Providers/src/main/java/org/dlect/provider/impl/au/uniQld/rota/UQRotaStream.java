/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author lee
 */
public class UQRotaStream {

    private String seriesName;
    private String groupName;
    private final Set<UQRotaStreamSession> session = Sets.newHashSet();

    public UQRotaStream() {
    }

    public UQRotaStream(String seriesName, String groupName, Collection<UQRotaStreamSession> session) {
        this.seriesName = seriesName;
        this.groupName = groupName;
        this.session.addAll(session);
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupNumber(String groupName) {
        this.groupName = groupName;
    }

    public Set<UQRotaStreamSession> getSession() {
        return ImmutableSet.copyOf(session);
    }

    public void setSession(Collection<UQRotaStreamSession> c) {
        session.clear();
        session.addAll(c);
    }

    @Override
    public String toString() {
        return "\nUQRotaStream{" + "seriesName=" + seriesName + ", groupName=" + groupName + ", session=" + session + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.seriesName, this.groupName, this.session);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UQRotaStream other = (UQRotaStream) obj;
        if (!Objects.equals(this.seriesName, other.getSeriesName())) {
            return false;
        }
        if (!Objects.equals(this.groupName, other.getGroupName())) {
            return false;
        }
        return Objects.equals(this.session, other.getSession());
    }

}
