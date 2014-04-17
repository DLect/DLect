/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.encryption;

import com.google.common.base.Charsets;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import org.dlect.model.Database;

/**
 *
 * @author lee
 */
public class DatabaseEncryptionHandler {

    private final Database db;
    private final DatabaseKeyHandler key;

    public DatabaseEncryptionHandler(Database db) {
        this.db = db;
        this.key = DatabaseKeyHandler.getKeyHandler(db);
    }

    /**
     * If the given value is null then then no changes are made and false is returned.
     *
     * @param settingKey
     * @param plainValue
     *
     * @return {@code true} if the setting was correctly encrypted and stored.
     */
    public boolean setEncryptedSetting(String settingKey, String plainValue) {
        if (plainValue == null) {
            return false;
        }

        try {
            Cipher dc = key.getEncryptingCipher();

            byte[] encoded = dc.doFinal(plainValue.getBytes(Charsets.UTF_8));

            String value = key.getEncryptedPrefix() + BytesToString.encode(encoded);

            db.addSetting(settingKey, value);
            return true;
        } catch (GeneralSecurityException ex) {
            EncryptionLogger.LOGGER.error("Failed to encrypt value for key " + settingKey, ex);
            return false;
        }

    }

}
