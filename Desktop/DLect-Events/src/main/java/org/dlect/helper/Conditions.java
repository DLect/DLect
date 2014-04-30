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

    public static <T> T checkNonNull(T check, String varName) {
        if (check == null) {
            throw new IllegalArgumentException(varName + " should not be null");
        }
        return check;
    }

    public static void checkTrue(boolean check, String desc) {
        if (!check) {
            throw new IllegalArgumentException(desc == null ? "Condition was false" : desc);
        }
    }

    public static void checkFalse(boolean check, String desc) {
        if (!check) {
            throw new IllegalArgumentException(desc == null ? "Condition was false" : desc);
        }
    }

    public static void checkNull(Object check, String varName) {
        if (check != null) {
            throw new IllegalArgumentException(varName + " should be null");
        }
    }

}
