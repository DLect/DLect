/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.base.Optional;
import org.dlect.immutable.model.ImmutableSemester;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author lee
 */
public class UQRotaSemesterParseHandler extends DefaultHandler {

    private boolean started;
    private StringBuilder content;

    private Integer id;
    private String name;
    private String semNumber;
    private String semYear;

    public Optional<ImmutableSemester> getSemester() {
        if (id == null || name == null || semNumber == null || semYear == null) {
            // TODO Log this error.
            return Optional.absent();
        } else {
            return Optional.of(new ImmutableSemester(id, name, "Sem " + semNumber + ", " + semYear));
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
        String c = content.toString().trim();
        if (id == null && "id".equalsIgnoreCase(qName)) {
            id = Integer.valueOf(c);
        } else if ("name".equalsIgnoreCase(qName)) {
            name = c;
        } else if ("number".equalsIgnoreCase(qName)) {
            semNumber = c;
        } else if ("year".equalsIgnoreCase(qName)) {
            semYear = c;
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
