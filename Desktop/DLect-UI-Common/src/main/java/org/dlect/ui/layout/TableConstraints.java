/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.layout;

import java.awt.GridBagConstraints;

/**
 *
 * @author lee
 */
public class TableConstraints {

    public static TableConstraints convert(GridBagConstraints cons) {
        boolean fullH = false;
        if (cons.weighty > 0 && (cons.gridheight == GridBagConstraints.REMAINDER || cons.gridheight >= 100)) {
            fullH = true;
        }
        return new TableConstraints(fullH, cons.gridx, cons.gridy);
    }

    public static TableConstraints create(int x, int y) {
        return new TableConstraints(false, x, y);
    }

    /**
     * Creates a new constraints object that represents a `full height` object
     * at the specified x coordinate.
     *
     * `full height` means the element will be placed at {@code y=0} and have a
     * height equal to that of it's parent.
     *
     * @param x The {@code x} coordinate to put the element at.
     *
     * @return
     */
    public static TableConstraints create(int x) {
        return new TableConstraints(true, x, 0);
    }
    private final boolean fullHeight;
    private final int gridX;
    private final int gridY;

    private TableConstraints(boolean fullHeight, int gridX, int gridY) {
        this.fullHeight = fullHeight;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public boolean isFullHeight() {
        return fullHeight;
    }

    public int getGridY() {
        return gridY;
    }

    public int getGridX() {
        return gridX;
    }
}
