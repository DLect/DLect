/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.helper;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import org.dlect.logging.TestLogging;
import org.dlect.model.Database;
import org.dlect.model.Semester;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
@SuppressWarnings("unchecked")
public class DatabaseLoaderTest {

    /**
     * Test of load method, of class DatabaseLoader.
     */
    @Test
    public void testLoad_String() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><dlect><semesters><semester><number>1</number><name>Semester 1</name><prefix>Sem 1</prefix><subjects/></semester></semesters><settings><entry><key>username</key><value>testUsername</value></entry><entry><key>password</key><value>testPassword</value></entry></settings></dlect>";

        Database d = DatabaseLoader.load(xml);

        TestLogging.LOG.error("{}", d);

        Map<String, String> settings = Maps.newHashMap();
        settings.put("username", "testUsername");
        settings.put("password", "testPassword");

        assertEquals(settings, d.getSettings());
        Set<Semester> semSet = d.getSemesters();

        assertEquals(1, semSet.size());
        Semester s = semSet.iterator().next();

        assertEquals(1, s.getNum());
        assertEquals("Semester 1", s.getLongName());
        assertEquals("Sem 1", s.getCoursePostfixName());
        assertEquals(0, s.getSubjects().size());
    }

    /**
     * Test of load method, of class DatabaseLoader.
     */
    @Ignore
    @Test
    public void testLoad_InputStream() throws Exception {
    }

    /**
     * Test of load method, of class DatabaseLoader.
     */
    @Ignore
    @Test
    public void testLoad_File() throws Exception {
    }

    /**
     * Test of load method, of class DatabaseLoader.
     */
    @Ignore
    @Test
    public void testLoad_Reader() throws Exception {
    }

}
