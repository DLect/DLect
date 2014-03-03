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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
    private static final LoadingCache<Integer, String> dotPostfixString = CacheBuilder.newBuilder().build(new CacheLoader<Integer, String>() {
        @Override
        public String load(Integer key) throws Exception {
            StringBuilder d = new StringBuilder(key * 2);
            for (int i = 0; i < key; i++) {
                d.append('.');
            }
            return d.toString();
        }
    });
    private final JButton b;

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
        b.setText(name + dotPostfixString.apply(3));
        Dimension ps = b.getPreferredSize();
        b.setPreferredSize(ps);
        b.setText(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dots = (dots % 3) + 1;
        b.setText(name + dotPostfixString.apply(dots));
    }

    public void reset() {
        b.setPreferredSize(null);
        b.setText(name);
        Dimension ps = b.getPreferredSize();
        b.setPreferredSize(ps);
        dots = 1;
    }
}
