/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class BlackboardHttpClientImpl implements BlackboardHttpClient {

    private final HttpClient client = HttpClients.createDefault();

    @Override
    public InputStream doGet(URI uri) throws IOException {
        // TODO Fully consume & close responce:
        /*
         * EntityUtils.consume(entity1);
         * } finally {
         * response1.close();
         * }
         */
        return client.execute(new HttpGet(uri)).getEntity().getContent();
    }

    @Override
    public InputStream doPost(URI uri, Map<String, String> credentials) throws IOException {
        // TODO Fully consume & close responce:
        /*
         * EntityUtils.consume(entity1);
         * } finally {
         * response1.close();
         * }
         */

        HttpPost p = new HttpPost(uri);

        List< NameValuePair> list = Lists.newArrayList();

        for (Entry<String, String> entry : credentials.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            list.add(new BasicNameValuePair(name, value));
        }

        p.setEntity(new UrlEncodedFormEntity(list, Charsets.UTF_8));
        return client.execute(p).getEntity().getContent();
    }

}