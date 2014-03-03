/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.slf4j.Logger;

/**
 *
 * @author lee
 */
public class FileDebuggingHelper {
    
    private FileDebuggingHelper() {
    }
    
    public static void debugFileToLogger(File f, Logger l) {
        try {
            debugReaderToLogger(new FileReader(f), f.getName(), l);
        } catch (FileNotFoundException ex) {
            l.error("Failed to load file " + f, ex);
        }
    }
    
    public static void debugStringToLogger(String fileContent, String name, Logger l) {
        debugReaderToLogger(new StringReader(fileContent), name, l);
    }
    
    public static void debugReaderToLogger(Reader r, String name, Logger l) {
        String format = name + "| Ln {}: {}";
        int lineNo = 0;
        try (BufferedReader br = new BufferedReader(r)) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNo++;
                l.error(format, lineNo, line);
            }
        } catch (IOException ex) {
            l.error("IOException thrown at line " + lineNo, ex);
        }
    }
    
    public static void debugFolderToLogger(File folder, Logger l) {
        
    }
    
}
