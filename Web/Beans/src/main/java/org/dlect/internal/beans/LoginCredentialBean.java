/*
 * This file is part of DLect. DLect is a suite of code that facilitates the downloading of lecture recordings.
 *
 * Copyright © 2014 Lee Symes.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dlect.internal.beans;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.enterprise.context.SessionScoped;
import org.dlect.ejb.internal.provder.UniversityActionProvider;
import org.dlect.export.University;
import org.dlect.helpers.DataHelpers;
import org.dlect.internal.ProviderKey;
import org.dlect.internal.data.SubjectData;

/**
 *
 * @author lee
 */
@SessionScoped
public class LoginCredentialBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private University university;
    private String username;
    private String password;
    private long lastRequestMade;
    private UniversityActionProvider provider;

    private final Map<ProviderKey<?>, Serializable> extraData = Maps.newHashMap();

    private List<SubjectData> listing;

    public List<SubjectData> getListing() {
        return DataHelpers.wrap(listing);
    }

    public void setListing(List<SubjectData> listing) {
        this.listing = DataHelpers.copy(listing);
    }

    public boolean isListingValid() {
        return listing != null && !listing.isEmpty();
    }

    public void updateFrom(UniversityActionProvider providerUsed, University university, String username, String password) {
        this.university = university;
        this.username = username;
        this.password = password;
        this.provider = providerUsed;
        this.listing = Lists.newArrayList();
        this.resetRequestTime();
    }

    public <T extends Serializable> void setProviderData(ProviderKey<T> key, T value) {
        synchronized (extraData) {
            extraData.put(key, value);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getProviderData(ProviderKey<T> key) {
        synchronized (extraData) {
            return (T) extraData.get(key);
        }
    }

    public void resetRequestTime() {
        this.lastRequestMade = System.currentTimeMillis();
    }

    public University getUniversity() {
        return university;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getLastRequestMade() {
        return lastRequestMade;
    }

    public UniversityActionProvider getProvider() {
        return provider;
    }

    public void setProvider(UniversityActionProvider provider) {
        this.provider = provider;
    }

    public boolean isValid() {
        return university != null && username != null && password != null;
    }

    public boolean sameAs(University u, String username, String password) {
        return isValid() && Objects.equals(u, this.university) && Objects.equals(username, this.username) && Objects.equals(password, this.password);
    }

    public boolean sameAs(String u, String username, String password) {
        return isValid()
               && this.university != null
               && Objects.equals(this.university.getCode(), u)
               && Objects.equals(username, this.username)
               && Objects.equals(password, this.password);
    }

    public void clear() {
        this.university = null;
        this.username = null;
        this.password = null;
        this.lastRequestMade = 0;
        this.provider = null;
        this.extraData.clear();
    }

    @Override
    public String toString() {
        return "LoginCredentialBean{" + super.toString() + "; university=" + university + ", username=" + username + ", password=" + password + ", lastRequestMade=" + lastRequestMade + '}';
    }

}
