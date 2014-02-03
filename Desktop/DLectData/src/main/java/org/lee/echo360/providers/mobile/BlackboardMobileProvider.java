/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.lee.echo360.model.ProviderInformation;
import org.lee.echo360.providers.LectureLocator;
import org.lee.echo360.providers.LoginExecuter;
import org.lee.echo360.providers.SubjectLocator;
import org.lee.echo360.util.ExceptionReporter;
import org.lee.echo360.util.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author lee
 */
public abstract class BlackboardMobileProvider {

    private static transient final LoadingCache<Integer, Optional< ProviderInformation>> providers = CacheBuilder.newBuilder().weakValues().build(new CacheLoader<Integer, Optional< ProviderInformation>>() {
        @Override
        public Optional< ProviderInformation> load(Integer clientId) throws Exception {
            InputStream stream = null;
            try {
                System.out.println((DATA_REFRESH_PRE_CLIENT_ID + clientId + DATA_REFRESH_POST_CLIENT_ID));
                URL u = new URL((DATA_REFRESH_PRE_CLIENT_ID + clientId + DATA_REFRESH_POST_CLIENT_ID));
                stream = u.openStream();
                ProviderInformation provider = parseProviderFromStream(stream);
                return Optional.fromNullable(provider);
            } catch (IOException ex) {
                ExceptionReporter.reportException(ex);
                // There was an error. I'll treat it as not supported.
                return Optional.absent();
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException ex) {
                    ExceptionReporter.reportException(ex);
                }
            }
        }
    });
    public static final String DATA_REFRESH_PRE_CLIENT_ID = "https://mlcs.medu.com/api/b2_registration/refresh_info?q=&carrier_code=&carrier_name=&device_name=&platform=&client_id=";
    public static final String DATA_REFRESH_POST_CLIENT_ID = "&timestamp=&registration_id=&f=xml&device_id=&android=1&v=1&language=en_GB&ver=3.1.2";

    public static boolean checkClientIdSupported(int clientId) {
        if (clientId >= 1) {
            try {
                Optional<ProviderInformation> provider = providers.get(clientId);
                if (provider.isPresent()) {
                    return !provider.get().isUsingHttpAuth() && provider.get().isUsingSSL();
                }
            } catch (ExecutionException ex) {
                ExceptionReporter.reportException(ex);
            } catch (UncheckedExecutionException ex) {
                ExceptionReporter.reportException(ex);
            }
        }
        return false;
    }

    /**
     *
     * @param clientId
     * @param mpl
     * @return An implementation of the {@linkplain BlackboardMobileProvider},
     * or {@code null} if an error occurred.
     */
    public static BlackboardMobileProvider createProvider(int clientId, MobileProviderLocaliser mpl) {
        return createProvider(clientId, mpl, new SystemDefaultHttpClient());
    }

    public static BlackboardMobileProvider createProvider(int clientId, MobileProviderLocaliser mpl, HttpClient httpClient) {
        try {
            Optional<ProviderInformation> prov = providers.get(clientId);
            if (prov.isPresent()) {
                return new BlackboardMobileProviderImpl(new DefaultMobileXmlProviderImpl(prov.get(), httpClient, false), mpl);
            }
            ExceptionReporter.reportException("Provider is not present" + clientId);
            return null;
        } catch (ExecutionException ex) {
            ExceptionReporter.reportException(ex);
            return null;
        } catch (UncheckedExecutionException ex) {
            ExceptionReporter.reportException(ex);
            return null;
        }
    }

    protected static ProviderInformation parseProviderFromStream(InputStream stream) throws IOException {
        try {
            Document doc = XMLUtil.getDocumentFromStream(stream);
            String name = XMLUtil.getTextByXPath(doc, "s/name");
            String b2Url = XMLUtil.getTextByXPath(doc, "s/b2_url");
            boolean httpAuth = XMLUtil.getBooleanByXPath(doc, "s/http_auth");
            int clientId = (int) XMLUtil.getLongByXPath(doc, "s/client_id");
            boolean sslLogin = XMLUtil.getBooleanByXPath(doc, "s/can_has_ssl_login");
            if (name != null && !name.isEmpty() && b2Url != null && !b2Url.isEmpty() && clientId > 0) {
                return new ProviderInformation(name, b2Url, clientId, httpAuth, sslLogin);
            } else {
                ExceptionReporter.reportException("Parse Provider From Stream failed.\nname=" + name + "\nb2Url=" + b2Url + "\nhttpAuth=" + httpAuth + "\nclientId=" + clientId + "\nsslLogin=" + sslLogin);
            }
        } catch (SAXException ex) {
            ExceptionReporter.reportException(ex);
        } catch (ParserConfigurationException ex) {
            ExceptionReporter.reportException(ex);
        }
        // There was an error. I'll treat it as not supported.
        return null;
    }

    public static BlackboardMobileProvider createLazyProvider(int clientId, MobileProviderLocaliser mpl) {
        return createLazyProvider(clientId, mpl, new SystemDefaultHttpClient());
    }

    public static BlackboardMobileProvider createLazyProvider(int clientId, MobileProviderLocaliser mpl, HttpClient httpClient) {
        return new LazyBlackboardMobileProvider(clientId, mpl, httpClient);
    }

    protected BlackboardMobileProvider() {
    }

    public abstract LoginExecuter getLoginExecuter();

    public abstract SubjectLocator getSubjectLocator();

    public abstract LectureLocator getLectureLocator();

    public abstract HttpClient getHttpClient();
}
