/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard;

import org.dlect.provider.base.blackboard.lecture.plugin.impl.BlackboardLectureCustomiser;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.logging.ProviderLogger;
import org.dlect.provider.DownloadProvider;
import org.dlect.provider.LectureProvider;
import org.dlect.provider.LoginProvider;
import org.dlect.provider.Provider;
import org.dlect.provider.SubjectProvider;
import org.dlect.provider.base.blackboard.download.BlackboardDownloadProvider;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlParser;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlParserImpl;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClientImpl;
import org.dlect.provider.base.blackboard.helper.provider.BlackboardProviderDetails;
import org.dlect.provider.base.blackboard.helper.provider.BlackboardProviderInformation;
import org.dlect.provider.base.blackboard.helper.provider.BlackboardProviderInitiliser;
import org.dlect.provider.base.blackboard.helper.provider.BlackboardProviderInitiliserImpl;
import org.dlect.provider.base.blackboard.helper.provider.BlackboardProviderInitiliserWithBackupImpl;
import org.dlect.provider.base.blackboard.lecture.BlackboardLectureProvider;
import org.dlect.provider.base.blackboard.lecture.BlackboardStreamProvider;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureItemParserBuilder;
import org.dlect.provider.base.blackboard.login.BlackboardSslFormLoginProvider;
import org.dlect.provider.base.blackboard.subject.BlackboardSubjectProvider;

/**
 *
 * @author lee
 */
public class BlackboardBaseProvider implements Provider {

    private final BlackboardSubjectCustomiser subjectCustomiser;
    private final BlackboardLectureItemParserBuilder parserBuilder;
    private final BlackboardStreamProvider streamProvider;
    private final BlackboardProviderInitiliser providerInitiliser;
    private final BlackboardHttpClient httpClient;
    private final BlackboardXmlParser xmlParser;

    private LoginProvider loginProvider = null;
    private SubjectProvider subjectProvider = null;
    private LectureProvider lectureProvider = null;
    private DownloadProvider downloadProvider = null;

    public BlackboardBaseProvider(int providerCode,
                                  BlackboardSubjectCustomiser subjectCustomiser,
                                  BlackboardLectureItemParserBuilder parserBuilder,
                                  BlackboardStreamProvider streamProvider) {
        this(new BlackboardProviderInitiliserImpl(providerCode),
             subjectCustomiser, parserBuilder, streamProvider,
             new BlackboardHttpClientImpl(), new BlackboardXmlParserImpl());
    }

    public BlackboardBaseProvider(BlackboardProviderInformation providerInformation,
                                  BlackboardSubjectCustomiser subjectCustomiser,
                                  BlackboardLectureItemParserBuilder parserBuilder,
                                  BlackboardStreamProvider streamProvider) {
        this(new BlackboardProviderInitiliserWithBackupImpl(providerInformation),
             subjectCustomiser, parserBuilder, streamProvider,
             new BlackboardHttpClientImpl(), new BlackboardXmlParserImpl());
    }

    public BlackboardBaseProvider(BlackboardProviderInitiliser providerInitiliser,
                                  BlackboardSubjectCustomiser subjectCustomiser,
                                  BlackboardLectureItemParserBuilder parserBuilder,
                                  BlackboardStreamProvider streamProvider,
                                  BlackboardHttpClient httpClient,
                                  BlackboardXmlParser xmlParser) {
        this.providerInitiliser = providerInitiliser;

        this.subjectCustomiser = subjectCustomiser;
        this.xmlParser = xmlParser;
        this.parserBuilder = parserBuilder;

        this.streamProvider = streamProvider;
        this.httpClient = httpClient;
    }

    @Override
    public DownloadProvider getDownloadProvider() {
        return downloadProvider;
    }

    @Override
    public LectureProvider getLectureProvider() {
        return lectureProvider;
    }

    @Override
    public LoginProvider getLoginProvider() {
        return loginProvider;
    }

    @Override
    public SubjectProvider getSubjectProvider() {
        return subjectProvider;
    }

    @Override
    public void init() throws DLectException {
        BlackboardProviderDetails u = providerInitiliser.getProviderDetails();
        if (!u.isHttpAuth() && u.isSsl()) {
            loginProvider = new BlackboardSslFormLoginProvider(u, httpClient, xmlParser);
        } else {
            ProviderLogger.LOGGER.error("Failed to load provider. DLect does not support the login type.");
            throw new DLectException(DLectExceptionCause.ILLEGAL_PROVIDER_STATE, "The current implementation does not support the provider with the following details: " + u);
        }

        subjectProvider = new BlackboardSubjectProvider(u.getBaseUrl(), subjectCustomiser, httpClient, xmlParser);
        lectureProvider = new BlackboardLectureProvider(u.getBaseUrl(), httpClient, xmlParser, parserBuilder, streamProvider);
        downloadProvider = new BlackboardDownloadProvider(httpClient);
    }

}
