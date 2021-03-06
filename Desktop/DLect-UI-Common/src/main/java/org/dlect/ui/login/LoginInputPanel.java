/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.login;

import com.google.common.base.Optional;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.dlect.controller.LoginController;
import org.dlect.controller.provider.ProviderHelper;
import org.dlect.provider.loader.ProviderDetail;
import org.dlect.ui.ProviderModel;
import org.dlect.ui.ProviderRenderer;

/**
 *
 * @author lee
 */
public class LoginInputPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private final ProviderModel model;
    private ProviderDetail selectedProvider = null;
    private ActionListener listener = null;

    /**
     * Creates new form LoginPanel
     *
     * @param loader
     */
    public LoginInputPanel(ProviderHelper loader) {
        this.model = new ProviderModel(loader.getProviders());

        initComponents();
        institutionCombo.setSelectedIndex(0);
        final DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                doUpdate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doUpdate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                doUpdate();
            }
        };
        usernameField.getDocument().addDocumentListener(documentListener);
        passwordField.getDocument().addDocumentListener(documentListener);

    }

    public ComboBoxModel<String> getModel() {
        return model;
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

        institutionLabel = new javax.swing.JLabel();
        institutionCombo = new javax.swing.JComboBox();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();

        setLayout(new java.awt.GridBagLayout());

        institutionLabel.setText("Institution:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        add(institutionLabel, gridBagConstraints);

        institutionCombo.setModel(getModel());
        institutionCombo.setRenderer(new ProviderRenderer());
        institutionCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                institutionComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(institutionCombo, gridBagConstraints);

        usernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        usernameLabel.setText("Username:");
        usernameLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        add(usernameLabel, gridBagConstraints);

        usernameField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(usernameField, gridBagConstraints);

        passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        passwordLabel.setText("Password:");
        passwordLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        add(passwordLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(passwordField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void institutionComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_institutionComboActionPerformed
        int providerIndex = institutionCombo.getSelectedIndex();
        final Optional<ProviderDetail> p = model.getProviderAt(providerIndex);
        if (p.isPresent()) {
            selectedProvider = p.get();
        } else {
            selectedProvider = null;
        }
        if (institutionCombo.getSelectedIndex() > 0) {
            setFieldsEnabled(true);
        } else {
            setFieldsEnabled(false);
        }
    }//GEN-LAST:event_institutionComboActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox institutionCombo;
    private javax.swing.JLabel institutionLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables

    private void setFieldsEnabled(boolean enabled) {
        usernameLabel.setEnabled(enabled);
        usernameField.setEnabled(enabled);
        passwordLabel.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        doUpdate();
    }

    public void setUsername(String username) {
        if (username == null) {
            usernameField.setText("");
        } else {
            usernameField.setText(username);
        }
    }

    public void setPassword(String password) {
        if (password == null) {
            passwordField.setText("");
        } else {
            passwordField.setText(password);
        }
    }

    public void setProvider(ProviderDetail provider) {
        if (provider == null) {
            institutionCombo.setSelectedIndex(0);
        } else {
            int idx = model.getProviderIndex(provider);
            institutionCombo.setSelectedIndex(idx);
        }
    }

    public ProviderDetail getSelectedProvider() {
        return selectedProvider;
    }

    /**
     * Override this method to allow stuff.
     */
    public void doUpdate() {
        if (listener != null) {
            listener.actionPerformed(new ActionEvent(this, -1, "Do Update"));
        }
    }

    public boolean hasProviderSelected() {
        return institutionCombo.getSelectedIndex() != 0;
    }

    public boolean hasValidCredentials() {
        return hasProviderSelected() && !usernameField.getText().isEmpty() && passwordField.getPassword().length > 0;
    }

    public void setAllEnabled(boolean enabled) {
        institutionLabel.setEnabled(enabled);
        institutionCombo.setEnabled(enabled);
        boolean credEnabled = hasProviderSelected() && enabled;
        usernameLabel.setEnabled(credEnabled);
        usernameField.setEnabled(credEnabled);
        passwordLabel.setEnabled(credEnabled);
        passwordField.setEnabled(credEnabled);
        if (enabled == true) {
            doUpdate();
        }
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public void putFocus() {
        usernameField.requestFocusInWindow();
    }

    public boolean saveCredentialsTo(LoginController lc) {
        if (hasProviderSelected()) {
            final String user = usernameField.getText();
            final String pass = String.valueOf(passwordField.getPassword());

            if (user.length() == 0 || pass.length() == 0) {
                return false; // Incomplete information.
            }

            lc.configureLoginCredentials(selectedProvider, user, pass);

            return true;
        } else {
            return false;
        }
    }
}
