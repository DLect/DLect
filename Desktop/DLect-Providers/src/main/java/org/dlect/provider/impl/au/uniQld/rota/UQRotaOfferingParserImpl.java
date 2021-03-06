/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import java.io.IOException;
import java.net.URI;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.logging.ProviderLogger;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.xml.sax.SAXException;

/**
 *
 * @author lee
 */
public class UQRotaOfferingParserImpl implements UQRotaOfferingParser {

    private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();

    private final BlackboardHttpClient client;

    public UQRotaOfferingParserImpl(BlackboardHttpClient blackboardHttpClient) {
        this.client = blackboardHttpClient;
    }

    @Override
    public Set<UQRotaStream> getStreamsFor(int offeringID) throws DLectException {
        URI u = URI.create("http://rota.eait.uq.edu.au/offering/" + offeringID + ".xml");

        UQRotaOfferingParseHandler handler = new UQRotaOfferingParseHandler();
        try {
            SAXParser saxParser = PARSER_FACTORY.newSAXParser();

            saxParser.parse(client.doGet(u), handler);

            return handler.getStreams();
        } catch (ParserConfigurationException ex) {
            ProviderLogger.LOGGER.error("Failed to configure parser whilst attempting to read " + u, ex);
            throw new DLectException(DLectExceptionCause.ILLEGAL_PROVIDER_STATE, "Failed to configure parser.", ex);
        } catch (SAXException | IOException ex) {
            ProviderLogger.LOGGER.error("Failed to load xml from " + u, ex);
            throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, "Failed to load xml from UQ Rota url: " + u, ex);
        }
    }

}
