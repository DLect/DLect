/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui.util;

import java.awt.Color;

/**
 *
 * @author lee
 */
public class ColorUtil {

    public static Color whiten(Color c, double percentage) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hsb[2] += percentage;
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    private ColorUtil() {
    }

}
