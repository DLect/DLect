/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.lee.echo360.control.exceptions.InvalidImplemetationException;
import org.lee.echo360.providers.BlackboardProviderWrapper;
import org.lee.echo360.util.ExceptionReporter;
import org.lee.echo360.util.ImageUtils;

/**
 *
 * @author lee
 */
public class ProviderRenderer extends JLabel implements ListCellRenderer {

    private static final int ICON_WIDTH = 16;
    private static final int ICON_HEIGHT = 16;
    private static final ProviderRenderer renderer = new ProviderRenderer();

    public static ListCellRenderer getRenderer() {
        return renderer;
    }

    public ProviderRenderer() {
        this.setMinimumSize(new Dimension(ICON_WIDTH, ICON_HEIGHT));
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (!(value instanceof BlackboardProviderWrapper)) {
            this.setIcon(null);
            this.setText(value == null ? "null" : value.toString());
            return this;
        }
        BlackboardProviderWrapper prov = (BlackboardProviderWrapper) value;
        try {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setText(prov.getProviderName());
            Image i = prov.getProviderImage();
            if (i != null) {
                Image scaled = ImageUtils.srinkAndCenter(i, ICON_WIDTH, ICON_HEIGHT);
                setIcon(new ImageIcon(scaled));
            } else {
                setIcon(null);
            }
            return this;
        } catch (InvalidImplemetationException ex) {
            ExceptionReporter.reportException(ex);
            this.setIcon(null);
            this.setText("INVALID VALUE");
            return this;
        }
    }
}
