/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.prefs;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
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
import org.dlect.model.Semester;
import org.dlect.model.Semester.SemesterEventID;
import org.dlect.model.Subject;
import org.dlect.ui.helper.WrappingUtil;

/**
 *
 * @author lee
 */
public class SemesterRowGroupHandler implements EventListener {

    private static final Border SEMESTER_LABEL_BORDER = new CompoundBorder(new MatteBorder(1, 0, 2, 0, Color.BLACK), new EmptyBorder(0, 2, 0, 0));
    private static final Border SEMESTER_CHECK_BORDER = new MatteBorder(1, 1, 2, 0, Color.BLACK);
    private static final Border SEMESTER_PADDING_PANEL_BORDER = new MatteBorder(1, 1, 2, 0, Color.BLACK);
    private static final Color SEMESTER_HEADING_BACKGROUND_COLOR = getHeadingPanelColor();

    private final Semester s;
    private final MainController mc;
    private final TreeMap<Subject, SubjectRowHandler> rowHandlers = Maps.newTreeMap();

    private JLabel semesterNameLabel;
    private JPanel semesterNamePanel;

    private JCheckBox enabledCheckBox;
    private JPanel enabledCheckPanel;

    private JPanel paddingPanel;

    public SemesterRowGroupHandler(Semester s, MainController mc) {
        this.s = s;
        this.mc = mc;

        Wrappers.addSwingListenerTo(this, s, Semester.class);
        Wrappers.addSwingListenerTo(this, mc, Database.class);
    }

    private static Color getHeadingPanelColor() {
        Color c = UIManager.getColor("Panel.background");
        if (c == null) {
            return Color.WHITE.darker().darker();
        } else {
            return c.darker();
        }
    }

    public void createItems() {
        semesterNameLabel = new JLabel(s.getLongName());
        semesterNamePanel = new JPanel();
        semesterNamePanel.setOpaque(true);
        semesterNamePanel.setBackground(SEMESTER_HEADING_BACKGROUND_COLOR);
        WrappingUtil.wrap(semesterNamePanel, semesterNameLabel, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, SEMESTER_LABEL_BORDER);

        enabledCheckBox = new JCheckBox();
        enabledCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mc.getSubjectDisplayHelper().setSemesterDisplayed(s, enabledCheckBox.isSelected());
            }
        });
        doUpdateCheckBox();
        enabledCheckPanel = new JPanel();
        enabledCheckPanel.setOpaque(true);
        enabledCheckPanel.setBackground(SEMESTER_HEADING_BACKGROUND_COLOR);
        WrappingUtil.initRedirectListeners(enabledCheckBox, enabledCheckPanel);
        WrappingUtil.wrap(enabledCheckPanel, enabledCheckBox, GridBagConstraints.CENTER, GridBagConstraints.NONE, SEMESTER_CHECK_BORDER);

        paddingPanel = new JPanel();
        paddingPanel.setOpaque(true);
        paddingPanel.setBackground(SEMESTER_HEADING_BACKGROUND_COLOR);
        paddingPanel.setBorder(SEMESTER_PADDING_PANEL_BORDER);

        for (Subject subject : s.getSubjects()) {
            createSubjectItems(subject);
        }
    }

    public void createSubjectItems(Subject s) {
        SubjectRowHandler srh = rowHandlers.get(s);
        if (srh == null) {
            srh = new SubjectRowHandler(s, mc);
            rowHandlers.put(s, srh);
        }
        srh.createItems();
    }

    public void doUpdate() {
        semesterNameLabel.setText(s.getLongName());
        doUpdateCheckBox();

        for (SubjectRowHandler row : rowHandlers.values()) {
            row.doUpdate();
        }
    }

    private void doUpdateCheckBox() {
        enabledCheckBox.setSelected(mc.getSubjectDisplayHelper().isSemesterDisplayed(s));
        enabledCheckBox.setEnabled(!s.getSubjects().isEmpty());
    }

    /**
     *
     * @param c
     * @param gbc
     *
     * @return the number of new rows.
     */
    public int doLayout(JComponent c, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.weightx = 1;
        c.add(semesterNamePanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        c.add(enabledCheckPanel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        c.add(paddingPanel, gbc);

        int y = gbc.gridy + 1;

        doSubjectLayout(c, y);

        return rowHandlers.values().size() + 1;
    }

    public void doSubjectLayout(JComponent c, int y) {
        ImmutableSortedSet<Subject> subjects = s.getSubjects();

        Set<Subject> toRemove = Sets.newHashSet(rowHandlers.keySet());
        toRemove.removeAll(subjects);
        for (Subject sub : toRemove) {
            rowHandlers.remove(sub).removeFromLayout(c);
        }

        for (Subject sub : s.getSubjects()) {
            SubjectRowHandler get = rowHandlers.get(sub);
            if (get == null) {
                get = new SubjectRowHandler(sub, mc);
                rowHandlers.put(sub, get);
                get.createItems();
            }
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = y;
            get.doLayout(c, gbc);

            y++;
        }
    }

    public void removeFromLayout(JComponent c) {
        c.remove(semesterNamePanel);
        c.remove(enabledCheckPanel);

        for (SubjectRowHandler row : rowHandlers.values()) {
            row.removeFromLayout(c);
        }
    }

    @Override
    public void processEvent(Event e) {
        if (e.getEventID().equals(SemesterEventID.LONG_NAME)) {
            semesterNameLabel.setText(s.getLongName());
        } else if (e.getEventID().equals(DatabaseEventID.SETTING) || e.getEventID().equals(SemesterEventID.SUBJECT)) {
            // Need to listen for changes in subject list.
            doUpdateCheckBox();
        }
    }

}
