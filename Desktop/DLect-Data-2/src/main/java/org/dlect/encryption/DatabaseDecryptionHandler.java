/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.encryption;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import java.security.GeneralSecurityException;
import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import org.dlect.model.Database;

/**
 *
 * @author lee
 */
public class DatabaseDecryptionHandler {

    private final Database db;
    private final DatabaseKeyHandler key;

    public DatabaseDecryptionHandler(Database db) {
        this.db = db;
        this.key = DatabaseKeyHandler.getKeyHandler(db);
    }

    @Nonnull
    public Optional<String> getEncryptedSetting(String settingKey) {
        String encSettingValue = db.getSetting(settingKey);
        if (encSettingValue == null) {
            return Optional.absent();
        }

        Optional<String> encVal = key.verifyEncrypted(encSettingValue);

        if (!encVal.isPresent()) {
            return Optional.absent();
        }

        try {
            Cipher dc = key.getDecryptingCipher();

            byte[] decoded = dc.doFinal(BytesToString.decode(encVal.get()));

            return Optional.of(new String(decoded, Charsets.UTF_8));
        } catch (GeneralSecurityException ex) {
            EncryptionLogger.LOGGER.error("Failed to decrypt value for key " + settingKey, ex);
            return Optional.absent();
        }

    }

}
