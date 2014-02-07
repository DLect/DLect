/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.icon;

import java.awt.Image;

/**
 *
 * @author lee
 */
public enum LogoIconManager implements IconManager {

    FUZZY("fuzzy"), BLOCK("block");
    private final IconManagerHelper imh;

    private LogoIconManager(String filePrefix) {
        imh = new IconManagerHelper("logo", filePrefix, false);
    }

    @Override
    public Image getImage() {
        return imh.getImage();
    }
}
