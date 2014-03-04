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
    private Dimension ps;

    public DownloadButtonDotter(JButton b) {
        this.b = b;
        name = b.getText();
    }
    private String name;
    private int dots = 1;

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
        update(b.getText());
    }

    public void update(String buttonDefault) {
        name = buttonDefault;
        b.setPreferredSize(null);
        b.setText(name + dotPostfixString.get(3));

        ps = b.getPreferredSize();
        System.out.println(ps);
        b.setPreferredSize(ps);
        b.setText(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dots = (dots % 3) + 1;
        b.setText(name + dotPostfixString.get(dots));
        b.validate();
        if (!Objects.equal(ps, b.getSize())) {
            System.out.println("SIZE CHANGED:");
            System.out.println("\tO:" + ps);
            System.out.println("\tN:" + b.getPreferredSize());
            if (ps != null) {
                if (ps.width < b.getSize().width) {
                    ps = b.getPreferredSize();
                }
            }
        }
    }

    public void reset() {
        b.setPreferredSize(null);
        b.setText(name);
        Dimension ps = b.getPreferredSize();
        b.setPreferredSize(ps);
        dots = 1;
    }
}
