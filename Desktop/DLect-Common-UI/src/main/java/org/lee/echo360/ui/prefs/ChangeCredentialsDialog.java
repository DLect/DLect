/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui.prefs;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.ui.LoginPanel;

/**
 *
 * @author lee
 */
public class ChangeCredentialsDialog extends JDialog {

    private final LoginPanel loginPane;
    private boolean canceled = false;

    /**
     *
     * @param c
     * @param parent
     */
    public ChangeCredentialsDialog(final MainController c, JComponent parent) {
        super(SwingUtilities.windowForComponent(parent), "Change Login Credentials", ModalityType.APPLICATION_MODAL);
        this.setLocationRelativeTo(parent);
        this.setLayout(new CardLayout());
        final String uname = c.getPropertiesController().getBlackboard().getUsername();
        final String pword = c.getPropertiesController().getBlackboard().getUsername();
        final Class provClass = c.getPropertiesController().getProviderClass();
        loginPane = new LoginPanel(c) {
            @Override
            public void cancel() {
                canceled = true;
                c.getPropertiesController().setCredentials(uname, pword);
                c.getPropertiesController().setProviderClass(provClass);
                ChangeCredentialsDialog.this.setVisible(false);
            }

            @Override
            public void complete() {
                canceled = false;
                ChangeCredentialsDialog.this.setVisible(false);
            }

            @Override
            protected void doLogin() {
                c.getPropertiesController().getBlackboard().clearSubjects();
                super.doLogin();
            }
        };
        this.add(loginPane, "1");
        Dimension d = loginPane.getMinimumSize();
        d.height += 20;
        this.setMinimumSize(d);
    }

    public boolean isCanceled() {
        return canceled;
    }
}
