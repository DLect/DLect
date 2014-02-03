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
package org.lee.echo360.providers;

import java.awt.Image;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author lee
 */
@Ignore
public class BlackboardProviderWrapperTest {

    private BlackboardProviderWrapper wrapper;
    @Mock
    private BlackboardProvider mockProvider;

    public BlackboardProviderWrapperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // wrapper = new BlackboardProviderWrapper(mockProvider, TestProvider.class);// for simplicity sake.
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockProvider);
    }

    /**
     * Test of getProviderName method, of class BlackboardProviderWrapper.
     */
    @Test
    public void testGetProviderName() throws Exception {
        final String name = "PROVIDER NAME" + Math.random();
        when(mockProvider.getProviderName()).thenReturn(name);
        String resultName = wrapper.getProviderName();
        assertEquals(name, resultName);
        verify(mockProvider).getProviderName();
    }

    /**
     * Test of getProviderName method, of class BlackboardProviderWrapper.
     */
    @Test
    public void testGetProviderNameBad() throws Exception {
        final String name = "PROVIDER NAME" + Math.random();
        when(mockProvider.getProviderName()).thenReturn(name);
        String resultName = wrapper.getProviderName();
        assertEquals(name, resultName);
        verify(mockProvider).getProviderName();
    }

    /**
     * Test of createBlackboard method, of class BlackboardProviderWrapper.
     */
    @Test
    public void testCreateBlackboard() throws Exception {
        Blackboard bb = mock(Blackboard.class);
        when(mockProvider.createBlackboard()).thenReturn(bb);
        Blackboard result = wrapper.createBlackboard();
        assertEquals(bb, result);
        verify(mockProvider).createBlackboard();
        verify(bb).setUsername("");
        verify(bb).setPassword("");
        verifyNoMoreInteractions(bb);
    }

    /**
     * Test of getProviderImage method, of class BlackboardProviderWrapper.
     */
    @Ignore
    @Test
    public void testGetProviderImage() throws Exception {
        System.out.println("getProviderImage");
        BlackboardProviderWrapper instance = null;
        Image expResult = null;
        Image result = instance.getProviderImage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLecturesIn method, of class BlackboardProviderWrapper.
     */
    @Ignore
    @Test
    public void testGetLecturesIn() throws Exception {
        System.out.println("getLecturesIn");
        Subject s = null;
        Blackboard b = null;
        BlackboardProviderWrapper instance = null;
        ActionResult expResult = null;
        ActionResult result = instance.getLecturesIn(b, s);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doLogin method, of class BlackboardProviderWrapper.
     */
    @Test
    public void testDoLogin() throws Exception {
        System.out.println("doLogin");
        Blackboard b = null;
        BlackboardProviderWrapper instance = null;
        ActionResult expResult = null;
        ActionResult result = instance.doLogin(b);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubjects method, of class BlackboardProviderWrapper.
     */
    @Ignore
    @Test
    public void testGetSubjects() throws Exception {
        System.out.println("getSubjects");
        Blackboard b = null;
        BlackboardProviderWrapper instance = null;
        ActionResult expResult = null;
        ActionResult result = instance.getSubjects(b);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProviderClass method, of class BlackboardProviderWrapper.
     */
    @Ignore
    @Test
    public void testGetProviderClass() {
        System.out.println("getProviderClass");
        BlackboardProviderWrapper instance = null;
        Class expResult = null;
        Class result = instance.getProviderClass();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadLectureTo method, of class BlackboardProviderWrapper.
     */
    @Ignore
    @Test
    public void testDownloadLectureTo() throws Exception {
        System.out.println("downloadLectureTo");
        Blackboard b = null;
        Subject s = null;
        Lecture l = null;
        boolean video = false;
        File fls = null;
        BlackboardProviderWrapper instance = null;
        ActionResult expResult = null;
        ActionResult result = instance.downloadLectureTo(b, s, l, DownloadType.AUDIO, fls);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
