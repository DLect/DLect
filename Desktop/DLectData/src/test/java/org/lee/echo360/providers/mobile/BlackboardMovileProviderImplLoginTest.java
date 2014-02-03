/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lee.echo360.providers.mobile;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.LoginResult;
import static org.lee.echo360.test.TestUtilities.*;
import static org.mockito.Mockito.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author lee
 */
public class BlackboardMovileProviderImplLoginTest {

    private static final String DO_LOGIN_OK = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<mobileresponse status=\"OK\" userid=\"_345424_1\"/>";
    private static final String DO_LOGIN_FAIL = "<mobileresponse status=\"LOGIN_FAILED\">\n"
            + "    <![CDATA[ ...\n"
            + "        ...\n"
            + "    ]]>\n"
            + "</mobileresponse>";
    private MobileXmlProvider xmlProv;
    private Blackboard b;
    private MobileProviderLocaliser localiser;
    private BlackboardMobileProviderImpl bmp;

    public BlackboardMovileProviderImplLoginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    public static Blackboard createBlackboard() {
        final Blackboard b = new Blackboard();
        b.setUsername("testUser");
        b.setPassword("testPass");
        return b;
    }

    @Before
    public void preTestInit() {
        b = createBlackboard();
        xmlProv = mock(MobileXmlProvider.class);
        localiser = mock(MobileProviderLocaliser.class);
        bmp = new BlackboardMobileProviderImpl(xmlProv, localiser);
    }

    @After
    public void postTestCheck() throws Exception {
        verify(xmlProv).getLoginResponce(b);
        verifyNoMoreInteractions(xmlProv);
        verifyZeroInteractions(localiser);
    }

    /**
     * Test of doLogin method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testDoLoginWithOk() throws Exception {
        final Document responce = createXMLDocument(BlackboardMovileProviderImplLoginTest.DO_LOGIN_OK);
        doReturn(responce).when(xmlProv).getLoginResponce(b);
        ActionResult result = bmp.doLogin(b);
        assertEquals(ActionResult.SUCCEDED, result);
    }

    /**
     * Test of doLogin method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testDoLoginWithFail() throws Exception {
        final Document responce = createXMLDocument(BlackboardMovileProviderImplLoginTest.DO_LOGIN_FAIL);
        doReturn(responce).when(xmlProv).getLoginResponce(b);
        ActionResult result = bmp.doLogin(b);
        assertEquals(ActionResult.INVALID_CREDENTIALS, result);
    }

    /**
     * Test of doLogin method, of class BlackboardMobileProviderImpl.
     */
    public void testDoLoginWithNoInternet() throws Exception {
        doThrow(new IOException()).when(xmlProv).getLoginResponce(b);
        ActionResult result = bmp.doLogin(b);
        assertEquals(LoginResult.threwError(LoginResult.NOT_CONNECTED), result);
    }

    /**
     * Test of doLogin method, of class BlackboardMobileProviderImpl.
     */
    @Test
    public void testDoLoginWithBadXml() throws Exception {
        doThrow(new SAXException()).when(xmlProv).getLoginResponce(b);
        ActionResult result = bmp.doLogin(b);
        assertEquals(ActionResult.FATAL, result);
    }
//
//    /**
//     * Test of getSubjects method, of class BlackboardMobileProviderImpl.
//     */
//    @Test
//    @Ignore
//    public void testGetSubjects() {
//        System.out.println("getSubjects");
//        Blackboard b = null;
//        BlackboardMobileProviderImpl instance = null;
//        ActionResult expResult = null;
//        ActionResult result = instance.getSubjects(b);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getLecturesIn method, of class BlackboardMobileProviderImpl.
//     */
//    @Test
//    @Ignore
//    public void testGetLecturesIn() {
//        System.out.println("getLecturesIn");
//        Subject s = null;
//        Blackboard b = null;
//        BlackboardMobileProviderImpl instance = null;
//        ActionResult expResult = null;
//        ActionResult result = instance.getLecturesIn(s, b);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getLoginExecuter method, of class BlackboardMobileProviderImpl.
//     */
//    @Test
//    @Ignore
//    public void testGetLoginExecuter() {
//        System.out.println("getLoginExecuter");
//        BlackboardMobileProviderImpl instance = null;
//        LoginExecuter expResult = null;
//        LoginExecuter result = instance.getLoginExecuter();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getSubjectLocator method, of class BlackboardMobileProviderImpl.
//     */
//    @Test
//    @Ignore
//    public void testGetSubjectLocator() {
//        System.out.println("getSubjectLocator");
//        BlackboardMobileProviderImpl instance = null;
//        SubjectLocator expResult = null;
//        SubjectLocator result = instance.getSubjectLocator();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getLectureLocator method, of class BlackboardMobileProviderImpl.
//     */
//    @Test
//    @Ignore
//    public void testGetLectureLocator() {
//        System.out.println("getLectureLocator");
//        BlackboardMobileProviderImpl instance = null;
//        LectureLocator expResult = null;
//        LectureLocator result = instance.getLectureLocator();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of preLogin method, of class BlackboardMobileProviderImpl.
//     */
//    @Test
//    @Ignore
//    public void testPreLogin() {
//        System.out.println("preLogin");
//        BlackboardMobileProviderImpl instance = null;
//        instance.preLogin();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getHttpClient method, of class BlackboardMobileProviderImpl.
//     */
//    @Test
//    @Ignore
//    public void testGetHttpClient() {
//        System.out.println("getHttpClient");
//        BlackboardMobileProviderImpl instance = null;
//        AbstractHttpClient expResult = null;
//        AbstractHttpClient result = instance.getHttpClient();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
