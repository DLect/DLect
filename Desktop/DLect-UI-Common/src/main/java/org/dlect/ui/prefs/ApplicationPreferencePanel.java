/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs;

import com.google.common.base.Optional;
import org.dlect.controller.MainController;
import org.dlect.controller.event.ControllerType;
import org.dlect.controller.helper.ControllerStateHelper;
import org.dlect.controller.provider.ProviderHelper;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.model.Database;
import org.dlect.model.helper.CommonSettingNames;
import org.dlect.provider.loader.ProviderDetail;
import org.dlect.update.UpdateController;
import org.dlect.update.UpdateStyle;

/**
 * TODO(Later) implement this class to support changing & deleting login data.
 * TODO(Later) implement moving credentials.
 */
public class ApplicationPreferencePanel extends PreferencePanel implements EventListener {

    private static final String DEFAULT_PROVIDER_TEXT = "No Provider Selected";// TODO(Later) Localise
    private static final String NOT_LOGGED_IN_TEXT = "Not Logged In";// TODO(Later) Localise
    private final MainController c;

    /**
     * Creates new form ApplicationPreferencePanel
     */
    public ApplicationPreferencePanel(MainController c) {
        super(c);
        this.c = c;
        initComponents();
        initSettings();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel3 = new javax.swing.JLabel();
        updatesAutoInstall = new javax.swing.JRadioButton();
        updatesNotify = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        loginUserName = new javax.swing.JTextField();
        loginProv = new javax.swing.JTextField();
        updatesNoCheck = new javax.swing.JRadioButton();

        setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Updates:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel3, gridBagConstraints);

        buttonGroup1.add(updatesAutoInstall);
        updatesAutoInstall.setText("Automatically install updates");
        updatesAutoInstall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateSelectionChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 60;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(updatesAutoInstall, gridBagConstraints);

        buttonGroup1.add(updatesNotify);
        updatesNotify.setText("Notify me when updates are ready");
        updatesNotify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateSelectionChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 70;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(updatesNotify, gridBagConstraints);

        jLabel4.setText("Logged In As: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel4, gridBagConstraints);

        loginUserName.setEditable(false);
        loginUserName.setText(NOT_LOGGED_IN_TEXT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(loginUserName, gridBagConstraints);

        loginProv.setEditable(false);
        loginProv.setText(DEFAULT_PROVIDER_TEXT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(loginProv, gridBagConstraints);

        updatesNoCheck.setText("Don't check for updates");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 80;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(updatesNoCheck, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void processEvent(Event e) {
        // TODO(LAter) implement listening to changes in Update style and login information.
    }

    private void updateSelectionChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateSelectionChanged
        Database d = c.getDatabaseHandler().getDatabase();
        if (updatesNotify.isSelected()) {
            UpdateController.addUpdateSetting(UpdateStyle.MANUAL, d);
        } else if (updatesNoCheck.isSelected()) {
            UpdateController.addUpdateSetting(UpdateStyle.NONE, d);
        } else {
            UpdateController.addUpdateSetting(UpdateStyle.AUTOMATIC, d);
        }
    }//GEN-LAST:event_updateSelectionChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField loginProv;
    private javax.swing.JTextField loginUserName;
    private javax.swing.JRadioButton updatesAutoInstall;
    private javax.swing.JRadioButton updatesNoCheck;
    private javax.swing.JRadioButton updatesNotify;
    // End of variables declaration//GEN-END:variables

    @Override
    public void doSave() {
    }

    @Override
    public void doPreShow() {
    }

    @Override
    public String getTabName() {
        return "Application";
    }

    @Override
    public String getTabTooltip() {
        return "Modify Application Settings";
    }

    @Override
    public boolean isModified() {
        return false; // All changes are instant.
    }

    private void initSettings() {
        updateSettings();
    }

    private void updateSettings() {
        updateLoginDetails();
        updateDataLocationDetails();
        updateTrayDetails();
        updateUpdateDetails();
    }

    private void updateLoginDetails() {
        ControllerStateHelper csh = c.getControllerStateHelper();
        ProviderHelper ph = c.getProviderHelper();

        Optional<String> username = c.getDatabaseHandler().getEncryptedSetting(CommonSettingNames.USERNAME);
        ProviderDetail pd = ph.getProviderDetail();

        if (!username.isPresent()) {
            loginUserName.setText(NOT_LOGGED_IN_TEXT);
        } else if (!csh.hasCompleted(ControllerType.LOGIN)) {
            loginUserName.setText("(" + NOT_LOGGED_IN_TEXT + ") " + username.get());
        } else {
            loginUserName.setText(username.get());
        }
        if (pd == null) {
            loginProv.setText(DEFAULT_PROVIDER_TEXT);
        } else {
            loginProv.setText(pd.getName());
        }
    }

    private void updateDataLocationDetails() {
        // TODO(LAter) implement updateDataLocationDetails
    }

    private void updateTrayDetails() {
        // TODO(LAter) Implement updateTrayDetails
    }

    private void updateUpdateDetails() {
        Database d = c.getDatabaseHandler().getDatabase();

        UpdateStyle us = UpdateController.getUpdateSetting(d);
        switch (us) {
            case AUTOMATIC:
                updatesAutoInstall.setSelected(true);
                break;
            case MANUAL:
                updatesNotify.setSelected(true);
                break;
            case NONE:
                updatesNoCheck.setSelected(true);
                break;
            default:
                updatesAutoInstall.setSelected(true);
                break;
        }
    }
}
