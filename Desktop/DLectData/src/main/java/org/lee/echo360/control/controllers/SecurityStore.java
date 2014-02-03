/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author Lee Symes
 */
public final class SecurityStore {

    private static final byte[] aesKey = new byte[]{
        0x56, 0x5e, 0x57, (byte) 0xec, 0x09, 0x31, (byte) 0x9a, 0x49, 0x3a, (byte) 0xaf, 0x7d, 0x3a, 0x6f, (byte) 0xe9, 0x31, (byte) 0xb0};

    protected static InputStream getCipherInputStream(File f) throws FileNotFoundException {
        return getCipherInputStream(new FileInputStream(f));
    }

    protected static InputStream getCipherInputStream(InputStream s) {
        return new CipherInputStream(s, SecurityStore.getCipher(Cipher.DECRYPT_MODE));
    }

    protected static OutputStream getCipherOutputStream(File f) throws FileNotFoundException {
        return getCipherOutputStream(new FileOutputStream(f));
    }

    protected static OutputStream getCipherOutputStream(OutputStream o) {
        return new CipherOutputStream(o, getCipher(Cipher.ENCRYPT_MODE));
    }

    protected static Cipher getCipher(int type) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(aesKey, "AES");
            cipher.init(type, secretKey);
        } catch (InvalidKeyException ex) {
            ExceptionReporter.reportException(ex);
        } catch (NoSuchPaddingException ex) {
            ExceptionReporter.reportException(ex);
        } catch (NoSuchAlgorithmException ex) {
            ExceptionReporter.reportException(ex);
        }
        return cipher;
    }

    private SecurityStore() {
    }
}
