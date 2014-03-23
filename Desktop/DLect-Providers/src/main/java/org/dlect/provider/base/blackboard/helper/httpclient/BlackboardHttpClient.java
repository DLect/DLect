/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 *
 * @author lee
 */
public interface BlackboardHttpClient {

    public static final String DLECT_IDENTIFIER = "DLect Lecture Recording Helper/3.0";

    public InputStream doPost(URI uri, Map<String, String> postVars) throws IOException;

    public InputStream doGet(URI uri) throws IOException;

}
