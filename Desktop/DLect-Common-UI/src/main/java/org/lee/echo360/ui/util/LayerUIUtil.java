/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui.util;

import com.jhlabs.image.BoxBlurFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.effect.LayerEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

/**
 *
 * @author Lee Symes
 */
public class LayerUIUtil {

    public static LockableUI plainLockUI(LayerEffect... e) {
        return new LockableUI(e);
    }

    public static LockableUI plainRefreshingLockUI(LayerEffect... e) {
        final LockableUI u = plainLockUI(e);
        final Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (u.getLayer() != null) {
                    u.getLayer().repaint();
                }
            }
        });
        timer.start();
        return u;
    }

    public static LayerEffect blur() {
        return new BufferedImageOpEffect(new BoxBlurFilter(2, 2, 2));
    }

    private LayerUIUtil() {
    }
}
