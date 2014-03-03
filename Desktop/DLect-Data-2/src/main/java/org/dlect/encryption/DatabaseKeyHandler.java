/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.encryption;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.dlect.model.Database;

/**
 *
 * @author lee
 */
public class DatabaseKeyHandler {

    private final Key aesKey;

    private DatabaseKeyHandler(Key aesKey) {
        this.aesKey = aesKey;
        try {
            getDecryptingCipher();
            getEncryptingCipher();
        } catch (GeneralSecurityException ex) {
            EncryptionLogger.LOGGER.error("INVALID KEY: " + aesKey, ex);
        }
    }

    public Cipher getDecryptingCipher() throws GeneralSecurityException {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, aesKey);
        return c;
    }

    public String getEncryptedPrefix() {
        StringBuilder b = new StringBuilder("ENC::");
        b.append(getKeyHash());
        b.append("::");
        return b.toString();
    }

    public Cipher getEncryptingCipher() throws GeneralSecurityException {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, aesKey);
        return c;
    }

    public String getKeyHash() {
        byte[] keyBytes = aesKey.getEncoded();
        BigInteger bi = new BigInteger(1, keyBytes);
        return bi.toString(16);
    }

    public Optional<String> verifyEncrypted(String encSettingValue) {
        String prefix = getEncryptedPrefix();

        if (encSettingValue.startsWith(prefix)) {
            return Optional.of(encSettingValue.substring(prefix.length()));
        } else {
            return Optional.absent();
        }
    }

    public static DatabaseKeyHandler getKeyHandler(Database db) {
        String keyString = db.getSetting(AES_KEY_SETTING_NAME);
        byte[] key = null;
        if (keyString != null) {
            byte[] decodedKey = BytesToString.decode(keyString);
            if (decodedKey.length == 16) {
                key = decodedKey;
            }
        }
        if (key == null) {
            key = newKey();
            db.addSetting(AES_KEY_SETTING_NAME, BytesToString.encode(key));
        }

        Key aesKey = new SecretKeySpec(key, "AES");
        return new DatabaseKeyHandler(aesKey);
    }
    private static final String AES_KEY_SETTING_NAME = "AES-Key";

    private static byte[] newKey() {
        byte[] bytes = new SecureRandom().generateSeed(16);
        return bytes;
    }

}
