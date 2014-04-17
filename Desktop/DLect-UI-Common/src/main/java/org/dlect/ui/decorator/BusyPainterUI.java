/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.decorator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;
import javax.swing.Timer;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import org.jdesktop.swingx.painter.BusyPainter;

public class BusyPainterUI extends LockableUI
        implements ActionListener {

    private final BusyPainter busyPainter;
    private final Timer timer;
    private int frameNumber;
    private boolean showSpinner = true;

    public BusyPainterUI() {
        busyPainter = createBusyPainter();
        timer = new Timer(1000 / 15, this);
    }

    @Override
    public void paint(Graphics g2, JComponent l) {
        super.paint(g2, l);
        if (isLocked() && isSpinnerShown()) {
            busyPainter.paint((Graphics2D) g2, l, l.getWidth(), l.getHeight());
        }
    }

    @Override
    protected void paintLayer(Graphics2D g2, JXLayer<? extends JComponent> l) {
        super.paintLayer(g2, l);
        if (isLocked() && isSpinnerShown()) {
            busyPainter.paint(g2, l, l.getWidth(), l.getHeight());
        }
    }

    @Override
    public void setLocked(boolean isLocked) {
        super.setLocked(isLocked);
        if (isLocked && showSpinner) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    // Change the frame for the busyPainter
    // and mark BusyPainterUI as dirty
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isSpinnerShown()) {
            frameNumber++;
            busyPainter.setFrame(frameNumber);
        }
        // this will repaint the layer
        setDirty(true);
    }

    public boolean isSpinnerShown() {
        return showSpinner;
    }

    public void setSpinnerShown(boolean showSpinner) {
        this.showSpinner = showSpinner;
        setDirty(true);
        this.getLayer().repaint();
        if (this.isLocked() && showSpinner) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public final void setPointShape(Shape pointShape) {
        busyPainter.setPointShape(pointShape);
    }

    public final void setTrajectory(Shape trajectory) {
        busyPainter.setTrajectory(trajectory);
    }

    public void setPoints(int points) {
        busyPainter.setPoints(points);
    }

    public static BusyPainter createBusyPainter() {
        return createBusyPainter(new Dimension(100, 100));
    }

    public static BusyPainter createBusyPainter(Dimension recSize) {
        double s = Math.min(recSize.width, recSize.height);
        double p = s * 0.15;
        BusyPainter busyPainter = new BusyPainter();
        busyPainter.setPoints(10);
        busyPainter.setPointShape(new Ellipse2D.Double(0, 0, p, p));
        busyPainter.setTrajectory(new Ellipse2D.Double(0, 0, s, s));
        busyPainter.setTrailLength(4);
        busyPainter.setBaseColor(new Color(1f, 1f, 1f, 0f));
        busyPainter.setPaintCentered(true);
        return busyPainter;
    }
}
