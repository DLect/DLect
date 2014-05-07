/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.model.helper.ThreadLocalDateFormat;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.httpclient.HttpResponceStream;
import org.dlect.test.helper.DataLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.base.Charsets.UTF_8;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class UQRotaOfferingParserImplTest {

    private static final ThreadLocalDateFormat DATE = new ThreadLocalDateFormat("yyyy-M-d");
    @Mock
    private BlackboardHttpClient httpClient;

    @InjectMocks
    private UQRotaOfferingParserImpl testObject;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getStreamsFor method, of class UQRotaOfferingParser.
     */
    @Test
    public void testGetStreamsFor_doGetFails() throws Exception {
        when(httpClient.doGet(getUriFor(1))).thenThrow(IOException.class);
        try {
            testObject.getStreamsFor(1);
            fail("No error thrown");
        } catch (DLectException ex) {
            assertEquals(ex.getCauseCode(), DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE);
            verify(httpClient).doGet(getUriFor(1));
            verifyNoMoreInteractions(httpClient);
        }
    }

    /**
     * Test of getStreamsFor method, of class UQRotaOfferingParser.
     */
    @Test
    public void testGetStreamsFor_doGetNonXml() throws Exception {
        when(httpClient.doGet(getUriFor(1))).thenReturn(toIs("Not XML!"));
        try {
            testObject.getStreamsFor(1);
            fail("No error thrown");
        } catch (DLectException ex) {
            assertEquals(ex.getCauseCode(), DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE);
            verify(httpClient).doGet(getUriFor(1));
            verifyNoMoreInteractions(httpClient);
        }
    }

    /**
     * Test of getStreamsFor method, of class UQRotaOfferingParser.
     */
    @Test
    public void testGetStreamsFor_doGetValidXml() throws Exception {
        HttpResponceStream is = toIs(DataLoader.loadResource("org/dlect/provider/impl/au/uniQld/rota/offeringComplete.xml", this, 1000));
        when(httpClient.doGet(getUriFor(1))).thenReturn(is);

        Set<UQRotaStream> result = testObject.getStreamsFor(1);

        Set<UQRotaStream> expecteds = ImmutableSet.of(
                new UQRotaStream("L", "", ImmutableList.of(
                                         new UQRotaStreamSession(720, 830, "102E", "43A", "STLUC", ImmutableList.of(
                                                                         date(2012, 7, 25),
                                                                         date(2012, 8, 1)
                                                                 )),
                                         new UQRotaStreamSession(300, 350, "1E", "4A", "STLUCIA", ImmutableList.of(
                                                                         date(2012, 7, 26),
                                                                         date(2012, 8, 2)
                                                                 ))
                                 )),
                new UQRotaStream("T", "1", ImmutableList.of(
                                         new UQRotaStreamSession(840, 890, "211", "35", "STLUC", ImmutableList.of(
                                                                         date(2012, 7, 25)
                                                                 ))
                                 )),
                new UQRotaStream("T", "2", ImmutableList.of(
                                         new UQRotaStreamSession(900, 950, "237", "3", "STLUC", ImmutableList.of(
                                                                         date(2012, 7, 25)
                                                                 ))
                                 ))
        );

        assertEquals(expecteds, result);
    }

    private Date date(int y, int m, int d) throws ParseException {
        return DATE.parse(y + "-" + m + "-" + d);
    }

    private HttpResponceStream toIs(String str) {
        return new HttpResponceStream(new ByteArrayInputStream(str.getBytes(UTF_8)), null, URI.create("http://www.google.com/"));
    }

    private URI getUriFor(int offeringId) {
        return URI.create("http://rota.eait.uq.edu.au/offering/" + offeringId + ".xml");
    }

}
