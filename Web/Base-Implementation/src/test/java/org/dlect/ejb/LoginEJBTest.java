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
package org.dlect.ejb;

import org.dlect.ejb.internal.LoginImpl;
import org.dlect.ejb.internal.provder.UniversityActionEJBLocal;
import org.dlect.ejb.internal.provder.UniversityActionProvider;
import org.dlect.except.NoSupportingProvidersException;
import org.dlect.export.LoginResult;
import org.dlect.export.University;
import org.dlect.internal.beans.LoginCredentialBean;
import org.dlect.object.ResultType;
import org.dlect.test.helpers.TestHelpers.AbortExecutionException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.dlect.test.helpers.TestHelpers.ABORT_EXCEPTION;
import static org.dlect.test.helpers.TestHelpers.TEST_PASSWORD;
import static org.dlect.test.helpers.TestHelpers.TEST_USERNAME;
import static org.dlect.test.helpers.TestHelpers.createLoginResult;
import static org.dlect.test.helpers.TestHelpers.createUniqueLoginResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author Lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class LoginEJBTest {

    final Logger logger = LoggerFactory.getLogger(LoginEJBTest.class);

    @Mock
    LoginCredentialBean loginCredentialsMock;
    @Mock
    UniversityEJBLocal uniEJBMock;
    @Mock
    UniversityActionEJBLocal universityMock;
    @Mock
    LoginImpl loginImpl;

    @InjectMocks
    LoginEJB testObject;

    @Ignore
    @Test
    public void testIsLoggedIn() throws Exception {
        System.out.println("isLoggedIn");

        when(loginCredentialsMock.isValid()).thenReturn(true);

        assertTrue(testObject.isLoggedIn());

        verifyZeroInteractions(uniEJBMock, universityMock, loginImpl);
        verify(loginCredentialsMock).isValid();
        verifyNoMoreInteractions(loginCredentialsMock);
    }

    @Ignore
    @Test
    public void testEnsureLoggedIn_NotValid() throws Exception {
        when(loginCredentialsMock.isValid()).thenReturn(false);

        assertFalse(testObject.ensureLoggedIn());

        verifyZeroInteractions(uniEJBMock, universityMock, loginImpl);
        verify(loginCredentialsMock).isValid();
        verifyNoMoreInteractions(loginCredentialsMock);
    }

    @Ignore
    @Test
    public void testEnsureLoggedIn_Valid_NotTimedOut() throws Exception {
        when(loginCredentialsMock.isValid()).thenReturn(true);
        // Still within the timeout window.
        when(loginCredentialsMock.getLastRequestMade()).thenReturn(System.currentTimeMillis());

        assertTrue(testObject.ensureLoggedIn());

        verifyZeroInteractions(uniEJBMock, universityMock, loginImpl);
        verify(loginCredentialsMock).isValid();
        verify(loginCredentialsMock).getLastRequestMade();
        verifyNoMoreInteractions(loginCredentialsMock);
    }

    @Ignore
    @Test
    public void testEnsureLoggedIn_Valid_TimedOut_Ok() throws Exception {
        University mockUni = mock(University.class);

        UniversityActionProvider uap = mock(UniversityActionProvider.class);

        LoginResult re = createLoginResult(ResultType.OK);

        when(loginCredentialsMock.isValid()).thenReturn(true);
        // Timed out last century
        when(loginCredentialsMock.getLastRequestMade()).thenReturn(0L);
        when(loginCredentialsMock.getUniversity()).thenReturn(mockUni);
        when(loginCredentialsMock.getProvider()).thenReturn(uap);
        when(loginCredentialsMock.getUsername()).thenReturn(TEST_USERNAME);
        when(loginCredentialsMock.getPassword()).thenReturn(TEST_PASSWORD);
//
//        when(loginImpl.doLoginImpl(uap, mockUni, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock)).thenReturn(re);
//
//        assertTrue(testObject.ensureLoggedIn());
//
//        verifyZeroInteractions(uniEJBMock, universityMock);
//
//        verify(loginCredentialsMock).isValid();
//        verify(loginCredentialsMock).getLastRequestMade();
//
//        verifyZeroInteractions(uap, mockUni);
//
//        verify(loginImpl).doLoginImpl(uap, mockUni, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock);
//        verifyNoMoreInteractions(loginImpl);
    }

    @Ignore
    @Test
    public void testEnsureLoggedIn_Valid_TimedOut_Exception() throws Exception {
        University mockUni = mock(University.class);

        UniversityActionProvider uap = mock(UniversityActionProvider.class);

        when(loginCredentialsMock.isValid()).thenReturn(true);
        // Timed out last century
        when(loginCredentialsMock.getLastRequestMade()).thenReturn(0L);
        when(loginCredentialsMock.getUniversity()).thenReturn(mockUni);
        when(loginCredentialsMock.getProvider()).thenReturn(uap);
        when(loginCredentialsMock.getUsername()).thenReturn(TEST_USERNAME);
        when(loginCredentialsMock.getPassword()).thenReturn(TEST_PASSWORD);

        when(loginImpl.doLoginImpl(uap, mockUni, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock)).thenThrow(NoSupportingProvidersException.class);

        assertFalse(testObject.ensureLoggedIn());

        verifyZeroInteractions(uniEJBMock, universityMock);

        verify(loginCredentialsMock).isValid();
        verify(loginCredentialsMock).getLastRequestMade();

        verifyZeroInteractions(uap, mockUni);

        verify(loginImpl).doLoginImpl(uap, mockUni, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock);
        verifyNoMoreInteractions(loginImpl);
    }

    @Ignore
    @Test
    public void testEnsureLoggedIn_Valid_TimedOut_Failed() throws Exception {
        University mockUni = mock(University.class);

        UniversityActionProvider uap = mock(UniversityActionProvider.class);

        LoginResult re = createLoginResult(ResultType.INTERNAL_ERROR);

        when(loginCredentialsMock.isValid()).thenReturn(true);
        // Timed out last century
        when(loginCredentialsMock.getLastRequestMade()).thenReturn(0L);
        when(loginCredentialsMock.getUniversity()).thenReturn(mockUni);
        when(loginCredentialsMock.getProvider()).thenReturn(uap);
        when(loginCredentialsMock.getUsername()).thenReturn(TEST_USERNAME);
        when(loginCredentialsMock.getPassword()).thenReturn(TEST_PASSWORD);
//
//        when(loginImpl.doLoginImpl(uap, mockUni, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock)).thenReturn(re);
//
//        assertFalse(testObject.ensureLoggedIn());
//
//        verifyZeroInteractions(uniEJBMock, universityMock);
//
//        verify(loginCredentialsMock).isValid();
//        verify(loginCredentialsMock).getLastRequestMade();
//
//        verifyZeroInteractions(uap, mockUni);
//
//        verify(loginImpl).doLoginImpl(uap, mockUni, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock);
//        verifyNoMoreInteractions(loginImpl);
    }

    @Ignore
    @Test
    public void testPerformLogin_NullParams() throws Exception {
        when(loginCredentialsMock.sameAs("0", "", "")).thenReturn(true);
        when(loginCredentialsMock.isValid()).thenReturn(true);
        when(loginCredentialsMock.getLastRequestMade()).thenReturn(System.currentTimeMillis());

        testObject.performLogin("0", null, null);

        verifyZeroInteractions(uniEJBMock, universityMock, loginImpl);

        verify(loginCredentialsMock).sameAs("0", "", "");
        verify(loginCredentialsMock).isValid();
        verify(loginCredentialsMock).getLastRequestMade();
        verifyNoMoreInteractions(loginCredentialsMock);
    }
    
    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testPerformLogin_NullCode() throws Exception {
        testObject.performLogin(null, null, null);
    }

    @Ignore
    @Test
    public void testPerformLogin_NotNullParams() throws Exception {
        String code = "10";

        when(loginCredentialsMock.sameAs(code, TEST_USERNAME, TEST_PASSWORD)).thenReturn(true);
        when(loginCredentialsMock.isValid()).thenReturn(true);
        when(loginCredentialsMock.getLastRequestMade()).thenReturn(System.currentTimeMillis());

        testObject.performLogin(code, TEST_USERNAME, TEST_PASSWORD);
        // Nothing should be called
        verifyZeroInteractions(uniEJBMock, universityMock, loginImpl);

        verify(loginCredentialsMock).sameAs(code, TEST_USERNAME, TEST_PASSWORD);
        verify(loginCredentialsMock).isValid();
        verify(loginCredentialsMock).getLastRequestMade();
        verifyNoMoreInteractions(loginCredentialsMock);
    }

    @Ignore
    @Test
    public void testPerformLogin_Same_NotLoggedIn() throws Exception {
        String code = "10";

        // Line 51 if( true , false )
        when(loginCredentialsMock.sameAs(code, TEST_USERNAME, TEST_PASSWORD)).thenReturn(true);
        when(loginCredentialsMock.isValid()).thenReturn(false);

        when(uniEJBMock.getUniversityData(code)).thenThrow(ABORT_EXCEPTION);

        try {
            testObject.performLogin(code, TEST_USERNAME, TEST_PASSWORD);
            fail("No exception thrown.");
        } catch (AbortExecutionException e) {
            // No Op.
        }

        // Nothing should be called
        verifyZeroInteractions(universityMock, loginImpl);

        verify(loginCredentialsMock).sameAs(code, TEST_USERNAME, TEST_PASSWORD);
        verify(loginCredentialsMock).isValid();
        verifyNoMoreInteractions(loginCredentialsMock);
    }

    @Ignore
    @Test
    public void testPerformLogin_AlreadyLoggedIn() throws Exception {
        String code = "10";

        when(loginCredentialsMock.sameAs(code, TEST_USERNAME, TEST_PASSWORD)).thenReturn(true);
        when(loginCredentialsMock.isValid()).thenReturn(true);
        when(loginCredentialsMock.getLastRequestMade()).thenReturn(System.currentTimeMillis());

//        LoginResult res = testObject.performLogin(code, TEST_USERNAME, TEST_PASSWORD);
//
//        assertEquals("Already logged in, should always return success.", ResultType.OK, res.getStatus().getStatus());
//
//        // Nothing should be called - We're already logged in.
//        verifyZeroInteractions(uniEJBMock, universityMock, loginImpl);
//
//        verify(loginCredentialsMock).sameAs(code, TEST_USERNAME, TEST_PASSWORD);
    }

    @Ignore
    @Test
    public void testPerformLogin_BadUniversity() throws Exception {
        String code = "10";

        when(loginCredentialsMock.sameAs(code, TEST_USERNAME, TEST_PASSWORD)).thenReturn(false);

        when(uniEJBMock.getUniversityData(code)).thenReturn(null);
//
//        LoginResult res = testObject.performLogin(code, TEST_USERNAME, TEST_PASSWORD);
//
//        assertNotEquals("No university, should not be ok.", ResultType.OK, res.getStatus().getStatus());
//
//        // Nothing should be called - We're already logged in.
//        verifyZeroInteractions(universityMock, loginImpl);
//
//        verify(loginCredentialsMock).sameAs(code, TEST_USERNAME, TEST_PASSWORD);
//        verifyNoMoreInteractions(loginCredentialsMock);
    }

    @Ignore
    @Test
    public void testPerformLogin_FallThrough() throws Exception {
        String code = "10";

        University uni = mock(University.class);
        UniversityActionProvider uap = mock(UniversityActionProvider.class);

        LoginResult expRes = createUniqueLoginResult(ResultType.OK);

        when(loginCredentialsMock.sameAs(code, TEST_USERNAME, TEST_PASSWORD)).thenReturn(false);

        when(uniEJBMock.getUniversityData(code)).thenReturn(uni);

        when(universityMock.getProviderFor(uni)).thenReturn(uap);

//        when(loginImpl.doLoginImpl(uap, uni, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock)).thenReturn(expRes);
//
//        LoginResult res = testObject.performLogin(code, TEST_USERNAME, TEST_PASSWORD);
//
//        assertEquals("Not directly returning ", expRes, res);
//
//        verify(loginCredentialsMock).sameAs(code, TEST_USERNAME, TEST_PASSWORD);
//
//        verify(loginImpl).doLoginImpl(uap, uni, TEST_USERNAME, TEST_PASSWORD, loginCredentialsMock);
//        verifyNoMoreInteractions(loginImpl);
    }

}
