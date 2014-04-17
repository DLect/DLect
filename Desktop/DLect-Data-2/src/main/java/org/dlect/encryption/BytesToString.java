/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.encryption;

import java.math.BigInteger;

/**
 *
 * @author lee
 */
public class BytesToString {

    private static final int RADIX = 36;

    public static String encode(byte[] bytes) {
        return new BigInteger(bytes).toString(RADIX);
    }

    public static byte[] decode(String string) {
        return new BigInteger(string, RADIX).toByteArray();
    }

}
