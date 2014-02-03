/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.providers.LectureLocator;
import org.lee.echo360.providers.LoginExecuter;
import org.lee.echo360.providers.SubjectLocator;
import org.lee.echo360.util.ExceptionReporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author lee
 */
public class BlackboardMobileProviderImpl extends BlackboardMobileProvider implements
        LoginExecuter {

    private final AbstractHttpClient HTTP_CLIENT = new SystemDefaultHttpClient();
    private final MobileXmlProvider xmlProvider;
    private final BlackboardMobileProviderSubjectImpl subjectImpl;
    private final BlackboardMobileProviderLectureImpl lectureImpl;

    public BlackboardMobileProviderImpl(MobileXmlProvider get, MobileProviderLocaliser mpl) {
        if (mpl == null) {
            throw new IllegalArgumentException("The MobileProviderLocaliser was null");
        } else if (get == null) {
            throw new IllegalArgumentException("The MobileXmlProvider was null");
        }
        this.xmlProvider = get;
        this.subjectImpl = new BlackboardMobileProviderSubjectImpl(get, mpl);
        this.lectureImpl = new BlackboardMobileProviderLectureImpl(get, mpl);
    }

    @Override
    public ActionResult doLogin(Blackboard b) {
        ActionResult result;
        try {
            Document document = xmlProvider.getLoginResponce(b);
            final Element documentElement = document.getDocumentElement();
            String attr = documentElement.getAttribute("status");
            if ("OK".equals(attr)) {
                result = ActionResult.SUCCEDED;
            } else {
                result = ActionResult.INVALID_CREDENTIALS;
            }
        } catch (SAXException ex) {
            ExceptionReporter.reportException(ex);
            result = ActionResult.FATAL;
        } catch (ParserConfigurationException ex) {
            ExceptionReporter.reportException(ex);
            result = ActionResult.FATAL;
        } catch (IOException ex) {
            ExceptionReporter.reportException(ex);
            result = ActionResult.NOT_CONNECTED;
        }
        return result;
    }

    @Override
    public LoginExecuter getLoginExecuter() {
        return this;
    }

    @Override
    public SubjectLocator getSubjectLocator() {
        return subjectImpl;
    }

    @Override
    public LectureLocator getLectureLocator() {
        return lectureImpl;
    }

    @Override
    public AbstractHttpClient getHttpClient() {
        return HTTP_CLIENT;
    }
}
