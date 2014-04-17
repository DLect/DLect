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
public enum MenuIconManager implements IconManager {

    DOWNLOADING("m_downloading", false);
    private final IconManagerHelper imh;

    private MenuIconManager(String filePrefix, boolean osDependant) {
        imh = new IconManagerHelper("menu", filePrefix, osDependant);
    }

    @Override
    public Image getImage() {
        return imh.getImage();
    }
}
