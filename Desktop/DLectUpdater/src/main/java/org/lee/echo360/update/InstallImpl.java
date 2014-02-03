/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author lee
 */
public class InstallImpl {

    private static final Set<Listener> listeners = new HashSet<Listener>();

    public static void doInstall(File dlectJar, File tmpJar) throws IOException {
        publish("Waiting for DLect to close.");
        System.in.read(); // Wait for old instance to close.
        publish("Starting Install");
        if (!tmpJar.renameTo(dlectJar)) {
            InstallImpl.copyFiles(dlectJar, tmpJar);
        }
        publish("Install Completed");
    }

    public static void copyFiles(File dlectJar, File tmpJar) throws IOException {
        long bufferSize = 1024 * 1024; // 1 MB Blocks
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(tmpJar);
            fos = new FileOutputStream(dlectJar);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count;
            while (pos < size) {
                count = size - pos > bufferSize ? bufferSize : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            if (output != null) {
                output.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (input != null) {
                input.close();
            }
            if (fis != null) {
                fis.close();
            }
            tmpJar.delete();
        }
    }

    public static void addListener(Listener l) {
        listeners.add(l);
    }

    public static void removeListener(Listener l) {
        listeners.remove(l);
    }

    private static void publish(String m) {
        for (Listener l : listeners) {
            l.update(m);
        }
    }

    public static interface Listener {

        public void update(String s);
    }
}
