/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.httpclient;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.dlect.helper.ApplicationInformation;
import org.dlect.logging.ProviderLogger;

public class BlackboardHttpClientImpl implements BlackboardHttpClient {

    private static final HttpRequestInterceptor DLECT_USER_AGENT_REQUEST_INTERCEPTOR = new DLectUserAgentRequestInterceptor();
    private static final HttpResponseInterceptor REDIRECT_MONITORING_RESPONSE_INTERCEPTOR = new RedirectMonitoringResponseInterceptor();

    private static final String LAST_REDIRECT_URL = "last_redirect_url";

    private final HttpClient cookieClient = HttpClients.custom()
            .addInterceptorLast(DLECT_USER_AGENT_REQUEST_INTERCEPTOR)
            .addInterceptorFirst(REDIRECT_MONITORING_RESPONSE_INTERCEPTOR)
            .build();
    private final HttpClient noCookieClient = HttpClients.custom()
            .addInterceptorLast(DLECT_USER_AGENT_REQUEST_INTERCEPTOR)
            .addInterceptorFirst(REDIRECT_MONITORING_RESPONSE_INTERCEPTOR)
            .setDefaultCookieStore(new NoOpCookieStore())
            .build();

    @Override
    public HttpResponceStream doGet(URI uri) throws IOException {
        return doRequest(cookieClient, new HttpGet(uri));
    }

    @Override
    public HttpResponceStream doGetWithoutCookies(URI uri) throws IOException {
        return doRequest(noCookieClient, new HttpGet(uri));
    }

    @Override
    public HttpResponceStream doPost(URI uri, Map<String, String> credentials) throws IOException {
        HttpPost p = new HttpPost(uri);

        List< NameValuePair> list = Lists.newArrayList();

        for (Entry<String, String> entry : credentials.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            list.add(new BasicNameValuePair(name, value));
        }

        p.setEntity(new UrlEncodedFormEntity(list, Charsets.UTF_8));
        return doRequest(cookieClient, p);
    }

    private HttpResponceStream doRequest(HttpClient client, HttpUriRequest httpGet) throws IOException {
        try {
            HttpContext c = new BasicHttpContext();
            HttpResponse r = client.execute(httpGet, c);

            Header[] h = r.getAllHeaders();

            URI t = getUriAfterRedirects(c);

//            ProviderLogger.LOGGER.error("Headers: " + r.getStatusLine() + ":: -> " + t);
//            
//            for (Header header : h) {
//                ProviderLogger.LOGGER.error(header.getName() + ": " + header.getValue());
//            }
            return new HttpResponceStream(new EntityInputStream(r.getEntity()), t);
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

    private URI getUriAfterRedirects(HttpContext context) throws URISyntaxException, MalformedURLException {
        String lastRedirectUri = (String) context.getAttribute(LAST_REDIRECT_URL);
        if (lastRedirectUri != null) {
            return new URI(lastRedirectUri);
        } else {
            HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST);
            HttpHost currentHost = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
            return new URL(new URL(currentHost.toURI()), currentReq.getURI().toString()).toURI();
        }
    }

    protected static class NoOpCookieStore implements CookieStore {

        public NoOpCookieStore() {
        }

        @Override
        public void addCookie(Cookie cookie) {
        }

        @Override
        public void clear() {
        }

        @Override
        public boolean clearExpired(Date date) {
            return false;
        }

        @Override
        public List<Cookie> getCookies() {
            return ImmutableList.of();
        }
    }

    protected static class DLectUserAgentRequestInterceptor implements HttpRequestInterceptor {

        public DLectUserAgentRequestInterceptor() {
        }

        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            request.setHeader("User-Agent", DLECT_IDENTIFIER);
            request.setHeader("X-DLect-Version", ApplicationInformation.APPLICATION_VERSION);
        }
    }

    private static class RedirectMonitoringResponseInterceptor implements HttpResponseInterceptor {

        public RedirectMonitoringResponseInterceptor() {
        }

        @Override
        public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
            if (response.containsHeader("Location")) {
                Header location = response.getFirstHeader("Location");
                ProviderLogger.LOGGER.error("Redirecting to: " + location);
                if (location != null) {
                    context.setAttribute(LAST_REDIRECT_URL, location.getValue());
                }
            }
        }
    }

}
