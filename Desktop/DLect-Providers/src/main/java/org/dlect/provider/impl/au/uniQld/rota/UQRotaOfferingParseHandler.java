/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import org.dlect.logging.ProviderLogger;
import org.dlect.model.helper.ThreadLocalDateFormat;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author lee
 */
public class UQRotaOfferingParseHandler extends DefaultHandler {

    private static final ThreadLocalDateFormat DATE_FORMAT = new ThreadLocalDateFormat("yyyy-MM-dd");

    private StringBuilder content;
    private OfferingParseState state = null;

    private final Multimap<String, UQRotaStream> streamSeries = HashMultimap.create();

    private String currentSeriesName;
    private UQRotaStream currentStream;
    private Set<UQRotaStreamSession> sessions;
    private UQRotaStreamSession currentSession;

    public Set<UQRotaStream> getStreams() {
        Set<UQRotaStream> streams = Sets.newHashSet();
        for (Entry<String, Collection<UQRotaStream>> e : streamSeries.asMap().entrySet()) {
            Collection<UQRotaStream> seriesStreams = e.getValue();
            if (seriesStreams.size() == 1) {
                UQRotaStream stream = seriesStreams.iterator().next();
                stream.setGroupNumber("");
            }
            streams.addAll(seriesStreams);
        }
        return streams;
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
            if (Boolean.parseBoolean(attributes.getValue("taught"))) {
                try {
                    currentSession.addDate(DATE_FORMAT.parse(attributes.getValue("date")));
                } catch (ParseException ex) {
                    ProviderLogger.LOGGER.error("Failed to parse date: \"" + attributes.getValue("date") + "\"", ex);
                }
            }

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
                currentStream.setGroupNumber(c);
                currentStream.setSeriesName(currentSeriesName);
                streamSeries.put(currentSeriesName, currentStream);
                sessions = Sets.newHashSet();
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
