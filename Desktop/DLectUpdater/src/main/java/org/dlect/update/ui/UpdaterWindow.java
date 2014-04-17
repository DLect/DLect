/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update.ui;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author lee
 */
public class UpdaterWindow extends javax.swing.JFrame {

    public static void start(final String location) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UpdaterWindow(location).setVisible(true);
            }
        });
    }
    private final String location;

    /**
     * Creates new form UpdaterWindow
     *
     * @param loc
     */
    public UpdaterWindow(String loc) {
        initComponents();
        ChangeLogPane.configureLogPane(changeLogPane, jScrollPane1);
        this.location = loc;
        new DownloadUpdateWorker(jProgressBar1) {
            @Override
            protected void done() {
                try {
                    jLabel1.setText("To finish installing the update, Please close DLect.");
                    new InstallUpdateWorker(jProgressBar1, location, this.get()).execute();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    // TODO report Error
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                    // TODO report Error
                }
            }
        }.execute();
        this.setLocationRelativeTo(null);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        changeLogPane = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DLect Updater");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(400, 250));
        setPreferredSize(new java.awt.Dimension(400, 250));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Download the latest version of DLect");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(jLabel1, gridBagConstraints);

        jProgressBar1.setToolTipText("");
        jProgressBar1.setValue(70);
        jProgressBar1.setString("Downloading at 100KB/s");
        jProgressBar1.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(jProgressBar1, gridBagConstraints);

        jScrollPane1.setViewportView(changeLogPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UpdaterWindow("tmp-dl.jar").setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane changeLogPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
