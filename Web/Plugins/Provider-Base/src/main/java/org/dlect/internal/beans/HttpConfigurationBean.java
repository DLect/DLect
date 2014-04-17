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

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.enterprise.context.SessionScoped;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dlect.log.Beans;

/**
 *
 * This bean
 *
 * @author lee
 */
@SessionScoped
public class HttpConfigurationBean implements Serializable {

    private final CookieStore cookieStore = new BasicCookieStore();
    private transient CloseableHttpClient client;

    public String getAsString(HttpUriRequest request) throws IOException {
        HttpResponse ex = this.getClient().execute(request);
        String htmlString = CharStreams.toString(new InputStreamReader(ex.getEntity().getContent(), Charsets.UTF_8));
        HttpClientUtils.closeQuietly(ex);
        return htmlString;
    }

    @PostConstruct
    public void postConstruct() {
        Beans.LOG.error("BUILD");
        client = HttpClients.custom().setDefaultCookieStore(cookieStore).setUserAgent("DLectWeb/0.1 JavaEE/7 Apache-HttpClient/4.3").build();
    }

    @PostActivate
    public void postActivate() {
        Beans.LOG.error("ACTIVATE");
        postConstruct();
    }

    @PrePassivate
    public void prePassivate() {
        Beans.LOG.error("PASSIVATE");
        try {
            client.close();
            client = null;
        } catch (IOException ex) {
            Beans.LOG.error("Pre-destroy error.", ex);
        }
    }

    public HttpClient getClient() {
        if (client == null) {
            postActivate();
        }
        return client;
    }

    /**
     * This method removes all cookies, expired or not; effectively cleaning the
     * bean to be re-used elsewhere.
     */
    public void purgeCookieStore() {
        cookieStore.clear();
    }

    /**
     * This method removes all expired cookies from the current time.
     */
    public void cleanCookieStore() {
        cookieStore.clearExpired(new Date());
    }

}
