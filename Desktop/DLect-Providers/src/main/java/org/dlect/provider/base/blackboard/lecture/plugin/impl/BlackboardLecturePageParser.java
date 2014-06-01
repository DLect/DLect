/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin.impl;

import java.net.URI;
import org.dlect.exception.DLectException;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardLectureRecordingPage;

/**
 *
 * @author lee
 */
public abstract class BlackboardLecturePageParser {

    private final BlackboardHttpClient client;

    public BlackboardLecturePageParser(BlackboardHttpClient client) {
        this.client = client;
    }

    protected BlackboardHttpClient getClient() {
        return client;
    }

    public abstract BlackboardLectureRecordingPage getPageFor(URI normal) throws DLectException;

}
