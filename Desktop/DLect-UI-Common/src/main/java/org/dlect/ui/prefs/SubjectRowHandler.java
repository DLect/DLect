/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import org.dlect.controller.MainController;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.events.wrapper.Wrappers;
import org.dlect.model.Database;
import org.dlect.model.Database.DatabaseEventID;
import org.dlect.model.Subject;
import org.dlect.model.Subject.SubjectEventID;
import org.dlect.ui.helper.WrappingUtil;
import org.dlect.ui.subject.settings.AdvancedSubjectPreferencesDialog;

/**
 *
 * @author lee
 */
public class SubjectRowHandler implements EventListener {

    private static final Border LABEL_BORDER = new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK), new EmptyBorder(0, 12, 0, 0));
    private static final Border CHECK_BORDER = new MatteBorder(0, 1, 1, 0, Color.BLACK);
    private static final Border BUTTON_BORDER = new MatteBorder(0, 1, 1, 0, Color.BLACK);

    private final Subject s;
    private final MainController mc;

    private JButton settingsButton;
    private JPanel settingsButtonPanel;

    private JLabel subjectNameLabel;
    private JPanel subjectNamePanel;

    private JCheckBox subjectEnabled;
    private JPanel subjectEnabledPanel;

    private AdvancedSubjectPreferencesDialog aspd;

    public SubjectRowHandler(Subject s, MainController mc) {
        this.s = s;
        this.mc = mc;
        Wrappers.addSwingListenerTo(this, s, Subject.class);
        Wrappers.addSwingListenerTo(this, mc, Database.class);
    }

    public void createItems() {
        subjectNameLabel = new JLabel(s.getName());
        subjectNamePanel = new JPanel();
        WrappingUtil.wrap(subjectNamePanel, subjectNameLabel, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, LABEL_BORDER);

        subjectEnabled = new JCheckBox();
        subjectEnabled.setSelected(mc.getSubjectDisplayHelper().isSubjectDisplayed(s));
        subjectEnabled.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mc.getSubjectDisplayHelper().setSubjectDisplayed(s, subjectEnabled.isSelected());
            }
        });
        subjectEnabledPanel = new JPanel();
        WrappingUtil.initRedirectListeners(subjectEnabled, subjectEnabledPanel);
        WrappingUtil.wrap(subjectEnabledPanel, subjectEnabled, GridBagConstraints.CENTER, GridBagConstraints.NONE, CHECK_BORDER);

        settingsButton = new JButton("Settings");
        settingsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (aspd == null) {
                    aspd = new AdvancedSubjectPreferencesDialog(SwingUtilities.getWindowAncestor(settingsButton), s, mc);
                }
                aspd.setVisible(true);
            }
        });
        settingsButtonPanel = new JPanel();
        WrappingUtil.wrap(settingsButtonPanel, settingsButton, GridBagConstraints.CENTER, GridBagConstraints.BOTH, BUTTON_BORDER);
    }

    public void doUpdate() {
        subjectNameLabel.setText(s.getName());
        subjectEnabled.setSelected(mc.getSubjectDisplayHelper().isSubjectDisplayed(s));
    }

    public void doLayout(JComponent c, GridBagConstraints gbc) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        c.add(subjectNamePanel, gbc);

        gbc.weightx = 0;
        gbc.gridx = 1;
        c.add(subjectEnabledPanel, gbc);

        gbc.weightx = 0;
        gbc.gridx = 2;
        c.add(settingsButtonPanel, gbc);
    }

    @Override
    public void processEvent(Event e) {
        if (e.getEventID().equals(SubjectEventID.NAME) || e.getEventID().equals(DatabaseEventID.SETTING)) {
            doUpdate();
        }
    }

    public void removeFromLayout(JComponent c) {
        c.remove(settingsButtonPanel);
        c.remove(subjectEnabledPanel);
        c.remove(subjectNamePanel);
    }

}
