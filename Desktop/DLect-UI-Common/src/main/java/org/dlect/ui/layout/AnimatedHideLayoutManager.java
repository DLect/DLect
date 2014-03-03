/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dlect.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Window;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JViewport;
import org.dlect.utils.AdvancedTimer;

/**
 *
 * @author lee
 */
public class AnimatedHideLayoutManager implements LayoutManager {

    private static final double PIXELS_PER_FRAME = 10;
    private static final long TIMER_DELAY = 1000 / 60; // 60 fps
    private final Component head;
    private final Component pane;
    private final Component parent;
    private Component topLevelComp;
    private double movementAmount;
    private int heightAtLastCalc = -1;
    private int movementDirection = -1;
    private double animationPercentage = 0;
    private AdvancedTimer timer;

    public AnimatedHideLayoutManager(Component head, Component pane, Component parent) {
        this.head = head;
        this.pane = pane;
        this.parent = parent;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        // Do Nothing - The elements are given in the constructor.
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        // Do Nothing - The elements are given in the constructor.
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension d = head.getPreferredSize();
        Dimension paneD = pane.getPreferredSize();
        d.width = Math.max(d.width, paneD.width);
        d.height += (paneD.height * animationPercentage) / 1.0;
        return d;
    }

    @Override
    public String toString() {
        return "AnimatedHideLayoutManager{" + "head=" + head + ", pane="
                + pane + ", parent=" + parent + ", animationPercentage="
                + animationPercentage + '}';
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        head.setLocation(0, 0);
        int width = parent.getWidth();
        Dimension d = new Dimension(head.getPreferredSize());
        d.width = width;
        int headHeight = d.height;
        head.setSize(d);
        d.height = pane.getPreferredSize().height;
        pane.setSize(d);
        int y = headHeight;
        y -= (d.height * (1.0 - animationPercentage));
        pane.setLocation(0, y);
    }

    public boolean animateDown() {
        movementDirection = 1;
        return doAnimate();
    }

    public boolean animateUp() {
        movementDirection = -1;
        return doAnimate();
    }

    private boolean doAnimate() {
        updateMovementAmount();
        generateTopLevelComponent();
        if (timer == null || timer.isCanceled()) {
            timer = new AdvancedTimer(true);
            timer.schedule(new AnimationTimerTask(timer), 0, TIMER_DELAY);
        }
        return true;
    }

    public boolean animateToggle() {
        movementDirection = -movementDirection;
        return doAnimate();
    }

    private void generateTopLevelComponent() {
        if (topLevelComp != null) {
            return;
        }
        topLevelComp = traverseTree(parent);
    }

    private Component traverseTree(Component current) throws IllegalStateException {
        if (current instanceof JViewport || current instanceof Window || current instanceof Dialog) {
            return current;
        }
        if (current.getParent() == null) {
            throw new IllegalStateException("Can't animate a panel without it having a parent");
        }
        return traverseTree(current.getParent());
    }

    private void updateMovementAmount() {
        heightAtLastCalc = pane.getHeight();
        movementAmount = (PIXELS_PER_FRAME / heightAtLastCalc);
    }

    class AnimationTimerTask extends TimerTask {

        private Timer t;

        public AnimationTimerTask(Timer t) {
            super();
            this.t = t;
        }

        @Override
        public void run() {
            animationPercentage += movementAmount * movementDirection;
            if (animationPercentage >= 1) {
                animationPercentage = 1;
                t.cancel();
            } else if (animationPercentage <= 0) {
                animationPercentage = 0;
                t.cancel();
            }
            parent.invalidate();
            topLevelComp.validate();
            topLevelComp.doLayout();
        }
    }
}
