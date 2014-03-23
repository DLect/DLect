/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import static com.google.common.collect.ImmutableList.of;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.dlect.exception.DLectException;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;

/**
 *
 * @author lee
 */
public class UQRotaStreamHelper {

    private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();

    private final BlackboardHttpClient client;
    private final UQRotaOfferingSearchParser searchParser;

    public UQRotaStreamHelper(BlackboardHttpClient client) {
        this(client, new UQRotaOfferingSearchParser(client));
    }

    public UQRotaStreamHelper(BlackboardHttpClient client, UQRotaOfferingSearchParser searchParser) {
        this.client = client;
        this.searchParser = searchParser;
    }

    public List<ImmutableStream> getOffering(String subjectCode, int semesterNum) throws DLectException {
        int offeringId = searchParser.getOfferingId(subjectCode, semesterNum);

        
        return of();
    }

}
