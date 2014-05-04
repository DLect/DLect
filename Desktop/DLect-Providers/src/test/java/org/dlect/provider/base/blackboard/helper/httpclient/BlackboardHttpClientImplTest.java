/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.httpclient;

import java.net.URI;
import org.dlect.helpers.FileDebuggingHelper;
import org.dlect.logging.TestLogging;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.dlect.test.helper.ExtendedAssume.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class BlackboardHttpClientImplTest {

    // TODO 'hack' the httpClient variables to be mocks. Then we can test as though it was injected.
    private BlackboardHttpClientImpl testObject;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    @Ignore
    public void testExamplar() throws Exception {
        Object o = mock(Object.class);
        assertNotNull(o);
        fail();
    }

    /**
     * Test of doGet method, of class BlackboardHttpClientImpl.
     */
    @Test
    public void testDoGet() throws Exception {
//        assumeConnection("http://rota.eait.uq.edu.au/semester/6420.xml");
//        FileDebuggingHelper.debugUTF8StreamToLogger(
//                testObject.doGet(
//                        URI.create("http://rota.eait.uq.edu.au/semester/6420.xml")
//                ),
//                "semester",
//                TestLogging.LOG);
    }

    /**
     * Test of doPost method, of class BlackboardHttpClientImpl.
     */
    @Test
    public void testDoPost() throws Exception {
    }

}
