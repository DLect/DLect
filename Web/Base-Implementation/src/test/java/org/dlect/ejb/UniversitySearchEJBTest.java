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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Set;
import org.dlect.ejb.internal.uni.search.UniversitySearchImplEJBLocal;
import org.dlect.export.University;
import org.dlect.internal.data.UniversityData;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
@Ignore
public class UniversitySearchEJBTest {

    // TODO Test with powermock.
    
    @Mock
    private UniversitySearchImplEJBLocal searchImplMock;
    @Mock
    private UniversityDatabaseEJBLocal databaseMock;

    @InjectMocks
    private UniversitySearchEJB testObject;

    public UniversitySearchEJBTest() {
    }

    @Test
    public void testGetUniversitiesMatching_NoMatches() throws Exception {
        String term = "Test Term";

        when(searchImplMock.getUniversitiesMatching(term)).thenReturn(ImmutableSortedSet.<UniversityData>of());

        Set<University> res = testObject.getUniversitiesMatching(term);

        assertNotNull(res);
        assertTrue(res.isEmpty());

        verify(searchImplMock).getUniversitiesMatching(term);

        verifyNoMoreInteractions(searchImplMock);
    }

    @Test
    public void testGetUniversitiesMatching_TwoMatches() throws Exception {
        String term = "Test Term";

        UniversityData od1 = mock(UniversityData.class);
        UniversityData d1 = mock(UniversityData.class);
        University u1 = mock(University.class);

        when(databaseMock.updateUniversityInDatabase(od1)).thenReturn(d1);

        UniversityData od2 = mock(UniversityData.class);
        UniversityData d2 = mock(UniversityData.class);
        University u2 = mock(University.class);

        when(databaseMock.updateUniversityInDatabase(od2)).thenReturn(d2);

        when(searchImplMock.getUniversitiesMatching(term)).thenReturn(ImmutableSet.of(od1, od2));

        Set<University> res = testObject.getUniversitiesMatching(term);

        assertNotNull(res);
        assertEquals(2, res.size());

        verify(databaseMock).updateUniversityInDatabase(od1);
        assertTrue(res.contains(u1));

        verify(databaseMock).updateUniversityInDatabase(od2);
        assertTrue(res.contains(u2));

        verifyNoMoreInteractions(od1, d1, u1, od2, d2, u2);

        verify(searchImplMock).getUniversitiesMatching(term);

        verifyNoMoreInteractions(searchImplMock);
    }
}
