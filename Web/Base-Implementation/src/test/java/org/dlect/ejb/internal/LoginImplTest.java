/*
 * This file is part of DLect. DLect is a suite of code that facilitates the downloading of lecture recordings.
 *
 * Copyright © 2014 Lee Symes.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dlect.ejb.internal;

import java.io.IOException;
import org.dlect.ejb.LoginEJBTest;
import org.dlect.ejb.internal.provder.UniversityActionProvider;
import org.dlect.ejb.internal.provder.login.LoginActionProvider;
import org.dlect.export.LoginResult;
import org.dlect.export.University;
import org.dlect.internal.beans.LoginCredentialBean;
import org.dlect.object.ResultType;
import org.dlect.test.helpers.TestHelpers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.dlect.test.helpers.TestHelpers.IO_EXCEPTION;
import static org.dlect.test.helpers.TestHelpers.TEST_PASSWORD;
import static org.dlect.test.helpers.TestHelpers.TEST_USERNAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class LoginImplTest {
    
    public LoginImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    final Logger logger = LoggerFactory.getLogger(LoginEJBTest.class);
    
    @Mock
    LoginCredentialBean loginCredentialsMock;
    @Mock
    UniversityActionProvider provMock;
    @Mock
    LoginActionProvider loginMock;
    @Mock
    University universityMock;
    
    @InjectMocks
    LoginImpl testObject;
    
    @Ignore
    @Test
    public void testDoLoginImpl_NullLoginProvider() throws Exception {
        System.out.println("doLoginImpl");
        when(provMock.getLoginActionProvider()).thenReturn(null);
        
//        try {
//            testObject.doLoginImpl(provMock, universityMock, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock);
//            fail("No Exception Thrown");
//        } catch (NoSupportingProvidersException e) {
//            // No Op.
//        }
//        
        verify(provMock).getLoginActionProvider();
        verifyNoMoreInteractions(provMock, loginMock, universityMock, loginCredentialsMock);
    }
    
    @Ignore
    @Test
    public void testDoLoginImpl_LoginException() throws Exception {
        System.out.println("doLoginImpl");
        when(provMock.getLoginActionProvider()).thenReturn(loginMock);
        
        University copyOfMock = mock(University.class);
        when(universityMock.copyOf()).thenReturn(copyOfMock);
        
        when(loginMock.doLoginFor(copyOfMock, TEST_USERNAME, TEST_PASSWORD)).thenThrow(IO_EXCEPTION);
        
//        try {
//            testObject.doLoginImpl(provMock, universityMock, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock);
//            fail("No Exception Thrown");
//        } catch (IOException e) {
//            // No Op.
//        }
        
        verify(universityMock).copyOf();
        
        verify(provMock).getLoginActionProvider();
        verify(loginMock).doLoginFor(copyOfMock, TEST_USERNAME, TEST_PASSWORD);
        verifyNoMoreInteractions(provMock, loginMock, universityMock, loginCredentialsMock);
    }
    
    @Ignore
    @Test
    public void testDoLoginImpl_LoginSuccess() throws Exception {
        System.out.println("doLoginImpl");
        
        LoginResult expRes = TestHelpers.createUniqueLoginResult(ResultType.OK);
        
        University copyOfMock = mock(University.class);
        when(universityMock.copyOf()).thenReturn(copyOfMock);
        
        when(provMock.getLoginActionProvider()).thenReturn(loginMock);
        
//        when(loginMock.doLoginFor(copyOfMock, TEST_USERNAME, TEST_PASSWORD)).thenReturn(expRes);
//        
//        LoginResult res = testObject.doLoginImpl(provMock, universityMock, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock);
//        
//        assertEquals(expRes, res);
//        
//        verify(universityMock).copyOf();
//        
//        verify(provMock).getLoginActionProvider();
//        verify(loginMock).doLoginFor(copyOfMock, TEST_USERNAME, TEST_PASSWORD);
//        verify(loginCredentialsMock).updateFrom(provMock, universityMock, TEST_USERNAME, TEST_PASSWORD, res);
//        verifyNoMoreInteractions(provMock, loginMock, universityMock, loginCredentialsMock);
    }
    
}
