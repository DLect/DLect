/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import org.dlect.immutable.model.ImmutableSemester;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author lee
 */
public class UQRotaSemesterParserHandler extends DefaultHandler {

    private boolean started;
    private StringBuilder content;

    private Integer id;
    private String name;
    private String semNumber;
    private String semYear;

    public ImmutableSemester getSemester() {
        if (id == null || name == null || semNumber == null || semYear == null) {
            throw new IllegalStateException("Not completed reading in semester information");
        } else {
            return new ImmutableSemester(id, name, "Sem " + semNumber + ", " + semYear);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("semester".equalsIgnoreCase(qName)) {
            started = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("semester".equalsIgnoreCase(qName)) {
            started = false;
            return;
        } else if (content == null || !started) {
            return;
        }
        if ("id".equalsIgnoreCase(qName)) {
            id = Integer.valueOf(content.toString());
        } else if ("name".equalsIgnoreCase(qName)) {
            name = content.toString();
        } else if ("number".equalsIgnoreCase(qName)) {
            semNumber = content.toString();
        } else if ("year".equalsIgnoreCase(qName)) {
            semYear = content.toString();
        }
        content = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!started) {
            return;
        } else if (content == null) {
            content = new StringBuilder(length + 1);
        }
        content.append(ch, start, length);
    }

}
