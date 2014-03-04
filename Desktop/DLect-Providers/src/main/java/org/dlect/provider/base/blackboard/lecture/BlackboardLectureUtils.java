/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author lee
 */
public class BlackboardLectureUtils {

    public static URI normaliseUri(URI baseUri, String viewUrl) throws IOException {
        try {
            URI u = new URL(baseUri.toURL(), viewUrl).toURI();

            String scheme = u.getScheme();
            String userInfo = u.getUserInfo();
            String host = u.getHost();
            int port = u.getPort();
            String path = u.getPath();
            String query = u.getQuery();

            return new URI(scheme, userInfo, host, port, path, query, "");
        } catch (MalformedURLException | URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

    private BlackboardLectureUtils() {
    }

}
