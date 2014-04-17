/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update;

import org.dlect.helper.ApplicationInformation;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class OnlineUpdateCheckerImpl implements OnlineUpdateChecker {

    private static final URI UPDATE_CHECK_URI = URI.create("http://uqlectures.sourceforge.net/update.php");
    private static final String UPDATE_REQUIRED_HEADER = "X-Update-Required";

    private HttpEntity generateEntity(String selectedProvider, String uuid, String bbid) {
        List<BasicNameValuePair> postVars = Lists.newArrayList(
                of("thisver", ApplicationInformation.APPLICATION_VERSION),
                of("javaver", ApplicationInformation.JAVA_VERSION),
                of("bbid", bbid),
                of("uuid", uuid),
                of("provider", selectedProvider),
                of("osname", ApplicationInformation.OS_TYPE),
                of("osversion", ApplicationInformation.OS_VERSON),
                of("osarch", ApplicationInformation.OS_ARCHITECTURE)
        );
        UpdateLogger.LOGGER.error("Posting: " + postVars);
        return new UrlEncodedFormEntity(postVars, Charsets.UTF_8);
    }

    private BasicNameValuePair of(String name, String value) {
        return new BasicNameValuePair(name, value);
    }

    @Override
    public boolean isUpdateAvaliable(String selectedProvider, String uuid, String bbid) throws UpdateException {
        try (CloseableHttpClient hc = HttpClients.createDefault()) {
            HttpPost r = new HttpPost(UPDATE_CHECK_URI);
            r.setEntity(generateEntity(selectedProvider, uuid, bbid));

            CloseableHttpResponse response = hc.execute(r);

            UpdateLogger.LOGGER.error("{}", response.getStatusLine());

            UpdateLogger.LOGGER.error("{}", CharStreams.toString(new InputStreamReader(response.getEntity().getContent(), Charsets.UTF_8)));

            Header updateNeeded = response.getLastHeader(UPDATE_REQUIRED_HEADER);

            if (updateNeeded != null) {
                boolean value = parseBooleanSafe(updateNeeded.getValue(), false);
                return value;
            } else {
                UpdateLogger.LOGGER.error("No update header. Headders follow");
                for (Header header : response.getAllHeaders()) {
                    UpdateLogger.LOGGER.error("H > {}", header);
                }
                return false;
            }
        } catch (IOException e) {
            throw new UpdateException(e);
        }
    }

    private boolean parseBooleanSafe(String value, boolean b) {
        try {
            return Boolean.parseBoolean(value);
        } catch (NullPointerException | IllegalArgumentException e) {
            return b;
        }

    }

}
