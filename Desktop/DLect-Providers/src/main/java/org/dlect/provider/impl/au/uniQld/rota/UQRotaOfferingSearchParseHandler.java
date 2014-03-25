/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.base.Optional;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author lee
 */
public class UQRotaOfferingSearchParseHandler extends DefaultHandler {

    private boolean started;
    private StringBuilder content;

    private Integer offeringId;

    public Optional<Integer> getOfferingID() {
        return Optional.fromNullable(offeringId);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("offering".equalsIgnoreCase(qName)) {
            started = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (offeringId != null || !started) {
            return;
        }
        if ("id".equalsIgnoreCase(qName)) {
            offeringId = Integer.parseInt(content.toString().trim());
        }
        content = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!started || offeringId != null) {
            return;
        } else if (content == null) {
            content = new StringBuilder(length + 1);
        }
        content.append(ch, start, length);
    }

}
