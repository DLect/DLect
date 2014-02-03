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

import org.dlect.ejb.internal.uni.listing.UniversityDataEJBLocal;
import org.dlect.internal.data.UniversityData;
import org.dlect.lock.EquivalenceLock;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
@Ignore
public class UniversityUpdateEJBTest {

    //TODO re-write tests
    
    @Mock
    private UniversityDataEJBLocal listings;
    @Mock
    private UniversityDatabaseEJBLocal database;
    @Mock
    private EquivalenceLock<String> lock;

    @InjectMocks
    private UniversityUpdateEJB testObject;

    @Test
    public void testUpdateUniversity() throws Exception {
        String code = "10";
        UniversityData lstU = mock(UniversityData.class);
        UniversityData dbU = mock(UniversityData.class);
        when(listings.getUniversityForCode(code)).thenReturn(lstU);
        when(database.updateUniversityInDatabase(lstU)).thenReturn(dbU);
        
        UniversityData ret = testObject.updateUniversity(code);
        
        assertEquals(dbU, ret);
        
        verify(listings).getUniversityForCode(code);
        verify(database).updateUniversityInDatabase(lstU);
        
        verify(lock).release(code);
        
    }

}
