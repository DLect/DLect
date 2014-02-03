/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.UpdateStyle;
import org.lee.echo360.providers.BlackboardProviders;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class PropertiesSavingController {

    public static final ReentrantLock SAVE_LOAD_LOCK = new ReentrantLock(true);
    private static final String UPDATE_STYLE_PROPERTY = "update-style";
    private static final String DATA_DIRECTORY_PROPERTY = "data-directory";
    public static final File prefFolder = FileUtils.getFile(FileUtils.getUserDirectory(), ".DLect");
    public static final File prefDataFile = FileUtils.getFile(prefFolder, "prefs.dat");

    static {
        System.out.println("--" + prefFolder);
        try {
            System.out.println("--" + prefFolder.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(PropertiesSavingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("--" + prefFolder.exists());
        if (!prefFolder.exists()) {
            System.out.println("--" + prefFolder.mkdir());
            prefFolder.mkdir();
        }
    }

    public static void loadProperties(MainController ctl) {
        if (SAVE_LOAD_LOCK.tryLock()) {
            try {
                System.out.println("Load Lock Aquired");
                PropertiesController pc = ctl.getPropertiesController();
                ApplicationPropertiesController ac = ctl.getApplicationPropertiesController();
                loadApplicationProperties(ac); // DERP - Got to load the data location before trying to read the data :D - BIG DERP

                File prf = getDataFile(pc);
                System.out.println(prf);
                ObjectInputStream ois = null;
                Object bl = null, cls = null;
                if (prf.exists()) {
                    try {

                        ois = new ObjectInputStream(SecurityStore.getCipherInputStream(prf));
                        cls = ois.readObject();
                        bl = ois.readObject();
                    } catch (ClassNotFoundException ex) {
                        ExceptionReporter.reportException(ex);
                    } catch (IOException ex) {
                        ExceptionReporter.reportException(ex);
                    } finally {
                        if (ois != null) {
                            try {
                                ois.close();
                            } catch (IOException ex) {
                                ExceptionReporter.reportException(ex);
                            }
                        }
                    }
                    if (bl != null && bl instanceof Blackboard) {
                        pc.setBlackboard((Blackboard) bl);
                    }
                    if (cls != null && cls instanceof Class && BlackboardProviders.getByClass((Class) cls) != null) {
                        pc.setProviderClass((Class) cls);
                    }
                }
            } finally {
                SAVE_LOAD_LOCK.unlock();
            }
        }
        System.out.println("Load Lock Released");
    }

    private static void loadApplicationProperties(ApplicationPropertiesController ctl) {
        if (!prefDataFile.exists()) {
            return;
        }
        Properties p = new Properties();
        try {
            p.loadFromXML(new FileInputStream(prefDataFile));
            //p.loadFromXML(SecurityStore.getCipherInputStream(prefDataFile));
        } catch (IOException ex) {
            Logger.getLogger(ApplicationPropertiesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ctl.setUpdateStyle(UpdateStyle.valueOf(p.getProperty(UPDATE_STYLE_PROPERTY)));
        } catch (IllegalArgumentException e) {
            ctl.setUpdateStyle(UpdateStyle.COMPLETELY_AUTOMATIC);
        }
        File dataDirectory = new File(p.getProperty(DATA_DIRECTORY_PROPERTY, "."));
        if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
            dataDirectory = new File(".");
        }
        try {
            dataDirectory = dataDirectory.getCanonicalFile();
        } catch (IOException e) {
            dataDirectory = dataDirectory.getAbsoluteFile();
        } finally {
            ctl.setDataDirectory(dataDirectory);
        }
    }

    public static void saveProperties(MainController ctl) {
        if (SAVE_LOAD_LOCK.tryLock()) {
            System.out.println("Save Lock Aquired");
            try {
                PropertiesController pc = ctl.getPropertiesController();
                ApplicationPropertiesController ac = ctl.getApplicationPropertiesController();

                File prf = getDataFile(pc);

                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(SecurityStore.getCipherOutputStream(prf));
                    oos.writeObject(pc.getProviderClass());
                    oos.writeObject(pc.getBlackboard());
                    oos.flush();
                } catch (IOException ex) {
                    ExceptionReporter.reportException(ex);
                } finally {
                    if (oos != null) {
                        try {
                            oos.close();
                        } catch (IOException ex) {
                            ExceptionReporter.reportException(ex);
                        }
                    }
                }
                saveApplicationProperties(ac);
            } finally {
                SAVE_LOAD_LOCK.unlock();
            }
        }
        System.out.println("Save Lock Released");
    }

    private static void saveApplicationProperties(ApplicationPropertiesController ctl) {

        Properties p = new Properties();
        p.setProperty(UPDATE_STYLE_PROPERTY, ctl.getUpdateStyle().name());
        p.setProperty(DATA_DIRECTORY_PROPERTY, ctl.getDataDirectory().getPath());
        try {
            p.storeToXML(new FileOutputStream(prefDataFile), "Generated by DLect.");
        } catch (IOException ex) {
            Logger.getLogger(ApplicationPropertiesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static File getDataFile(PropertiesController pc) {
        return getDataFile(pc.getParentFolder());
    }

    public static File getDataFile(File parentFolder) {
        return FileUtils.getFile(parentFolder, "DLect.dat");
    }
}
