/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.icon;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * @author lee
 */
public class IconManagerHelper {

    private final URL imageFile;
    private Image image;

    public IconManagerHelper(String folder, String filePrefix, boolean osDependant) {
        String dirPrefix = "/media/icons/" + folder + "/";
        String postfix = ".png";
        if (osDependant) {
            if (SystemUtils.IS_OS_MAC) {
                filePrefix += "_osx";
            } else {
                filePrefix += "_win";
            }
        }
        imageFile = this.getClass().getResource(dirPrefix + filePrefix + postfix);
    }

    public Image getImage() {
        if (image == null) {
            loadImage();
        }
        return image;
    }

    private void loadImage() {
        image = new ImageIcon(imageFile).getImage();
    }
}
