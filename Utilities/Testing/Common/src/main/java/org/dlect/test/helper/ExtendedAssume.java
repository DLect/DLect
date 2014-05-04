/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.test.helper;

import java.io.IOException;
import java.net.URI;
import org.junit.Assume;

/**
 *
 * @author lee
 */
public class ExtendedAssume extends Assume {
    
    public static void assumeConnection(String toURI) {
        try {
            URI.create(toURI).toURL().openStream();
        } catch(IOException e) {
            assumeNoException("Unable to connect to [\"" + toURI + "\"]", e);
        } 
    }
    
}
