/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author lee
 */
public class I18N {

    private static final Object LOCK = new Object();
    private static volatile ResourceBundle bundle = null;

    private static ResourceBundle ensureReference() {
        ResourceBundle tmp = bundle;
        if (tmp == null) {
            tmp = createBundle();
        }
        return tmp;
    }

    /**
     * @param s The Resource Bundle key to get. This parameter is converted to
     *          uppercase before the bundle is queried.
     *
     * @return
     *
     * @throws MissingResourceException when the key is not found
     */
    public static String getString(String s) throws MissingResourceException {
        return ensureReference().getString(s.toUpperCase());
    }

    public static String getCheckedString(String s) throws Exception {
        try {
            return getString(s);
        } catch (MissingResourceException ex) {
            ExceptionReporter.reportException(ex, "The key requested was: \"" + s + "\"");
            throw new Exception("The key was not found: \"" + s + "\"", ex);
        }
    }

    private static ResourceBundle createBundle() {
        synchronized (LOCK) {
            if (bundle == null) {
                bundle = ResourceBundle.getBundle("org/lee/echo360/bundle");
            }
        }
        return bundle;
    }

    private I18N() {
    }
}
