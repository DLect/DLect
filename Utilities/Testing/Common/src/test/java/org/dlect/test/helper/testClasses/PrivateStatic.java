/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.test.helper.testClasses;

/**
 *
 * @author lee
 */
public class PrivateStatic {

    private static String field;

    public static String getField() {
        return field;
    }

    public static void changeField() {
        PrivateStatic.field += " - " + System.nanoTime();
    }

}
