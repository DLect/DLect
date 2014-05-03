/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.provider.base.blackboard.lecture.plugin.impl.echo;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dlect.provider.base.blackboard.helper.httpclient.HttpResponceStream;

/**
 *
 * @author lee
 */
public class EchoCenterHelper {

    public static String getStringStream(final HttpResponceStream r) throws IOException {
        return CharStreams.toString(new InputStreamReader(r, Charsets.UTF_8));
    }

    public static String matchOrNull(String string, Pattern regex) {
        return matchOrNull(string, 0, regex);
    }

    public static String matchOrNull(String string, int group, Pattern regex) {
        Matcher m = regex.matcher(string);
        if (m.find()) {
            return m.group(group);
        } else {
            return null;
        }
    }

    public static URI toUriFromHtml(URI root, String htmlLink) throws MalformedURLException, URISyntaxException {
        return toUri(root, StringEscapeUtils.unescapeHtml4(htmlLink));
    }
    
    public static URI toUri(URI root, String htmlLink) throws MalformedURLException, URISyntaxException {
        return new URL(root.toURL(), htmlLink).toURI();
    }
    
}
