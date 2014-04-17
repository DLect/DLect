/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.decorator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;

/**
 * TODO(Later) Re-Implement this to look nice.
 *
 * @author lee
 */
public class ArrowBorder extends AbstractBorder {

    private static final int ARROW_WIDTH = 20;
    private static final int ARROW_HEIGHT = 10;
    private static final int BORDER_WIDTH = 28;
    private static final long serialVersionUID = 1L;

    public ArrowBorder() {
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.bottom = insets.left = insets.top = 0;
        insets.right = BORDER_WIDTH;
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, BORDER_WIDTH);
    }

    @Override
    public void paintBorder(Component comp, Graphics gOld, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) gOld.create(x + width - BORDER_WIDTH, y, BORDER_WIDTH, height);

        int left = (BORDER_WIDTH - ARROW_WIDTH) / 2;
        int top = (height - ARROW_HEIGHT) / 2;
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(left, top, left + (ARROW_WIDTH / 2), top + ARROW_HEIGHT);
        g2.drawLine(left + (ARROW_WIDTH / 2), top + ARROW_HEIGHT, left + ARROW_WIDTH, top);
        g2.dispose();
    }

}
