/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lee
 */
public class UpdateImpl {

    public static final String UPDATE_DOWNLOAD_URL = "http://uqlectures.sourceforge.net/?h=dl";
    private static final int READ_SIZE = (int) 1e7;
    private static final Set<Listener> listeners = new HashSet<>();

    public static void addListener(Listener l) {
        listeners.add(l);
    }

    public static void removeListener(Listener l) {
        listeners.remove(l);
    }

    private static void publish(Pair<String, Integer> m) {
        for (Listener l : listeners) {
            l.update(m);
        }
    }

    private static void setMax(int max) {
        for (Listener l : listeners) {
            l.setMax(max);
        }
    }

    public static File downloadUpdate() throws IOException {
        publish(Pair.of("Starting up", 0));
        File tempFile = File.createTempFile("DLect New Update", ".jar");
        InputStream nc = null;
        FileOutputStream fc = null;
        HttpURLConnection con = null;
        long currentPosition = 0;
        int lastRead;
        int max = 1;
        boolean hasContentLength = false;
        try {
            con = getToDownload();
            if (con.getHeaderFieldInt("Content-Length", -1) != -1) {
                System.out.println("Content Length Exists");
                max = con.getHeaderFieldInt("Content-Length", 0) + 1;
                hasContentLength = true;
            } else {
                max = 3;
            }
            setMax(max);
            nc = con.getInputStream();

            fc = new FileOutputStream(tempFile);
            byte[] reads = new byte[READ_SIZE];
            publish(Pair.of("Connecting & Preparing to Download", 1));
            long startTime = System.currentTimeMillis();
            while ((lastRead = nc.read(reads, 0, READ_SIZE)) > 0) {
                fc.write(reads, 0, lastRead);
                currentPosition += lastRead;
                if (currentPosition > (max / 2)) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(UpdateImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                publish(Pair.of("Downloading at " + toSpeed(currentPosition
                                                            / ((double) (System.currentTimeMillis() - startTime))),
                                hasContentLength ? (int) currentPosition : 2));
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
            if (nc != null) {
                nc.close();
            }
            if (fc != null) {
                fc.close();
            }
            publish(Pair.of("Completed", max));
        }
        return tempFile;
    }

    protected static HttpURLConnection getToDownload() throws IOException {
        return getToDownload(UPDATE_DOWNLOAD_URL);
    }

    protected static HttpURLConnection getToDownload(final String downloadURL) throws IOException {
        HttpURLConnection con;
        HttpURLConnection.setFollowRedirects(true);
        con = (HttpURLConnection) new URL(downloadURL).openConnection();
        con.connect();
        debugHeaders(con);
        while (con.getHeaderField("Location") != null) {
            con = (HttpURLConnection) new URL(con.getHeaderField("Location")).openConnection();
            con.setUseCaches(false);
            con.connect();
            debugHeaders(con);
        }
        return con;
    }

    private static String toSpeed(double speed) {
        if (Double.isInfinite(speed) || Double.isNaN(speed)) {
            return "Unkown Speed";
        }
        double bytesSec = speed * 1000;
        String unit = "B/s";
        if (bytesSec > 1024) {
            unit = "KB/s";
            bytesSec /= 1024;
        }
        if (bytesSec > 1024) {
            unit = "MB/s";
            bytesSec /= 1024;
        }
        if (bytesSec > 1024) {
            unit = "GB/s";
            bytesSec /= 1024;
        }
        return String.format("%01.2f %s", bytesSec, unit);
    }

    private static void debugHeaders(HttpURLConnection con) throws IOException {
        if (con == null) {
            return;
        }
        System.out.println("------------------------------------");
        System.out.println("Responce Code: " + con.getResponseCode());
        for (Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
            String string = entry.getKey();
            List<String> list = entry.getValue();
            System.out.print(string + ": ");
            if (list.size() < 2) {
                System.out.println(list.isEmpty() ? "-----------" : list.get(0));
            } else {
                System.out.println();
                for (String s : list) {
                    System.out.println("\t\t" + s);
                }
            }
        }
        System.out.println("------------------------------------\n\n");
    }

    public static interface Listener {

        public void setMax(int max);

        public void update(Pair<String, Integer> s);
    }
}
