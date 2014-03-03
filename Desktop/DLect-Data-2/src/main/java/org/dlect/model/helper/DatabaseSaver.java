/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.helper;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.dlect.model.Database;

/**
 *
 * @author lee
 */
public class DatabaseSaver {

    public static String save(Database db) throws JAXBException {
        StringWriter w = new StringWriter();

        save(db, w);

        return w.getBuffer().toString();
    }

    public static void save(Database db, OutputStream read) throws JAXBException {
        save(db, new OutputStreamWriter(read, Charsets.UTF_8));
    }

    public static void save(Database db, File file) throws JAXBException, FileNotFoundException {
        save(db, new FileOutputStream(file));
    }

    public static void save(Database db, Writer writer) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Database.class);

        Marshaller m = jc.createMarshaller();

        m.marshal(db, writer);
    }

    private DatabaseSaver() {
    }

}
