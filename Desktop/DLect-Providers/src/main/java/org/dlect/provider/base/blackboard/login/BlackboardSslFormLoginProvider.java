/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.login;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.provider.BlackboardProviderDetails;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlParser;

/**
 *
 * @author lee
 */
public class BlackboardSslFormLoginProvider extends BlackboardLoginProvider {

    private final BlackboardHttpClient httpClient;
    private final BlackboardProviderDetails u;

    public BlackboardSslFormLoginProvider(BlackboardProviderDetails u, BlackboardHttpClient httpClient, BlackboardXmlParser xmlParser) {
        super(xmlParser);
        this.u = u;
        this.httpClient = httpClient;
    }

    @Override
    public InputStream doProviderLogin(String username, String password) throws IOException {
        try {
            ImmutableMap<String, String> credentials = ImmutableMap.of("username", username, "password", password);

            URI loginUrl = new URL(u.getBaseUrl().toURL(), "sslUserLogin?v=1&language=en_GB&ver=3.1.2").toURI();

            return httpClient.doPost(loginUrl, credentials);
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

}
