/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lee.echo360.providers.mobile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.ProviderInformation;
import org.lee.echo360.util.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DefaultMobileXmlProviderImpl implements MobileXmlProvider {

    public DefaultMobileXmlProviderImpl(ProviderInformation pInfo, HttpClient client, boolean synchronize) {
        if (pInfo == null) {
            throw new IllegalArgumentException("The MobileProvider was null");
        } else if (client == null) {
            throw new IllegalArgumentException("The HttpClient was null");
        } else if (pInfo.isUsingHttpAuth()) {
            throw new IllegalArgumentException("This implementation does not support HTTP Auth.");
        } else if (!pInfo.isUsingSSL()) {
            throw new IllegalArgumentException("This implementation only supports login with SSL.");
        } else if (pInfo.getB2Url() == null || !pInfo.getB2Url().startsWith("https://")) {
            throw new IllegalArgumentException("This implementation only supports login with SSL. Or The B2URL is missing");
        }
        try {
            B2_URL = new URL(pInfo.getB2Url());
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("B2 URL Is invalid and caused a Malformed URL Exception", ex);
        }
        HTTP_CLIENT = client;
        this.synchronize = synchronize;
    }
    private URL B2_URL;
    private final HttpClient HTTP_CLIENT;
    private final boolean synchronize;

    @Override
    public Document getLoginResponce(Blackboard b) throws IOException, SAXException, ParserConfigurationException {
        final URL url = new URL(B2_URL, "sslUserLogin?v=1&language=en_GB&ver=3.1.2");
        HttpPost post = new HttpPost(url.toString());
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("username", b.getUsername()));
        formparams.add(new BasicNameValuePair("password", b.getPassword()));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        post.setEntity(entity);
        HttpResponse r = execute(post);
        try {
            return XMLUtil.getDocumentFromStream(r.getEntity().getContent());
        } finally {
            EntityUtils.consumeQuietly(r.getEntity());
        }
    }

    @Override
    public Document getSubjects() throws IOException, SAXException, ParserConfigurationException {
        final URL url = new URL(B2_URL, "enrollments?course_type=COURSE&include_grades=false&v=1&language=en_GB&ver=3.1.2");
        HttpResponse r = execute(new HttpGet(url.toString()));
        try {
            return XMLUtil.getDocumentFromStream(r.getEntity().getContent());
        } finally {
            EntityUtils.consumeQuietly(r.getEntity());
        }
    }

    @Override
    public String getLectureContentString(String subjCode, String contentID) throws IOException {
        String rep;
        final String file = "contentDetail?course_id=" + subjCode + "&content_id=" + contentID + "&v=1&language=en_GB&ver=3.1.2";
        final URL uri = new URL(B2_URL, file);
        HttpResponse r = execute(new HttpGet(uri.toString()));
        ByteArrayOutputStream os = new ByteArrayOutputStream(Math.max(10240, (int) r.getEntity().getContentLength()));
        r.getEntity().writeTo(os);
        EntityUtils.consumeQuietly(r.getEntity());
        rep = os.toString();
        os.reset();
        return rep;
    }

    @Override
    public Document getLectureXML(final String code) throws ParserConfigurationException, IllegalStateException, IOException, SAXException {
        Document doc;
        URL map = new URL(B2_URL, "courseMap?course_id=" + code + "&v=1&language=en_GB&ver=3.1.2");
        HttpGet get = new HttpGet(map.toString());
        HttpResponse r = execute(get);
        doc = XMLUtil.getDocumentFromStream(r.getEntity().getContent());
        EntityUtils.consumeQuietly(r.getEntity());
        return doc;
    }

    @Override
    public HttpClient getClient() {
        return HTTP_CLIENT;
    }

    @Override
    public URL getRootURL(String thisRootUrl) throws MalformedURLException {
        return (thisRootUrl == null ? B2_URL : new URL(thisRootUrl));
    }

    private HttpResponse execute(HttpUriRequest request) throws IOException {
        if (synchronize) {
            synchronized (HTTP_CLIENT) {
                return HTTP_CLIENT.execute(request);
            }
        } else {
            return HTTP_CLIENT.execute(request);
        }
    }
}
