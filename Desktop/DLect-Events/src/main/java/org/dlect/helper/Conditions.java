/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helper;

/**
 * An extension of {@link com.google.common.base.Preconditions }.
 *
 * @author lee
 */
public class Conditions {

    public static void checkNonNull(Object check, String varName) {
        if (check == null) {
            throw new IllegalArgumentException(varName + " should not be null");
        }
    }

    public static void checkNull(Object check, String varName) {
        if (check != null) {
            throw new IllegalArgumentException(varName + " should be null");
        }
    }

}
