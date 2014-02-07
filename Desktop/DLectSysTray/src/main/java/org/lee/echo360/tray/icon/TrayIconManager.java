/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.tray.icon;

import java.awt.Image;
import org.lee.echo360.icon.IconManager;
import org.lee.echo360.icon.IconManagerHelper;

/**
 *
 * @author lee
 */
public enum TrayIconManager implements IconManager {

    DEFAULT("default", false),
    ERROR("error", false),
    NO_CONNECTION("no_connection", false),
    DOWNLOADING("downloading", false),
    PROCESSING("processing", false);
    private final IconManagerHelper imh;

    private TrayIconManager(String filePrefix, boolean osDependant) {
        imh = new IconManagerHelper("tray", filePrefix, osDependant);
    }

    @Override
    public Image getImage() {
        return imh.getImage();
    }
}
