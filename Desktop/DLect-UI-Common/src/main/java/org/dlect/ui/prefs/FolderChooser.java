/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author lee
 */
public class FolderChooser extends JDialog {

    private JTextField fileName;
    private JButton cancelButton;
    private JButton okButton;
    private final File folder;
    private boolean canceled = true;
    private String newFolderName = null;

    public FolderChooser(Window parent, File folder) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.setLocationRelativeTo(parent);
        this.folder = folder;
        initComponents();
    }

    private void initComponents() {
        fileName = new JTextField();
        cancelButton = new JButton("Cancel");
        okButton = new JButton("OK");

        this.setLayout(new GridBagLayout());
        this.setTitle("Create New Folder");
        this.getRootPane().setDefaultButton(okButton);
        this.setMinimumSize(new Dimension(200, 80));

        fileName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fileNameUpdated();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fileNameUpdated();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fileNameUpdated();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButtonActionPerformed();
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        this.getContentPane().add(fileName, c);

        c.gridwidth = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.weightx = 1.0;
        this.getContentPane().add(cancelButton, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0.0;
        this.getContentPane().add(okButton, c);

    }

    private void okButtonActionPerformed() {
        canceled = false;
        if (!fileName.getText().isEmpty() && !new File(folder, fileName.getText()).exists()) {
            newFolderName = fileName.getText();
        } else {
            newFolderName = null;
        }
        this.setVisible(false);
    }

    private void cancelButtonActionPerformed() {
        canceled = true;
        newFolderName = null;
        this.setVisible(false);
    }

    public String getNewFolderName() {
        return newFolderName;
    }

    public boolean isCanceled() {
        return canceled;
    }

    private void fileNameUpdated() {
        boolean en = !fileName.getText().isEmpty();
        en &= !new File(folder, fileName.getText()).exists();
        okButton.setEnabled(en);
    }
}
