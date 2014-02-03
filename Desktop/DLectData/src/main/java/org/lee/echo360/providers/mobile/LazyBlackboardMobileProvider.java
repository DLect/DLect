/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.LectureLocator;
import org.lee.echo360.providers.LoginExecuter;
import org.lee.echo360.providers.SubjectLocator;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class LazyBlackboardMobileProvider extends BlackboardMobileProvider implements LoginExecuter, SubjectLocator, LectureLocator {

    BlackboardMobileProvider actualProvider = null;
    private final int clientId;
    private final MobileProviderLocaliser mpl;
    private final HttpClient httpClient;

    public LazyBlackboardMobileProvider(int clientId, MobileProviderLocaliser mpl, HttpClient httpClient) {
        this.clientId = clientId;
        this.mpl = mpl;
        this.httpClient = httpClient;
    }

    @Override
    public LoginExecuter getLoginExecuter() {
        return this;
    }

    @Override
    public SubjectLocator getSubjectLocator() {
        return this;
    }

    @Override
    public LectureLocator getLectureLocator() {
        return this;
    }

    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public ActionResult doLogin(Blackboard b) {
        if (!createProvider()) {
            if (!online()) {
                return ActionResult.NOT_CONNECTED;
            } else {
                return ActionResult.FAILED;
            }
        } else {
            return actualProvider.getLoginExecuter().doLogin(b);
        }
    }

    @Override
    public ActionResult getSubjects(Blackboard b) {
        if (!createProvider()) {
            if (!online()) {
                return ActionResult.NOT_CONNECTED;
            } else {
                return ActionResult.FAILED;
            }
        } else {
            return actualProvider.getSubjectLocator().getSubjects(b);
        }
    }

    @Override
    public ActionResult getLecturesIn(Blackboard b, Subject s) {
        if (!createProvider()) {
            if (!online()) {
                return ActionResult.NOT_CONNECTED;
            } else {
                return ActionResult.FAILED;
            }
        } else {
            return actualProvider.getLectureLocator().getLecturesIn(b, s);
        }
    }

    private boolean online() {
        HttpHead basicHead = new HttpHead("http://www.google.com.au/");
        try {
            httpClient.execute(basicHead);
            return true;
        } catch (IOException ex) {
            ExceptionReporter.reportException(ex);
            return false;
        }
    }

    private boolean createProvider() {
        if (actualProvider == null) {
            actualProvider = BlackboardMobileProvider.createProvider(clientId, mpl, httpClient);
            if (actualProvider == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
