/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.BlackboardProviderWrapper;
import org.lee.echo360.providers.BlackboardProviders;

/**
 *
 * @author Lee Symes
 */
public class PropertiesController {

    private static final long serialVersionUID = 0x490e458e;
    private final Object LOCK = new Object();
    private Blackboard b;
    private Class providerClass;
    private final MainController ctl;

    public PropertiesController(MainController ctl) {
        this.ctl = ctl;
    }

    public void setCredentials(String user, String pass) {
        synchronized (LOCK) {
            if (b == null) {
                initBlackboard();
            }
        }
        b.setUsername(user);
        b.setPassword(pass);
    }

    public void setProviderClass(Class providerClass) {
        this.providerClass = providerClass;
    }

    public Blackboard getBlackboard() {
        return b;
    }

    public Class getProviderClass() {
        return providerClass;
    }

    public BlackboardProviderWrapper getProvider() {
        return BlackboardProviders.getByClass(this.getProviderClass());
    }

    public void initBlackboard() {
        synchronized (LOCK) {
            if (b == null) {
                b = new Blackboard();
            }
        }
    }

    public File getFolderFor(Subject s) {
        return getFolderFor(getParentFolder(), s);
    }

    public File getFileFor(Subject s, Lecture l, DownloadType dt) {
        return getFileFor(getParentFolder(), s, l, dt);
    }

    public File getFolderFor(File folder, Subject s) {
        return FileUtils.getFile(folder, s.getFolderName());
    }

    public File getFileFor(File folder, Subject s, Lecture l, DownloadType dt) {
        return FileUtils.getFile(getFolderFor(folder, s), l.getFileName(dt));
    }

    public File getParentFolder() {
        return ctl.getApplicationPropertiesController().getDataDirectory();
    }

    public  void setBlackboard(Blackboard blackboard) {
        synchronized (LOCK) {
            this.b = blackboard;
        }
    }
}
