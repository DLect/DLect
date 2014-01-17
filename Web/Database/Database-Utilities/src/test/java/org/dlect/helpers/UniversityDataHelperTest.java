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
package org.dlect.helpers;

import org.dlect.internal.data.UniversityData;
import org.dlect.object.UniversitySupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class UniversityDataHelperTest {

    @Mock
    private UniversityData data;

    @InjectMocks
    private UniversityDataHelper testObject;

    /**
     * Test of clean method, of class UniversityCleaner.
     */
    @Test
    public void testClean_Null() {
        try {
            testObject.clean(null);
            fail("No Exception Thrown.");
        } catch (IllegalArgumentException | NullPointerException ex) {
            // Pass :D
        }
    }

    @Test
    public void testClean_CodeNull() {
        when(data.getCode()).thenReturn(null);
        try {
            testObject.clean(data);
            fail("No Exception thrown for un-recoverable problem.");
        } catch (RuntimeException e) {
            // No Op
        }
        verify(data).getCode();
        verifyNoMoreInteractions(data);
    }

    @Test
    public void testClean_NameNull_SupportNull() {
        when(data.getCode()).thenReturn("0");
        when(data.getName()).thenReturn(null);
        when(data.getSupport()).thenReturn(null);

        UniversityData clean = testObject.clean(data);
        assertSame(clean, data);

        verify(data).getCode();
        verify(data).getName();
        verify(data).getSupport();
        verify(data).setName(anyString());
        verify(data).setSupport(UniversitySupport.NONE);
        verify(data).setRetry(false);
        verifyNoMoreInteractions(data);
    }

    @Test
    public void testClean_AllValid() {
        when(data.getCode()).thenReturn("0");
        when(data.getName()).thenReturn("??");
        when(data.getSupport()).thenReturn(UniversitySupport.HTTPS);

        UniversityData clean = testObject.clean(data);
        assertSame(clean, data);

        verify(data).getCode();
        verify(data).getName();
        verify(data).getSupport();
        verify(data).setRetry(false);
        verifyNoMoreInteractions(data);
    }

}
