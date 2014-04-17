/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.provider;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.logging.ProviderLogger;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardProviderXmlDetails;

import static org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient.DLECT_SHORT_IDENTIFIER;

public class BlackboardProviderInitiliserImpl implements BlackboardProviderInitiliser {

    private static final String BASE_URL = "https://mlcs.medu.com/api/b2_registration/refresh_info?"
                                           + "q=&carrier_code=&carrier_name=&device_name=&platform="
                                           + "&timestamp=&registration_id=&f=xml&device_id=&"
                                           + "&v=1&language=en_GB&ver=" + DLECT_SHORT_IDENTIFIER + "&client_id=";

    private final int providerCode;

    public BlackboardProviderInitiliserImpl(int providerCode) {
        this.providerCode = providerCode;
    }

    @Override
    public BlackboardProviderDetails getProviderDetails() throws DLectException {
        String xml;
        try {
            xml = CharStreams.toString(new InputStreamReader(new URL(BASE_URL + providerCode).openStream(), Charsets.UTF_8));
        } catch (IOException ex) {
            ProviderLogger.LOGGER.error("Problem loading information from url", ex);
            throw new DLectException(DLectExceptionCause.NO_CONNECTION, ex);
        }
        BlackboardProviderXmlDetails bpd;
        try {
            Object um = JAXBContext
                    .newInstance(BlackboardProviderXmlDetails.class)
                    .createUnmarshaller()
                    .unmarshal(new StringReader(xml));

            bpd = (BlackboardProviderXmlDetails) um;
        } catch (JAXBException ex) {
            ProviderLogger.LOGGER.error("Problem loading data from xml. ::" + xml.replace('\n', ' '), ex);
            throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, ex);
        }
        try {
            return new BlackboardProviderDetails(new URI(bpd.getB2Url()), bpd.isHttpAuth(), bpd.isSslLogin());
        } catch (URISyntaxException ex) {
            ProviderLogger.LOGGER.error("Problem converting url: " + bpd.getB2Url(), ex);
            throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, ex);
        }
    }

}
