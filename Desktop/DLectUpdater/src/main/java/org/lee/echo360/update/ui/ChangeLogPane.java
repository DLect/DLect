/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.update.ui;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author lee
 */
public class ChangeLogPane extends JScrollPane {
    
    public ChangeLogPane() {
        JEditorPane e = new JEditorPane("text/html", HTML_PREFIX + "<b>Loading Changelog.</b><br>" + CHANGE_LOG_LINK_HTML);
        e.setEditable(false);
        e.setContentType("text/html");
        e.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        }
                    } catch (IOException ex) {
                        // No Op
                    } catch (URISyntaxException ex) {
                        // No Op
                    }
                }
            }
        });
        new ChangeLogWorker(e, this).execute();
        this.setViewportView(e);
    }
    private static final String HTML_PREFIX = "<html><font style='font-family: verdana,arial,sans-serif'>";
    private static final String CHANGE_LOG_LINK_HTML = "To read the change log online visit <a href='http://uqlectures.sourceforge.net/?h=rdme'>http://uqlectures.sourceforge.net/?h=rdme</a>";
    private static final String ERROR_MESSAGE = HTML_PREFIX + "<b>Error</b> retrieving the change log. " + CHANGE_LOG_LINK_HTML;
    
    private class ChangeLogWorker extends SwingWorker<String, Void> {
        
        private final JEditorPane pane;
        private final JScrollPane sPane;
        
        public ChangeLogWorker(JEditorPane pane, JScrollPane sPane) {
            this.pane = pane;
            this.sPane = sPane;
        }
        
        @Override
        protected void done() {
            try {
                pane.setText(this.get());
            } catch (InterruptedException ex) {
                pane.setText(ERROR_MESSAGE);
            } catch (ExecutionException ex) {
                pane.setText(ERROR_MESSAGE);
            }
            sPane.invalidate();
            sPane.getViewport().invalidate();
            sPane.getViewport().setViewPosition(new Point());
            pane.setCaretPosition(0);
        }
        
        @Override
        protected String doInBackground() throws Exception {
            URL updateDesc = new URL("http://uqlectures.sourceforge.net/?h=cl");
            StringBuilder builder = new StringBuilder(1000);
            BufferedReader r = new BufferedReader(new InputStreamReader(openURL(updateDesc)));
            String line;
            while ((line = r.readLine()) != null) {
                builder.append(line);
            }
            return HTML_PREFIX + builder.toString();
        }
        
        private InputStream openURL(URL updateDesc) throws IOException {
            HttpURLConnection c = (HttpURLConnection) updateDesc.openConnection();
            c.setInstanceFollowRedirects(true);
            c.connect();
            while (c.getResponseCode() == 301 || c.getResponseCode() == 302) {
                c = (HttpURLConnection) new URL(c.getHeaderField("Location")).openConnection();
                c.setInstanceFollowRedirects(true);
                c.connect();
            }
            return c.getInputStream();
        }
    }
}
