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

import javax.persistence.EntityManager;
import org.dlect.internal.data.UniversityData;
import org.dlect.object.UniversitySupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
@Ignore
public class UniversityDatabaseEJBTest {

    // TODO re-write these.
    
    @Mock
    private EntityManager manager;

    @Mock
    private UniversityData preClean;
    @Mock
    private UniversityData postClean;
    @Mock
    private UniversityData database;

    @InjectMocks
    private UniversityDatabaseEJB testObject;

    private final String code = "10";

    @Before
    public void initCleanerAndManager() {
        //when(helper.clean(preClean)).thenReturn(postClean);
        when(postClean.getCode()).thenReturn(code);
    }

    @Test
    public void testUpdateUniversityInDatabase_NullFromDB() throws Exception {

        when(manager.find(UniversityData.class, code)).thenReturn(null);

        when(postClean.getSupport()).thenReturn(UniversitySupport.NONE);

        UniversityData res = testObject.updateUniversityInDatabase(preClean);
        assertEquals(postClean, res);

        verify(manager).find(UniversityData.class, code);
        verify(manager).persist(postClean);
        verifyNoMoreInteractions(manager);

        verify(postClean, atLeastOnce()).getSupport();

        ArgumentCaptor<Long> captor = forClass(Long.class);
        verify(postClean).setTimeout(captor.capture());
        assertTrue(captor.getValue() > System.currentTimeMillis());
    }

    @Test
    public void testUpdateUniversityInDatabase_Regression_NoRetry() throws Exception {

        when(manager.find(UniversityData.class, code)).thenReturn(database);

        when(postClean.getSupport()).thenReturn(UniversitySupport.NONE);

        when(database.getSupport()).thenReturn(UniversitySupport.HTTPS);
        when(database.getRetry()).thenReturn(false);

        UniversityData res = testObject.updateUniversityInDatabase(preClean);
        assertEquals(database, res);

        verify(manager).find(UniversityData.class, code);
        verifyNoMoreInteractions(manager);

        verify(postClean, atLeastOnce()).getSupport();

        verify(database).getSupport();
        verify(database).getRetry();
        verify(database).setRetry(true);

        ArgumentCaptor<Long> captor = forClass(Long.class);
        verify(database).setTimeout(captor.capture());
        assertTrue(captor.getValue() > System.currentTimeMillis());
    }

    @Test
    public void testUpdateUniversityInDatabase_NoSupport_NoRetry() throws Exception {

        when(manager.find(UniversityData.class, code)).thenReturn(database);

        when(postClean.getSupport()).thenReturn(UniversitySupport.NONE);

        when(database.getSupport()).thenReturn(UniversitySupport.NONE);
        when(database.getRetry()).thenReturn(false);

        UniversityData res = testObject.updateUniversityInDatabase(preClean);
        assertEquals(database, res);

        verify(manager).find(UniversityData.class, code);
        verifyNoMoreInteractions(manager);

        verify(postClean, atLeastOnce()).getSupport();

        verify(database).getSupport();
        verify(database).setRetry(false);
        //verify(helper).setTo(database, postClean);

        ArgumentCaptor<Long> captor = forClass(Long.class);
        verify(database).setTimeout(captor.capture());
        assertTrue(captor.getValue() > System.currentTimeMillis());
    }

    @Test
    public void testUpdateUniversityInDatabase_Support_NoRetry() throws Exception {

        when(manager.find(UniversityData.class, code)).thenReturn(database);

        when(postClean.getSupport()).thenReturn(UniversitySupport.HTTPS);

        when(database.getSupport()).thenReturn(UniversitySupport.HTTPS);
        when(database.getRetry()).thenReturn(false);

        UniversityData res = testObject.updateUniversityInDatabase(preClean);
        assertEquals(database, res);

        verify(manager).find(UniversityData.class, code);
        verifyNoMoreInteractions(manager);

        verify(postClean, atLeastOnce()).getSupport();

        verify(database).setRetry(false);
        //verify(helper).setTo(database, postClean);

        ArgumentCaptor<Long> captor = forClass(Long.class);
        verify(database).setTimeout(captor.capture());
        assertTrue(captor.getValue() > System.currentTimeMillis());
    }

    @Test
    public void testUpdateUniversityInDatabase_Regression_Retry() throws Exception {

        when(manager.find(UniversityData.class, code)).thenReturn(database);

        when(postClean.getSupport()).thenReturn(UniversitySupport.NONE);

        when(database.getSupport()).thenReturn(UniversitySupport.HTTPS);
        when(database.getRetry()).thenReturn(true);

        UniversityData res = testObject.updateUniversityInDatabase(preClean);
        assertEquals(database, res);

        verify(manager).find(UniversityData.class, code);
        verifyNoMoreInteractions(manager);

        verify(postClean, atLeastOnce()).getSupport();

        verify(database).getSupport();
        verify(database).setRetry(false);
        //verify(helper).setTo(database, postClean);

        ArgumentCaptor<Long> captor = forClass(Long.class);
        verify(database).setTimeout(captor.capture());
        assertTrue(captor.getValue() > System.currentTimeMillis());
    }
}
