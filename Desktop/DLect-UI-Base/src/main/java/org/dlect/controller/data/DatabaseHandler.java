/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.data;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.dlect.controller.helper.Initilisable;
import org.dlect.encryption.DatabaseDecryptionHandler;
import org.dlect.encryption.DatabaseEncryptionHandler;
import org.dlect.events.EventID;
import org.dlect.events.listenable.Listenable;
import org.dlect.helper.JavaHelper;
import org.dlect.helpers.FileDebuggingHelper;
import org.dlect.model.Database;
import org.dlect.model.helper.DatabaseLoader;
import org.dlect.model.helper.DatabaseSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lee
 */
public class DatabaseHandler extends Listenable<DatabaseHandler> implements Initilisable {

    public static final String DLECT_FOLDER_NAME = ".DLect";
    public static final String DLECT_DATABASE_NAME = "DLect-data.xml";

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);

    private Database database;
    private DatabaseEncryptionHandler encryptionHandler;
    private DatabaseDecryptionHandler decryptionHandler;

    private final Map<String, String> temporaryDatabaseSettings = Maps.newHashMap();

    @Override
    public void init() {
        loadDatabase();
    }

    public void loadDatabase() {
        File dataFile = getDatabaseFile();

        database = loadDatabase(dataFile);

        decryptionHandler = new DatabaseDecryptionHandler(database);
        encryptionHandler = new DatabaseEncryptionHandler(database);

        addChild(database);
        event(DatabaseHandlerEventID.DATABASE_LOADED).before(null).after(database).fire();
    }

    protected File getDatabaseFile() {
        File jarFolder = JavaHelper.getJarFile().getParentFile();
        File dataFolder = new File(jarFolder, DLECT_FOLDER_NAME);
        File dataFile = new File(dataFolder, DLECT_DATABASE_NAME);
        return dataFile;
    }

    protected Database loadDatabase(File dataFile) {
        if (dataFile.exists()) {
            try {
                // TODO load db from dataFile.
                return DatabaseLoader.load(dataFile);
            } catch (JAXBException | FileNotFoundException ex) {
                LOGGER.error("Failed to load database from file(" + dataFile + "). File Follows.", ex);

                FileDebuggingHelper.debugFileToLogger(dataFile, LOGGER);
                return backupLoadDatabase(dataFile);
            }
        } else {
            if (!dataFile.getParentFile().exists()) {
                dataFile.getParentFile().mkdirs();
            }
            return backupLoadDatabase(dataFile);
        }

    }

    protected Database backupLoadDatabase(File dataFile) {
        Database db = new Database();

        saveDatabase(db, dataFile);

        return db;
    }

    public void removeSetting(String key) {
        String tmpVal = temporaryDatabaseSettings.remove(key);
        if (tmpVal == null) {
            database.removeSetting(key);
        }
    }

    public void removeTemporarySetting(String key) {
        temporaryDatabaseSettings.remove(key);
    }

    public void saveDatabase() {
        if (database != null) {
            saveDatabase(database, getDatabaseFile());
            event(DatabaseHandlerEventID.DATABASE_SAVED).before(database).after(null).fire();
        }
    }

    protected void saveDatabase(Database db, File dataFile) {
        try {
            DatabaseSaver.save(db, dataFile);
        } catch (JAXBException ex) {
            LOGGER.error("Error exporting database to XML" + db, ex);
        } catch (FileNotFoundException ex) {
            LOGGER.error("Error saving database to file. Expected Output Follows", ex);
            try {
                FileDebuggingHelper.debugStringToLogger(DatabaseSaver.save(db), "Expected", LOGGER);
            } catch (JAXBException ex1) {
                LOGGER.error("Error exporting database!!" + db, ex1);
            }
        }
    }

    public Database getDatabase() {
        return database;
    }

    public void addSetting(String key, String value) {
        database.addSetting(key, value);
    }

    public String getSetting(String key) {
        String tmpVal = temporaryDatabaseSettings.get(key);
        if (tmpVal == null) {
            return database.getSetting(key);
        }
        return tmpVal;
    }

    public void addTemporarySetting(String key, String value) {
        temporaryDatabaseSettings.put(key, value);
    }

    public boolean isTemporarySetting(String key) {
        return temporaryDatabaseSettings.get(key) != null;
    }

    public Optional<String> getEncryptedSetting(String settingKey) {
        return decryptionHandler.getEncryptedSetting(settingKey);
    }

    public void addEncryptedSetting(String settingKey, String plaintextValue) {
        encryptionHandler.setEncryptedSetting(settingKey, plaintextValue);
    }

    public static enum DatabaseHandlerEventID implements EventID {

        DATABASE_LOADED, DATABASE_SAVED;

        @Override
        public Class<?> getAppliedClass() {
            return DatabaseHandler.class;
        }

        @Override
        public String getName() {
            return name();
        }

    }

}
