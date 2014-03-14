/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs.subject;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import org.dlect.controller.MainController;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class AdvancedPlaylistPreferencesPanel extends SubjectPreferencesPanel {

    private final Subject s;
    private final MainController ctl;

    public AdvancedPlaylistPreferencesPanel(Subject s, MainController ctl) {
        super(s, ctl);
        this.s = s;
        this.ctl = ctl;
        initComponents();
    }

    private void initComponents() {
        AdvancedPlaylistSettingsPanel apsp = new AdvancedPlaylistSettingsPanel(s, ctl);
        JLabel warning = new JLabel();
        
        this.setLayout(new GridBagLayout());

        warning.setText("<html><b>Warning: </b>Some of these features are experimental. <br>Please Use With Caution");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(warning, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(apsp, gbc);
    }

    @Override
    public void doPreShow() {
        // TODO implement
    }

    @Override
    public String getTabName() {
        return "Media";
    }

    @Override
    public String getTabTooltip() {
        return "Modify the playlist & tagging settings for this subject.";
    }
}
