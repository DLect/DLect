/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
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
public class UQRotaOfferingSearchParserTest {

    @Mock
    private BlackboardHttpClient httpClient;

    @InjectMocks
    private UQRotaOfferingSearchParserImpl testObject;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getJsonFor method, of class UQRotaOfferingSearchParser.
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testGetJsonFor_ValidData() throws UnsupportedEncodingException {
        String subject = "CSSE1001";
        int semNum = 10;
        String argument = UQRotaOfferingSearchParserImpl.getJsonFor(subject, semNum);

        String decoded = URLDecoder.decode(argument, "UTF-8");

        String target = "{\"course_code\": \"CSSE1001\", \"semester_id\": 10, \"mode\": \"Internal\"}";

        assertEquals(target, decoded);

        assertEquals(URLEncoder.encode(target, "UTF-8"), argument);
    }

    /**
     * Test of getJsonFor method, of class UQRotaOfferingSearchParser.
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testGetJsonFor_PartiallyValidData() throws UnsupportedEncodingException {
        String subject = "CSSE1001/7001";
        int semNum = 120;
        String argument = UQRotaOfferingSearchParserImpl.getJsonFor(subject, semNum);

        String decoded = URLDecoder.decode(argument, "UTF-8");

        String target = "{\"course_code\": \"CSSE1001\", \"semester_id\": 120, \"mode\": \"Internal\"}";

        assertEquals(target, decoded);

        assertEquals(URLEncoder.encode(target, "UTF-8"), argument);
    }

    /**
     * Test of getJsonFor method, of class UQRotaOfferingSearchParser.
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @Test(expected = RuntimeException.class)
    public void testGetJsonFor_ShortSubject() throws UnsupportedEncodingException {
        String subject = "No"; // Not long enough
        int semNum = 0;
        UQRotaOfferingSearchParserImpl.getJsonFor(subject, semNum);
    }

    /**
     * Test of getOfferingId method, of class UQRotaOfferingSearchParser.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOfferingId_IOException() throws Exception {
        String course = "NOPE1000";
        int sem = 10;
        when(httpClient.doGet(getUriFor(course, sem))).thenThrow(IOException.class);

        try {
            testObject.getOfferingId(course, sem);
            fail("Oops. No Exception.");
        } catch (DLectException ex) {
            verify(httpClient).doGet(getUriFor(course, sem));
            verifyNoMoreInteractions(httpClient);

            assertEquals(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, ex.getCauseCode());
        }
    }

    /**
     * Test of getOfferingId method, of class UQRotaOfferingSearchParser.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOfferingId_BadXml() throws Exception {
        String course = "NXML1000";
        int sem = 10;
        when(httpClient.doGet(getUriFor(course, sem))).thenReturn(toIs("No xml here!"));

        try {
            testObject.getOfferingId(course, sem);
            fail("Oops. No Exception.");
        } catch (DLectException ex) {
            verify(httpClient).doGet(getUriFor(course, sem));
            verifyNoMoreInteractions(httpClient);

            assertEquals(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, ex.getCauseCode());
        }
    }

    /**
     * Test of getOfferingId method, of class UQRotaOfferingSearchParser.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOfferingId_NoResults() throws Exception {
        HttpResponceStream is = toIs(DataLoader.loadResource("org/dlect/provider/impl/au/uniQld/rota/offeringSearchNoResults.xml", this, 20));
        String course = "NXML1000";
        int sem = 10;
        when(httpClient.doGet(getUriFor(course, sem))).thenReturn(is);

        try {
            testObject.getOfferingId(course, sem);
            fail("Oops. No Exception.");
        } catch (DLectException ex) {
            verify(httpClient).doGet(getUriFor(course, sem));
            verifyNoMoreInteractions(httpClient);

            assertEquals(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, ex.getCauseCode());
        }
    }

    /**
     * Test of getOfferingId method, of class UQRotaOfferingSearchParser.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOfferingId_SingleResult() throws Exception {
        HttpResponceStream is = toIs(DataLoader.loadResource("org/dlect/provider/impl/au/uniQld/rota/offeringSearchComplete.xml", this, 20));
        String course = "OXML1000";
        int sem = 10;
        when(httpClient.doGet(getUriFor(course, sem))).thenReturn(is);

        int res = testObject.getOfferingId(course, sem);

        verify(httpClient).doGet(getUriFor(course, sem));
        verifyNoMoreInteractions(httpClient);

        assertEquals(21081, res);
    }

    /**
     * Test of getOfferingId method, of class UQRotaOfferingSearchParser.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOfferingId_MultipleResult() throws Exception {
        HttpResponceStream is = toIs(DataLoader.loadResource("org/dlect/provider/impl/au/uniQld/rota/offeringSearchMultipleResults.xml", this, 20));
        String course = "MXML1000";
        int sem = 10;
        when(httpClient.doGet(getUriFor(course, sem))).thenReturn(is);

        int res = testObject.getOfferingId(course, sem);

        verify(httpClient).doGet(getUriFor(course, sem));
        verifyNoMoreInteractions(httpClient);

        assertEquals(21081, res);
    }

    private HttpResponceStream toIs(String str) {
        return new HttpResponceStream(new ByteArrayInputStream(str.getBytes(UTF_8)), null, URI.create("http://www.google.com/"));
    }

    private URI getUriFor(String subject, int semNum) throws UnsupportedEncodingException {
        return URI.create("http://rota.eait.uq.edu.au/offerings/find.xml?with="
                          + URLEncoder.encode("{\"course_code\": \"" + subject
                                              + "\", \"semester_id\": " + semNum
                                              + ", \"mode\": \"Internal\"}", "UTF-8")
        );
    }

}
