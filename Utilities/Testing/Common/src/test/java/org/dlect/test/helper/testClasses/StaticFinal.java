/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.test.helper.testClasses;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.dlect.test.helper.ObjectHelper.setField;

/**
 *
 * @author lee
 */
public class StaticFinal {

    public static final String finalField = "Final Field";

    public static String nonFinalField = "Non-final field";

    public static void changeFinalField() throws Exception {
        Field ff = StaticFinal.class.getDeclaredField("finalField");
        int modifiers = ff.getModifiers();

        // See: http://stackoverflow.com/a/3301720/369021 - Crazy person!
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        setField(modifiersField, ff, modifiers & ~Modifier.FINAL);
        try {
            setField(ff, null, "Final Field - " + System.nanoTime());
        } finally {
            setField(modifiersField, ff, modifiers);
        }

    }

}
