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

import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import org.dlect.export.University;
import org.dlect.internal.data.UniversityData;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.dlect.helpers.TimeHelper.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
@Ignore
public class UniversityEJBTest {

    // TODO re-write tests.
    
    @Mock
    private UniversityUpdateEJBLocal updateMock;
    @Mock
    private AsynchronousUniversityUpdateEJBLocal aysncUpdateMock;
    @Mock
    private EntityManager managerMock;


    @InjectMocks
    private UniversityEJB testObject;

    @Test
    public void testGetUniversityData_FindNull() throws Exception {
        String code = "10";

        University u = mock(University.class);
        UniversityData ud = mock(UniversityData.class);


        when(managerMock.find(UniversityData.class, code)).thenReturn(null);
        when(updateMock.updateUniversity(code)).thenReturn(ud);

        University res = testObject.getUniversityData(code);

        assertEquals(u, res);

        verify(managerMock).find(UniversityData.class, code);
        verify(updateMock).updateUniversity(code);

        verifyNoMoreInteractions(updateMock, managerMock);
    }

    @Test
    public void testGetUniversityData_FindWellTimedOut() throws Exception {
        String code = "10";

        University u = mock(University.class);
        UniversityData ud = mock(UniversityData.class);

        // Timed out a long time ago, refresh now.
        when(ud.getTimeoutLong()).thenReturn(0L);

        when(managerMock.find(UniversityData.class, code)).thenReturn(ud);
        when(updateMock.updateUniversity(code)).thenReturn(ud);

        University res = testObject.getUniversityData(code);

        assertEquals(u, res);

        verify(ud).getTimeoutLong();

        verify(managerMock).find(UniversityData.class, code);
        verify(updateMock).updateUniversity(code);

        verifyNoMoreInteractions(updateMock, managerMock);
    }

    @Test
    public void testGetUniversityData_FindRecentTimedOut() throws Exception {
        String code = "10";

        University u = mock(University.class);
        UniversityData ud = mock(UniversityData.class);

        // Timed out recently
        when(ud.getTimeoutLong()).thenReturn(ago(10, TimeUnit.MINUTES));

        when(managerMock.find(UniversityData.class, code)).thenReturn(ud);

        University res = testObject.getUniversityData(code);

        assertEquals(u, res);

        verify(ud, atLeastOnce()).getTimeoutLong();

        ArgumentCaptor<Long> captor = forClass(Long.class);
        verify(ud).setTimeout(captor.capture());
        // New timeout is some time in the future.
        assertTrue(captor.getValue() > System.currentTimeMillis());

        verify(managerMock).find(UniversityData.class, code);
        verify(aysncUpdateMock).updateUniversity(code);

        verifyNoMoreInteractions(updateMock, managerMock);
    }

    @Test
    public void testGetUniversityData_FindNotTimedOut() throws Exception {
        String code = "10";

        University u = mock(University.class);
        UniversityData ud = mock(UniversityData.class);

        // Not timed out yet.
        when(ud.getTimeoutLong()).thenReturn(Long.MAX_VALUE);

        when(managerMock.find(UniversityData.class, code)).thenReturn(ud);

        University res = testObject.getUniversityData(code);

        assertEquals(u, res);

        verify(ud, atLeastOnce()).getTimeoutLong();

        verify(managerMock).find(UniversityData.class, code);

        verifyNoMoreInteractions(updateMock, managerMock);
    }

    @Test
    public void testGetUniversityData_FindNull_UpdateNull() throws Exception {
        String code = "10";

        when(managerMock.find(UniversityData.class, code)).thenReturn(null);
        when(updateMock.updateUniversity(code)).thenReturn(null);

        University res = testObject.getUniversityData(code);

        assertEquals(null, res);

        verify(managerMock).find(UniversityData.class, code);
        verify(updateMock).updateUniversity(code);

        verifyNoMoreInteractions(updateMock, managerMock);
    }

}
