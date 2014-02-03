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
package org.dlect.internal.data;

import java.util.Date;
import org.dlect.object.UniversitySupport;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class UniversityDataTest {

    @Test
    public void testExamplar() throws Exception {
        Object o = mock(Object.class);
        assertNotNull(o);
    }

    @Test
    public void testConstructor_1argCode() {
        String code = "10L";

        UniversityData data = new UniversityData(code);

        assertEquals(code, data.getCode());
    }

    @Test
    public void testConstructor_5arg() {
        String code = "10L";
        String name = "Hello";
        UniversitySupport support = UniversitySupport.HTTPS;
        String url = "Not A URL";
        long timeout = 788891243455678L;

        UniversityData data = new UniversityData(code, name, support, url, timeout);

        assertEquals(code, data.getCode());
        assertEquals(name, data.getName());
        assertEquals(support, data.getSupport());
        assertEquals(url, data.getUrl());
        assertEquals(timeout, data.getTimeoutLong());
        assertFalse(data.getRetry());
    }

    @Test
    public void testConstructor_4arg() {
        String code = "10L";
        String name = "Hi";
        UniversitySupport support = UniversitySupport.HTTP;
        String url = "No a URL";

        UniversityData data = new UniversityData(code, name, support, url);

        assertEquals(code, data.getCode());
        assertEquals(name, data.getName());
        assertEquals(support, data.getSupport());
        assertEquals(url, data.getUrl());
        assertEquals(0L, data.getTimeoutLong());
        assertFalse(data.getRetry());
    }

    @Test
    public void testConstructor_3arg() {
        String code = "10L";
        String name = "Hi";
        UniversitySupport support = UniversitySupport.HTTP;

        UniversityData data = new UniversityData(code, name, support);

        assertEquals(code, data.getCode());
        assertEquals(name, data.getName());
        assertEquals(support, data.getSupport());
        assertNull(data.getUrl());
        assertEquals(0L, data.getTimeoutLong());
        assertFalse(data.getRetry());
    }

    @Test
    public void testConstructor_1argUData_NoRetry() {
        String code = "10L";
        String name = "Hello";
        UniversitySupport support = UniversitySupport.HTTPS;
        String url = "Not A URL";
        Date timeout = new Date(788891243455678L);
        boolean retry = false;
        UniversityData old = mock(UniversityData.class);

        when(old.getCode()).thenReturn(code);
        when(old.getName()).thenReturn(name);
        when(old.getSupport()).thenReturn(support);
        when(old.getUrl()).thenReturn(url);
        when(old.getTimeout()).thenReturn(timeout);
        when(old.getRetry()).thenReturn(retry);

        UniversityData data = new UniversityData(old);

        assertEquals(code, data.getCode());
        assertEquals(name, data.getName());
        assertEquals(support, data.getSupport());
        assertEquals(url, data.getUrl());
        assertEquals(timeout, data.getTimeout());
        assertEquals(retry, data.getRetry());
    }

    @Test
    public void testConstructor_1argUData_Retry() {
        String code = "10L";
        String name = "Hello";
        UniversitySupport support = UniversitySupport.HTTPS;
        String url = "Not A URL";
        Date timeout = new Date(788891243455678L);
        boolean retry = true;
        UniversityData old = mock(UniversityData.class);

        when(old.getCode()).thenReturn(code);
        when(old.getName()).thenReturn(name);
        when(old.getSupport()).thenReturn(support);
        when(old.getUrl()).thenReturn(url);
        when(old.getTimeout()).thenReturn(timeout);
        when(old.getRetry()).thenReturn(retry);

        UniversityData data = new UniversityData(old);

        assertEquals(code, data.getCode());
        assertEquals(name, data.getName());
        assertEquals(support, data.getSupport());
        assertEquals(url, data.getUrl());
        assertEquals(timeout, data.getTimeout());
        assertEquals(retry, data.getRetry());

        verify(old).getCode();
        verify(old).getName();
        verify(old).getSupport();
        verify(old).getUrl();
        verify(old).getTimeout();
        verify(old).getRetry();
    }

    @Test
    public void testGetSetCode() {
        UniversityData data = new UniversityData();
        assertNull(data.getCode());

        String code = "10L";
        data.setCode(code);
        assertEquals(code, data.getCode());

        code = "103445L";
        data.setCode(code);
        assertEquals(code, data.getCode());
    }

    @Test
    public void testGetSetName() {
        UniversityData data = new UniversityData();
        assertNull(data.getName());

        String name = "Name1";
        data.setName(name);
        assertEquals(name, data.getName());

        name = "Name2 - Longer :D";
        data.setName(name);
        assertEquals(name, data.getName());
    }

    @Test
    public void testGetSetSupport() {
        UniversityData data = new UniversityData();
        assertNull(data.getSupport());

        UniversitySupport support = UniversitySupport.HTTP;
        data.setSupport(support);
        assertEquals(support, data.getSupport());

        support = UniversitySupport.HTTP_AUTH;
        data.setSupport(support);
        assertEquals(support, data.getSupport());
    }

    @Test
    public void testGetSetUrl() {
        UniversityData data = new UniversityData();
        assertNull(data.getUrl());

        String url = "ASDGakjhfaksdjhfalksdfjha Hi";
        data.setUrl(url);
        assertEquals(url, data.getUrl());

        url = "SDGSDFSDFSFSDf";
        data.setUrl(url);
        assertEquals(url, data.getUrl());
    }

    @Test
    public void testGetSetTimeout_Long() {
        UniversityData data = new UniversityData();
        assertEquals(0, data.getTimeoutLong());

        long timeout = 129423048234L;
        data.setTimeout(timeout);
        assertEquals(timeout, data.getTimeoutLong());

        timeout = 9089780897L;
        data.setTimeout(timeout);
        assertEquals(timeout, data.getTimeoutLong());
    }

    @Test
    public void testGetSetTimeout_Date() {
        UniversityData data = new UniversityData();

        Date date = null;
        data.setTimeout(date);
        assertEquals(new Date(0L), data.getTimeout());

        long timeout = 1000002L;
        date = new Date(timeout);
        data.setTimeout(date);
        assertEquals(date, data.getTimeout());
    }

    @Test
    public void testGetSetTimeout_Cross() {
        UniversityData data = new UniversityData();

        long timeout = 1000002L;
        Date date = new Date(timeout);
        data.setTimeout(date);
        assertEquals(timeout, data.getTimeoutLong());
        
        timeout = 12L;
        date = new Date(timeout);
        data.setTimeout(timeout);
        assertEquals(date, data.getTimeout());
    }

    @Test
    public void testGetSetRetries() {
        UniversityData data = new UniversityData();
        assertFalse(data.getRetry());

        boolean retry = true;
        data.setRetry(retry);
        assertEquals(retry, data.getRetry());

        retry = false;
        data.setRetry(retry);
        assertEquals(retry, data.getRetry());
    }

    @Test
    public void testEqualsString() {
        String code = "10L";
        String name1 = "Hello";
        UniversitySupport support1 = UniversitySupport.HTTPS;
        String url1 = "Not A URL";
        long timeout1 = 788891243455678L;

        UniversityData data1 = new UniversityData(code, name1, support1, url1, timeout1);

        assertFalse(data1.equals("String that is not data"));
    }

    @Test
    public void testEqualsOnIncomplete() {
        String code = "10L";
        String name1 = "Hello";
        UniversitySupport support1 = UniversitySupport.HTTPS;
        String url1 = "Not A URL";
        long timeout1 = 788891243455678L;

        UniversityData data1 = new UniversityData(code, name1, support1, url1, timeout1);
        UniversityData data2 = new UniversityData(null, name1, support1, url1, timeout1);

        assertFalse(data1.equals(data2));
    }

    @Test
    public void testEqualsOnlyOnCode() {
        String code = "10L";
        String name1 = "Hello";
        UniversitySupport support1 = UniversitySupport.HTTPS;
        String url1 = "Not A URL";
        long timeout1 = 788891243455678L;

        UniversityData data1 = new UniversityData(code, name1, support1, url1, timeout1);
        UniversityData data2 = new UniversityData(code, name1 + "Not Eq", UniversitySupport.NONE, url1 + "Not Eq", timeout1 + 10);

        assertTrue(data1.equals(data2));
    }

    @Test
    public void testHashCodeOnlyOnCode() {
        String code = "10L";
        String name1 = "Hello";
        UniversitySupport support1 = UniversitySupport.HTTPS;
        String url1 = "Not A URL";
        long timeout1 = 788891243455678L;

        UniversityData data1 = new UniversityData(code, name1, support1, url1, timeout1);
        UniversityData data2 = new UniversityData(code, name1 + "Not Eq", UniversitySupport.NONE, url1 + "Not Eq", timeout1 + 10);

        assertEquals(data1.hashCode(), data2.hashCode());
    }

    @Test
    public void testCompareToOnlyOnCode() {
        String code = "10L";
        String name1 = "Hello";
        UniversitySupport support1 = UniversitySupport.HTTPS;
        String url1 = "Not A URL";
        long timeout1 = 788891243455678L;

        UniversityData data1 = new UniversityData(code, name1, support1, url1, timeout1);
        UniversityData data2 = new UniversityData(code, name1 + "Not Eq", UniversitySupport.NONE, url1 + "Not Eq", timeout1 + 10);

        assertEquals(0, data1.compareTo(data2));
    }

    @Test
    public void testCompareToSupportsNull() {
        String code = "10L";
        String name1 = "Hello";
        UniversitySupport support1 = UniversitySupport.HTTPS;
        String url1 = "Not A URL";
        long timeout1 = 788891243455678L;

        UniversityData data1 = new UniversityData(code, name1, support1, url1, timeout1);

        assertTrue(data1.compareTo(null) > 0);
    }

    @Test
    public void testCompareToSupportsNullCode() {
        String code = "10L";
        String name1 = "Hello";
        UniversitySupport support1 = UniversitySupport.HTTPS;
        String url1 = "Not A URL";
        long timeout1 = 788891243455678L;

        UniversityData data1 = new UniversityData(code, name1, support1, url1, timeout1);
        UniversityData data2 = new UniversityData(null, name1, support1, url1, timeout1);

        // Nulls should be the smallest
        assertTrue(data1.compareTo(data2) + " > 0", data1.compareTo(data2) > 0);
    }

    @Test
    public void testCompareToSupportsNullCodeBackwards() {
        String code = "10L";
        String name1 = "Hello";
        UniversitySupport support1 = UniversitySupport.HTTPS;
        String url1 = "Not A URL";
        long timeout1 = 788891243455678L;

        UniversityData data1 = new UniversityData(null, name1, support1, url1, timeout1);
        UniversityData data2 = new UniversityData(code, name1, support1, url1, timeout1);

        // Nulls should be the smallest
        assertTrue(data1.compareTo(data2) + " > 0", data1.compareTo(data2) < 0);
    }

    @Test
    public void testCompareTo() {
        String code = "10L";
        String name1 = "Hello";
        UniversitySupport support1 = UniversitySupport.HTTPS;
        String url1 = "Not A URL";
        long timeout1 = 788891243455678L;

        UniversityData data1 = new UniversityData(code, name1, support1, url1, timeout1);
        UniversityData data2 = new UniversityData(code + "??", name1, support1, url1, timeout1);

        assertTrue(data1.compareTo(data2) < 0);
    }
    // TODO MOVE THIS TEST.

    @Ignore
    @Test
    public void testSetTo() {
        String code = "10L";
        String code2 = code + "123";
        String name = "Hello";
        UniversitySupport support = UniversitySupport.HTTPS;
        String url = "Not A URL";
        long timeout = 788891243455678L;
        boolean retry = true;

        UniversityData oldData = new UniversityData(code, name, support, url, timeout);
        oldData.setRetry(retry);
        // Different Code
        UniversityData data = new UniversityData(code2, name + "Fail", UniversitySupport.NONE, url + "Fail", -1);

        // TODO Uncomment this line.
        //data.setTo(oldData);
        // Check it hasn't changed the code.
        assertEquals(code2, data.getCode());
        assertEquals(name, data.getName());
        assertEquals(support, data.getSupport());
        assertEquals(url, data.getUrl());
        assertEquals(timeout, data.getTimeout());
        assertEquals(retry, data.getRetry());

    }

    @Test
    public void testExportTo() {
        // TODO write test.
    }
}
