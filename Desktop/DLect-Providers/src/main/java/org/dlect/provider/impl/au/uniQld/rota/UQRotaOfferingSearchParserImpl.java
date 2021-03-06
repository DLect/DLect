/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.base.Optional;
import java.io.IOException;
import java.net.URI;
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
public class UQRotaOfferingSearchParserImpl implements UQRotaOfferingSearchParser {

    private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();

    private final BlackboardHttpClient client;

    public UQRotaOfferingSearchParserImpl(BlackboardHttpClient client) {
        this.client = client;
    }

    protected static String getJsonFor(String subjectCode, int semesterNum) {
        String cleanedCode = subjectCode.replaceAll("[^A-Z0-9]", "");
        cleanedCode = cleanedCode.substring(0, 8);

        // Pre-encoded: {"course_code":"...", "semester_id": ..., "mode": "Internal"}
        String json = "%7B%22course_code%22%3A+%22" + cleanedCode + "%22%2C"
                      + "+%22semester_id%22%3A+" + semesterNum + "%2C"
                      + "+%22mode%22%3A+%22Internal%22%7D";

        return json;
    }

    @Override
    public int getOfferingId(String subjectCode, int semesterNum) throws DLectException {
        URI u = URI.create("http://rota.eait.uq.edu.au/offerings/find.xml?with=" + getJsonFor(subjectCode, semesterNum));

        UQRotaOfferingSearchParseHandler handler = new UQRotaOfferingSearchParseHandler();
        try {
            SAXParser saxParser = PARSER_FACTORY.newSAXParser();

            saxParser.parse(client.doGet(u), handler);

            Optional<Integer> id = handler.getOfferingID();
            if (id.isPresent()) {
                return id.get();
            } else {
                throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, "No offerings detected for uri: " + u);
            }
        } catch (ParserConfigurationException ex) {
            ProviderLogger.LOGGER.error("Failed to configure parser whilst attempting to read " + u, ex);
            throw new DLectException(DLectExceptionCause.ILLEGAL_PROVIDER_STATE, "Failed to configure parser.", ex);
        } catch (SAXException | IOException ex) {
            ProviderLogger.LOGGER.error("Failed to load xml from " + u, ex);
            throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, "Failed to load xml from UQ Rota url: " + u, ex);
        }
    }

}
