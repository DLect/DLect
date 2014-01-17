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

import java.util.concurrent.Future;
import org.dlect.internal.data.UniversityData;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.dlect.test.helpers.TestHelpers.IO_EXCEPTION;
import static org.dlect.test.helpers.TestHelpers.NO_SUPPORT_EXCEPTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
public class AsynchronousUniversityUpdateEJBTest {

    // TODO - re-write tests.
    
    @Mock
    private UniversityUpdateEJB updateMock;
    @InjectMocks
    private AsynchronousUniversityUpdateEJB testObject;

    public AsynchronousUniversityUpdateEJBTest() {
    }

    @Test
    public void testUpdateUniversity_Good() throws Exception {
        String code = "10";
        UniversityData exp = mock(UniversityData.class);

        when(updateMock.updateUniversity(code)).thenReturn(exp);

        Future<UniversityData> res = testObject.updateUniversity(code);

        assertNotNull(res);
        assertEquals(exp, res.get());

        verify(updateMock).updateUniversity(code);
    }

    @Test
    public void testUpdateUniversity_IOException() throws Exception {
        String code = "10";

        when(updateMock.updateUniversity(code)).thenThrow(IO_EXCEPTION);

        Future<UniversityData> res = testObject.updateUniversity(code);

        assertNull(res);

        verify(updateMock).updateUniversity(code);
    }

    @Test
    public void testUpdateUniversity_NoProviders() throws Exception {
        String code = "10";

        when(updateMock.updateUniversity(code)).thenThrow(NO_SUPPORT_EXCEPTION);

        Future<UniversityData> res = testObject.updateUniversity(code);

        assertNull(res);

        verify(updateMock).updateUniversity(code);
    }

}
