/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

/**
 *
 * @author lee
 */
public class TableLayout implements LayoutManager2 {

    private Map<Component, TableConstraints> elements = new HashMap<Component, TableConstraints>();
    private int specifiedRowHeight;
    private int maxGridY;
    private Map<Integer, Boolean> fullHeightColumns;
    private Map<Integer, Integer> columnWidths;
    private Map<Integer, Integer> leftX;
    private int minWidth;
    private boolean needsValidating = true;

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        TableConstraints cons = null;
        if (constraints instanceof GridBagConstraints) {
            cons = TableConstraints.convert((GridBagConstraints) constraints);
        } else if (constraints instanceof TableConstraints) {
            cons = (TableConstraints) constraints;
        }
        if (cons == null) {
            throw new IllegalArgumentException("Should pass only a GridBag or Table Constraints object.");
        }
        elements.put(comp, cons);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
        needsValidating = true;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        // Do Nothing as I need the layout stuff.
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        elements.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        updateComponents(true); // Force the update.
        Dimension d = new Dimension(minWidth, (maxGridY + 1) * specifiedRowHeight);
        return d;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        updateComponents(); // Force the update.
        for (Entry<Component, TableConstraints> entry : elements.entrySet()) {
            Component c = entry.getKey();
            TableConstraints tc = entry.getValue();
            int gridX = tc.getGridX();
            int x = leftX.get(gridX);
            int width = columnWidths.get(gridX);
            int height;
            int y;
            if (tc.isFullHeight()) {
                y = 0;
                height = parent.getHeight();
            } else {
                y = (tc.getGridY() * specifiedRowHeight);
                height = specifiedRowHeight;
            }
            c.setBounds(x, y, width, height);
        }
    }

    private void updateComponents() throws IllegalArgumentException {
        updateComponents(false);
    }

    private void updateComponents(boolean force) throws IllegalArgumentException {
        if (force || needsValidating) {
            specifiedRowHeight = 0;
            maxGridY = 0;
            columnWidths = new HashMap<Integer, Integer>();
            fullHeightColumns = new HashMap<Integer, Boolean>();
            for (Entry<Component, TableConstraints> entry : elements.entrySet()) {
                Component component = entry.getKey();
                Dimension pref = component.getPreferredSize();
                TableConstraints tableConstraints = entry.getValue();
                final int x = tableConstraints.getGridX();
                if (tableConstraints.isFullHeight()) {
                    if (fullHeightColumns.containsKey(x)) {
                        throw new IllegalArgumentException("The column " + x
                                                           + " has an element in it AND it has a Full"
                                                           + " Height Element in it.");
                    }
                    fullHeightColumns.put(x, Boolean.TRUE);
                } else {
                    fullHeightColumns.put(x, Boolean.FALSE);
                    specifiedRowHeight = Math.max(specifiedRowHeight, pref.height);
                    maxGridY = Math.max(maxGridY, tableConstraints.getGridY());
                }
                if (columnWidths.containsKey(x)) {
                    columnWidths.put(x, Math.max(columnWidths.get(x), pref.width));
                } else {
                    columnWidths.put(x, pref.width);
                }
            }
        }
        leftX = new HashMap<Integer, Integer>();
        TreeSet<Integer> xCords = new TreeSet<Integer>(columnWidths.keySet());
        minWidth = 0;
        for (Integer xCord : xCords) {
            leftX.put(xCord, minWidth);
            minWidth += columnWidths.get(xCord);
        }
    }
}
