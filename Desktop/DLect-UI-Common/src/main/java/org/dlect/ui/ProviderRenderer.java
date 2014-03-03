/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.dlect.provider.loader.ProviderDetail;

/**
 *
 * @author lee
 */
public class ProviderRenderer extends JLabel implements ListCellRenderer<Object> {

    private static final int ICON_WIDTH = 16;
    private static final int ICON_HEIGHT = 16;
    private static final ProviderRenderer renderer = new ProviderRenderer();
    private static final long serialVersionUID = 1L;

    public static ListCellRenderer<Object> getRenderer() {
        return renderer;
    }

    public ProviderRenderer() {
        this.setMinimumSize(new Dimension(ICON_WIDTH, ICON_HEIGHT));
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (!(value instanceof ProviderDetail)) {
            this.setIcon(null);
            this.setText(value == null ? "null" : value.toString());
            return this;
        }
        ProviderDetail prov = (ProviderDetail) value;
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setText(prov.getName());
        return this;
    }
}
