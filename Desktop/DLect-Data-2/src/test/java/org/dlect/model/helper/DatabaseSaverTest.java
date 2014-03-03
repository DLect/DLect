/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.helper;

import org.dlect.logging.TestLogging;
import org.dlect.model.Database;
import org.dlect.model.Semester;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author lee
 */
@SuppressWarnings("unchecked")
public class DatabaseSaverTest {

    /**
     * Test of save method, of class DatabaseSaver.
     */
    @Test
    @Ignore
    public void testSave_Database() throws Exception {
        Database d = new Database();

        d.addSetting("username", "testUsername");
        d.addSetting("password", "testPassword");

        Semester s = new Semester();
        s.setCoursePostfixName("Sem 1");
        s.setLongName("Semester 1");
        s.setNum(1);

        d.addSemester(s);

        String databaseXml = DatabaseSaver.save(d);

        TestLogging.LOG.error(databaseXml);
    }

    /**
     * Test of save method, of class DatabaseSaver.
     */
    @Ignore
    @Test
    public void testSave_Database_OutputStream() throws Exception {
    }

    /**
     * Test of save method, of class DatabaseSaver.
     */
    @Ignore
    @Test
    public void testSave_Database_File() throws Exception {
    }

    /**
     * Test of save method, of class DatabaseSaver.
     */
    @Ignore
    @Test
    public void testSave_Database_Writer() throws Exception {
    }

}
