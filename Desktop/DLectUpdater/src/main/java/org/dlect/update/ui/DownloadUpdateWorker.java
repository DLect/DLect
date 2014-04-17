/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update.ui;

import org.dlect.update.Pair;
import java.io.File;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.dlect.update.UpdateImpl;

/**
 *
 * @author lee
 */
public abstract class DownloadUpdateWorker extends SwingWorker<File, Pair<String, Integer>> implements UpdateImpl.Listener {

    private final JProgressBar p;

    public DownloadUpdateWorker(JProgressBar p) {
        this.p = p;
        p.setStringPainted(true);
        p.setString("");
    }

    @Override
    protected void process(List<Pair<String, Integer>> chunks) {
        Pair<String, Integer> pair = chunks.get(chunks.size() - 1);
        if (pair.getA() != null && !pair.getA().isEmpty()) {
            p.setString(pair.getA());
        }
        p.setValue(pair.getValue());


    }

    @Override
    protected abstract void done();

    @Override
    public void setMax(final int max) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                p.setMaximum(max);
            }
        });
    }

    @Override
    public void update(Pair<String, Integer> s) {
        publish(s);
    }

    @Override
    protected File doInBackground() throws Exception {
        UpdateImpl.addListener(this);
        try {
            return UpdateImpl.downloadUpdate();
        } finally {
            UpdateImpl.removeListener(this);
        }
    }
}
