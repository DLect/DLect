/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.httpclient;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import org.dlect.helper.ApplicationInformation;

/**
 *
 * @author lee
 */
public interface BlackboardHttpClient {

    public static final String DLECT_SHORT_IDENTIFIER = "DLect-" + ApplicationInformation.APPLICATION_VERSION;
    public static final String DLECT_IDENTIFIER = "DLect Lecture Recording Helper/" + ApplicationInformation.APPLICATION_VERSION;

    public HttpResponceStream doPost(URI uri, Map<String, String> postVars) throws IOException;

    public HttpResponceStream doGet(URI uri) throws IOException;
    
    public HttpResponceStream doGetWithoutCookies(URI uri) throws IOException;

}
