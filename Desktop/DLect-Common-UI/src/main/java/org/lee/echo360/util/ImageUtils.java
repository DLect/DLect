/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author lee
 */
public class ImageUtils {

    public static final Color TRANSPARENT_COLOR = new Color(1f, 1f, 1f, 0f);

    public static Image srinkAndCenter(Image i, int width, int height) {
        BufferedImage ret = null;
        if (i != null) {
            Image scaled;
            int leftOffset;
            int topOffset;
            scaled = scaleImageWithAspectRatio(i, width, height);
            int iW = scaled.getWidth(null);
            int iH = scaled.getHeight(null);
            leftOffset = width - iW;
            topOffset = height - iH;
            ret = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = ret.getGraphics();
            g.setColor(TRANSPARENT_COLOR);
            g.drawImage(scaled, leftOffset, topOffset, TRANSPARENT_COLOR, null);
        }
        return ret;
    }
//TODO create Tests

    public static Image scaleImageWithAspectRatio(Image i, int width, int height) {
        double widthRatio = ((double) width) / i.getWidth(null);
        double heightRatio = ((double) height) / i.getHeight(null);
        if (widthRatio < heightRatio) {
            return scaleWidthWise(i, width);
        } else {
            return scaleHeightWise( i, height);
        }
    }

    public static Image scaleWidthWise(Image i, int width) {
        // Scale the width and the height will be inside.
        return i.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
    }

    public static Image scaleHeightWise(Image i, int height) {
        return i.getScaledInstance(-1, height, Image.SCALE_SMOOTH);
    }

    private ImageUtils() {
    }
}
