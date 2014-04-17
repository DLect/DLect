/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.dlect.update.InstallImpl;

/**
 *
 * @author lee
 */
public class InstallUpdateWorker extends SwingWorker<Void, String> implements InstallImpl.Listener {

    private final File dlectJar;
    private final File tmpJar;
    private final JProgressBar p;

    public InstallUpdateWorker(JProgressBar p, String file, File tmpFile) {
        this.p = p;
        this.tmpJar = tmpFile;
        this.dlectJar = new File(file);
        if (this.tmpJar == null) {
            throw new IllegalArgumentException("Source must not be null");
        }
        if (!this.tmpJar.exists()) {
            throw new IllegalArgumentException("Source '" + this.tmpJar + "' does not exist");
        }
        if (this.tmpJar.isDirectory()) {
            throw new IllegalArgumentException("Source '" + this.tmpJar + "' is a directory");
        }
        if (this.dlectJar.isDirectory()) {
            throw new IllegalArgumentException("Destination '" + this.dlectJar + "' is a directory");
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        InstallImpl.addListener(this);
        try {
            InstallImpl.doInstall(dlectJar, tmpJar);
        } finally {
            InstallImpl.removeListener(this);
        }
        return null;
    }

    @Override
    protected void done() {
        new Timer().schedule(
                new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 3000);
    }

    @Override
    protected void process(List<String> chunks) {
        p.setString(chunks.get(chunks.size() - 1));
    }

    @Override
    public void update(String s) {
        publish(s);
    }
}
