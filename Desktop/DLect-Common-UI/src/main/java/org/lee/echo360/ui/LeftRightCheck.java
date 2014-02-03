/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.plaf.UIResource;

/**
 *
 * @author lee
 */
public class LeftRightCheck extends JCheckBox {

    /**
     * This checkbox is used to check that the UIManager is not trying to make
     * the font smaller than normal.
     */
    private final static JCheckBox DEFAULT_CHECK_BOX = new JCheckBox();
    private int minReqestedWidth = 0;
    private final JLabel rightLabel;

    public LeftRightCheck() {
        this("", "", null, false, 140);
    }

    public LeftRightCheck(String text) {
        this(text, "", null, false, 140);
    }

    public LeftRightCheck(String leftText, String rightText) {
        this(leftText, rightText, null, false, 140);
    }

    public LeftRightCheck(String leftText, String rightText, boolean selected) {
        this(leftText, rightText, null, selected, 140);
    }

    public LeftRightCheck(String leftText, String rightText, boolean selected, int minReqestedWidth) {
        this(leftText, rightText, null, selected, 140);
    }

    public LeftRightCheck(String leftText, String rightText, Icon icon, boolean selected, int minReqestedWidth) {
        super(leftText, icon, selected);
        this.minReqestedWidth = minReqestedWidth;
        rightLabel = new JLabel(rightText, TRAILING);
        // TODO figure out how to make the font work right on OSX
        this.setLayout(new BaseLineSingleElementLayoutManager(10));
        this.add(rightLabel);
    }

    public int getMinReqestedWidth() {
        return minReqestedWidth;
    }

    public void setMinReqestedWidth(int minReqestedWidth) {
        this.minReqestedWidth = minReqestedWidth;
    }

    public String getRightText() {
        return rightLabel.getText();
    }

    public Color getRightForeground() {
        return rightLabel.getForeground();
    }

    @Override
    public final void setFont(Font font) {
        if (font.getSize2D() <= DEFAULT_CHECK_BOX.getFont().getSize2D() && font instanceof UIResource) {
            font = font.deriveFont(DEFAULT_CHECK_BOX.getFont().getSize2D());
        }
        super.setFont(font);
        if (rightLabel != null) {
            rightLabel.setFont(font);
        }
    }

    public void setRightText(String text) {
        rightLabel.setText(text);
    }

    public void setRightForeground(Color fg) {
        rightLabel.setForeground(fg);
    }

    private class BaseLineSingleElementLayoutManager implements LayoutManager {

        private int hgap;
        private Component component;

        public BaseLineSingleElementLayoutManager(int hgap) {
            this.hgap = hgap;
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
            // Do Nothing
        }

        @Override
        public void removeLayoutComponent(Component comp) {
            // Do Nothing
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            if (parent != null && parent instanceof JCheckBox) {
                JCheckBox cc = (JCheckBox) parent;
                // These 2 lines will add the size of the checkbox to the
                // preferred size
                Dimension parentSize = DEFAULT_CHECK_BOX.getPreferredSize();
                parentSize.width += cc.getFontMetrics(cc.getFont()).
                        stringWidth(cc.getText()) + hgap;
                if (checkComponent(cc)) {
                    Dimension childSize = this.component.getPreferredSize();
                    parentSize.height = Math.max(parentSize.height,
                            childSize.height);
                    parentSize.width += childSize.width;
                }
                parentSize.width = Math.max(parentSize.width, minReqestedWidth);
                return parentSize;
            }
            return new Dimension();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            if (!checkComponent(parent)) {
                return;
            }
            this.component.setSize(parent.getSize());
            this.component.setLocation(0, getBaseline(parent)
                    - getBaseline(this.component));
        }

        private int getBaseline(Component c) {
            return c.getBaseline(c.getWidth(), c.getHeight());
        }

        protected boolean checkComponent(Container parent) {
            if (parent == null) {
                return false;
            } else if (parent.getComponentCount() >= 1
                    && parent.getComponent(0).isVisible()) {
                this.component = parent.getComponent(0);
                return true;
            } else {
                return false;
            }
        }
    }
}