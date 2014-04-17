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
package org.dlect.ui.decorator;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 *
 * @author lee
 */
public class DownloadButtonDotter implements ActionListener {

    private Timer timer = null;
    private static final ImmutableList<String> dotPostfixString = ImmutableList.of("", ".", "..", "...");
    private final JButton b;

    private String name;
    private int dots = 1;

    public DownloadButtonDotter(JButton b) {
        this.b = b;
        this.name = b.getText();
    }

    @Deprecated
    public void updateAndStart() {
        update();
        start();
    }

    public void updateAndStart(String buttonDefault) {
        update(buttonDefault);
        start();
    }

    public void start() {
        update(name);
        if (timer == null) {
            timer = new Timer(500, this);
        }
        timer.start();
        b.setEnabled(false);
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
        reset();
        b.setEnabled(true);
    }

    public void update() {
        updatePreferedSize(name + dotPostfixString.get(3));
        b.setText(name);
    }

    public void updateAndStop(String text) {
        if (!Objects.equal(name, text)) {
            name = text;
            reset();
        }
    }

    public void updatePreferedSize(String s) {
        b.setPreferredSize(null);
        b.setText(s);

        b.setPreferredSize(b.getPreferredSize());
    }

    public void update(String buttonDefault) {
        if (!Objects.equal(name, buttonDefault)) {
            name = buttonDefault;
            update();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dots = (dots % 3) + 1;
        b.setText(name + dotPostfixString.get(dots));
    }

    public void reset() {
        updatePreferedSize(name);
        dots = 1;
    }
}
