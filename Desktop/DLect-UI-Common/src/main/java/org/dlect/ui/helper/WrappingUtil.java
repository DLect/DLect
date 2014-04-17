/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.helper;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author lee
 */
public class WrappingUtil {

    private WrappingUtil() {
    }

    public static MouseEvent duplicateEvent(MouseEvent e) {
        return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(),
                              e.getModifiers(), 0, 0, e.getClickCount(), e.isPopupTrigger(),
                              e.getButton());
    }

    private static MouseListener wrapMouseListener(final MouseListener ml, final JComponent origin) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.setSource(origin);
                ml.mouseClicked(duplicateEvent(e));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                e.setSource(origin);
                ml.mousePressed(duplicateEvent(e));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                e.setSource(origin);
                ml.mouseReleased(duplicateEvent(e));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                e.setSource(origin);
                ml.mouseEntered(duplicateEvent(e));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.setSource(origin);
                ml.mouseExited(duplicateEvent(e));
            }
        };
    }

    public static JPanel wrap(JComponent entry, int anchor, int fill, Border b) {
        JPanel p = new JPanel();
        return wrap(p, entry, anchor, fill, b);
    }

    public static JPanel wrap(JPanel p, JComponent entry, int anchor, int fill, Border b) {
        p.setLayout(new GridBagLayout());
        p.setBorder(b);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        c.gridwidth = c.gridheight = 1;
        c.anchor = anchor;
        c.fill = fill;
        c.weightx = c.weighty = 1;
        p.add(entry, c);
        return p;
    }

    public static JPanel wrapCenterNoFill(JComponent entry, Border b) {
        return wrap(entry, GridBagConstraints.CENTER, GridBagConstraints.NONE, b);
    }

    public static JPanel wrapCenterNoFill(JComponent entry) {
        return wrap(entry, GridBagConstraints.CENTER, GridBagConstraints.NONE, null);
    }

    public static JPanel wrapCenterBothFill(JComponent entry) {
        return wrap(entry, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null);
    }

    public static JPanel wrapCenterBothFill(JComponent entry, Border b) {
        return wrap(entry, GridBagConstraints.CENTER, GridBagConstraints.BOTH, b);
    }

    public static JPanel wrapCenterHorizontalFill(JComponent entry, Border b) {
        return wrap(entry, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, b);
    }

    public static JPanel wrapCenterHorizontalFill(JComponent entry) {
        return wrap(entry, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null);
    }

    public static void initRedirectListeners(JComponent target, JComponent... redirectors) {
        for (MouseListener ml : target.getMouseListeners()) {
            MouseListener m = wrapMouseListener(ml, target);
            for (JComponent com : redirectors) {
                com.addMouseListener(m);
            }
        }
    }
}
