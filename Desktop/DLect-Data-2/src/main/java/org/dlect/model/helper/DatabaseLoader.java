/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.helper;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.dlect.model.Database;

/**
 *
 * @author lee
 */
public class DatabaseLoader {

    public static Database load(String xml) throws JAXBException {
        return load(new StringReader(xml));
    }

    public static Database load(InputStream read) throws JAXBException {
        return load(new InputStreamReader(read, Charsets.UTF_8));
    }

    public static Database load(File file) throws JAXBException, FileNotFoundException {
        return load(new FileInputStream(file));
    }

    public static Database load(Reader read) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Database.class);

        Unmarshaller m = jc.createUnmarshaller();

        Object unmarshalled = m.unmarshal(read);

        if (unmarshalled instanceof Database) {
            return (Database) unmarshalled;
        } else {
            throw new JAXBException("Unmarshalled object was not a Database");
        }
    }

    private DatabaseLoader() {
    }

}
