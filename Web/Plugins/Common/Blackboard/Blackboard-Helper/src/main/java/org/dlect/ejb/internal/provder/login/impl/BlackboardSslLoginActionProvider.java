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
package org.dlect.ejb.internal.provder.login.impl;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dlect.ejb.internal.provder.login.LoginActionProvider;
import org.dlect.provider.common.blackboard.xml.BlackboardLoginXml;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.export.University;
import org.dlect.helpers.JAXBHelper;
import org.dlect.internal.beans.HttpConfigurationBean;
import org.dlect.object.ResultType;

import static org.dlect.except.DLectExceptionBuilder.builder;

// TODO change this to a singleton.
@SessionScoped
public class BlackboardSslLoginActionProvider implements LoginActionProvider {

    private static final long serialVersionUID = 1L;

    @Inject
    private HttpConfigurationBean httpConfig;

    @Override
    public boolean doLoginFor(University u, String name, String pass) throws DLectException {
        HttpResponse r = null;
        try {
            final URL url = new URL(new URL(u.getUrl()), "sslUserLogin?v=1&language=en_GB&ver=3.1.2");
            HttpPost post = new HttpPost(url.toURI());

            HttpEntity entity = new UrlEncodedFormEntity(Lists.newArrayList(
                    new BasicNameValuePair("username", name),
                    new BasicNameValuePair("password", pass)
            ));

            post.setEntity(entity);

            r = httpConfig.getClient().execute(post);

            boolean ret = readLogin(r.getEntity().getContent());

            if (!ret) {
                throw CommonExceptionBuilder.getOnFailContractBreachException("Read Login for university: " + u);
            }

            return ret;
        } catch (IOException | URISyntaxException e) {
            throw builder().setCause(e).setResult(ResultType.UNIVERSITY_ERROR).setErrorMessages("Failed to access university").build();
        } finally {
            if (r != null) {
                EntityUtils.consumeQuietly(r.getEntity());
            }
        }
    }

    private boolean readLogin(InputStream s) throws DLectException {
        try {
            BlackboardLoginXml login = JAXBHelper.unmarshalObjectFromStream(s, BlackboardLoginXml.class);
            if (login.getStatus() == BlackboardLoginXml.LoginStatus.OK) {
                return true;
            } else {
                throw builder().setResult(ResultType.BAD_CREDENTIALS).addErrorMessages("Login did not indicate success.").build();
            }
        } catch (JAXBException | IOException e) {
            throw builder().setCause(e).setResult(ResultType.UNIVERSITY_ERROR).addErrorMessages("Unsupported responce from university.").build();
        }
    }

}
