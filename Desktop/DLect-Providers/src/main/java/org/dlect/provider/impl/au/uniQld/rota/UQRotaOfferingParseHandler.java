/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author lee
 */
public class UQRotaOfferingParseHandler extends DefaultHandler {

    private StringBuilder content;
    private OfferingParseState state = null;

    private final Multimap<String, UQRotaStream> streamSeries = HashMultimap.create();

    private String currentSeriesName;
    private UQRotaStream currentStream;
    private List<UQRotaStreamSession> sessions;
    private UQRotaStreamSession currentSession;

    void printStuffs() {
        System.out.println("Streams: ");
        System.out.println(Joiner.on("\n").useForNull("null").join(streamSeries.entries()));

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("series".equalsIgnoreCase(qName)) {
            state = OfferingParseState.SERIES;
        } else if ("group".equalsIgnoreCase(qName)) {
            state = OfferingParseState.GROUP;
        } else if ("session".equalsIgnoreCase(qName)) {
            state = OfferingParseState.SESSION;
            currentSession = new UQRotaStreamSession();
            sessions.add(currentSession);
        } else if ("building".equalsIgnoreCase(qName)) {
            state = OfferingParseState.BUILDING;
        } else if ("event".equalsIgnoreCase(qName)) {
            // TODO read attributes and store.
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (state == null) {
            return;
        }
        String c = content.toString().trim();
        if ("group".equalsIgnoreCase(qName)) {
            currentStream.setSession(sessions);
        } else if ("name".equalsIgnoreCase(qName)) {
            if (state == OfferingParseState.SERIES) {
                currentSeriesName = c;
            } else if (state == OfferingParseState.GROUP) {
                currentStream = new UQRotaStream();
                currentStream.setGroupName(c);
                currentStream.setSeriesName(currentSeriesName);
                streamSeries.put(currentSeriesName, currentStream);
                sessions = Lists.newArrayList();
            }
        } else if (state == OfferingParseState.SESSION) {
            if ("startmins".equalsIgnoreCase(qName)) {
                currentSession.setStartMins(Integer.parseInt(c));
            } else if ("finishmins".equalsIgnoreCase(qName)) {
                currentSession.setEndMins(Integer.parseInt(c));
            } else if ("room".equalsIgnoreCase(qName)) {
                currentSession.setRoom(c);
            }
        } else if (state == OfferingParseState.BUILDING) {
            if ("campus".equalsIgnoreCase(qName)) {
                currentSession.setBuildingCampus(c);
            } else if ("number".equalsIgnoreCase(qName)) {
                currentSession.setBuildingNum(c);
            }
        }
        content = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (state == null) {
            return;
        } else if (content == null) {
            content = new StringBuilder(length + 1);
        }
        content.append(ch, start, length);
    }

    private static enum OfferingParseState {

        SERIES,
        GROUP,
        SESSION,
        BUILDING
    }
}
