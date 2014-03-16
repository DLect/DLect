package org.dlect.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lee
 */
public class DropDownArrow extends JButton {

    private static final int ARROW_WIDTH = 20;
    private static final int ARROW_HEIGHT = ARROW_WIDTH / 2;
    private int left;
    private int top;

    {
        super.setOpaque(false);
        super.setText("");
    }

    @Override
    public void setText(String text) {
    }

    @Override
    public void setOpaque(boolean isOpaque) {
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        left = (getWidth() - ARROW_WIDTH) / 2;
        top = (getHeight() - ARROW_HEIGHT) / 2;
        g2.setBackground(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(left, top, left + (ARROW_WIDTH / 2), top + ARROW_HEIGHT);
        g2.drawLine(left + (ARROW_WIDTH / 2), top + ARROW_HEIGHT, left + ARROW_WIDTH, top);
    }
}
