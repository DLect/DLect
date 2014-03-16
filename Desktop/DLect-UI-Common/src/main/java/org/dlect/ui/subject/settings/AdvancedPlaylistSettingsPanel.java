/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.subject.settings;

import com.google.common.collect.EnumHashBiMap;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Formatter;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.dlect.controller.MainController;
import org.dlect.model.Lecture;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.ui.helper.EnumComboBoxModel;
import org.dlect.ui.SwingWorkerProgressDialog;

/**
 *
 * @author lee
 */
public class AdvancedPlaylistSettingsPanel extends javax.swing.JPanel implements ActionListener {

    private EnumHashBiMap<DownloadType, JComboBox> playlistComboBoxes = EnumHashBiMap.create(DownloadType.class);
    private EnumHashBiMap<DownloadType, JComboBox> fileTagComboBoxes = EnumHashBiMap.create(DownloadType.class);
    private final Subject s;
//    private PlaylistFormatter originalPF;
//    private TagFormatter originalTF;
    private int playlistButtonRow;
    private int tagButtonRow;
    private final MainController ctl;

    /**
     * Creates new form AdvancedPlaylistSettingsPanel
     */
    public AdvancedPlaylistSettingsPanel(Subject s, MainController mc) {
        ctl = mc;
        this.s = s;
//        originalPF = new PlaylistFormatter(s.getPlaylistFormatter());
//        originalTF = new TagFormatter(s.getTagFormatter());
        initComponents();
        initMediaSettings();
    }

    private void initMediaSettings() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.add(playlistLabel, gbc);
        playlistButtonRow = gbc.gridy;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
//        PlaylistFormatter.PlaylistStyle style = s.getPlaylistFormatter().getStyle();
//        playlistCombo.setModel(new EnumComboBoxModel<PlaylistFormatter.PlaylistStyle>(PlaylistFormatter.PlaylistStyle.class));
//        playlistCombo.setSelectedItem(style);
        this.add(playlistCombo, gbc);
//        playlistComboBoxes.putAll(addDownloadTypeBoxes("Playlist", gbc, playlistCombo.getSelectedIndex() != 0, s.getPlaylistFormatter()));
        initTagMediaSettings(gbc);
    }

    private void initTagMediaSettings(GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.add(fileTagLabel, gbc);
        tagButtonRow = gbc.gridy;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(fileTagComboBox, gbc);
//        boolean enabled = s.getTagFormatter().isEnabled();
//        fileTagComboBox.setSelectedItem((enabled ? 1 : 0));
//        fileTagComboBoxes.putAll(addDownloadTypeBoxes("Tag", gbc, enabled, s.getTagFormatter()));
    }

    private EnumMap<DownloadType, JComboBox> addDownloadTypeBoxes(final String postfix, GridBagConstraints gbc, boolean enabled, Formatter f) {
        EnumMap<DownloadType, JComboBox> type = new EnumMap<DownloadType, JComboBox>(DownloadType.class);
        for (DownloadType downloadType : DownloadType.values()) {
            String label = downloadType + " " + postfix + " Style:";
            ComboBoxModel m = new EnumComboBoxModel<DownloadType>(DownloadType.class, "None");
            JLabel l = new JLabel(label);
            JComboBox c = new JComboBox(m);
            c.setEnabled(enabled);
            //c.setSelectedItem(f.getNullableFormat(downloadType));
            c.addActionListener(this);
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            this.add(l, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 2;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.BOTH;
            this.add(c, gbc);
            type.put(downloadType, c);
        }
        return type;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        playlistLabel = new JLabel();
        playlistCombo = new JComboBox();
        fileTagLabel = new JLabel();
        fileTagComboBox = new JComboBox();
        applyPlaylistButton = new JButton();
        applyTagButton = new JButton();

        playlistLabel.setText("Playlist Style:");

        playlistCombo.setModel(new DefaultComboBoxModel(new String[]{"Not Enabled", "For Each Stream", "All in One"}));
        playlistCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                playlistComboActionPerformed(evt);
            }
        });

        fileTagLabel.setText("Audio/Video Tags:");

        fileTagComboBox.setModel(new DefaultComboBoxModel(new String[]{"<html>None <b>(Safest)</b>", "Basic"}));
        fileTagComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fileTagComboBoxActionPerformed(evt);
            }
        });

        applyPlaylistButton.setText("Apply");

        applyPlaylistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyPlaylistButtonActionPerformed(e);
            }
        });

        applyTagButton.setText("Apply");

        applyTagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyTagButtonActionPerformed(e);
            }
        });

        this.setLayout(new GridBagLayout());
    }// </editor-fold>

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO(Later) inplement playlist panel.
        if (!(e.getSource() instanceof JComboBox)) {
            return;
        }
        JComboBox b = (JComboBox) e.getSource();
//        FormatType t = (b.getSelectedIndex() == 0 ? null : (FormatType) b.getSelectedItem());
//        if (playlistComboBoxes.containsValue(b)) {
//            s.getPlaylistFormatter().setFormat(playlistComboBoxes.inverse().get(b), t);
//        }
//        if (fileTagComboBoxes.containsValue(b)) {
//            s.getTagFormatter().setFormat(fileTagComboBoxes.inverse().get(b), t);
//        }
        updateButtons();
    }

    private void playlistComboActionPerformed(java.awt.event.ActionEvent evt) {
        updateComboValues();
        updateButtons();
    }

    private void fileTagComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        updateComboValues();
        updateButtons();
    }

    private void applyPlaylistButtonActionPerformed(ActionEvent e) {
//        if (!originalPF.equals(s.getPlaylistFormatter())) {
//            originalPF = new PlaylistFormatter(s.getPlaylistFormatter());
//            applyPlaylistButton.setEnabled(false);
//            SwingWorkerProgressDialog swpd = new SwingWorkerProgressDialog(SwingUtilities.windowForComponent(this), new PlaylistApplyWorker());
//            swpd.setVisible(true);
//        }
    }

    private void applyTagButtonActionPerformed(ActionEvent e) {
//        if (!originalTF.equals(s.getTagFormatter())) {
//            originalTF = new TagFormatter(s.getTagFormatter());
//            applyTagButton.setEnabled(false);
//            SwingWorkerProgressDialog swpd = new SwingWorkerProgressDialog(SwingUtilities.windowForComponent(this), new TagApplyWorker());
//            swpd.setVisible(true);
//        }
    }
    private JComboBox fileTagComboBox;
    private JLabel fileTagLabel;
    private JComboBox playlistCombo;
    private JLabel playlistLabel;
    private JButton applyPlaylistButton;
    private JButton applyTagButton;

    private void updateButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = playlistButtonRow;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
//        if (!originalPF.equals(s.getPlaylistFormatter())) {
//            gbc.gridwidth = 1;
//            this.add(playlistCombo, gbc);
//            gbc.weightx = 0;
//            gbc.gridx = 2;
//            this.add(applyPlaylistButton, gbc);
//        } else {
//            gbc.gridwidth = 2;
//            this.add(playlistCombo, gbc);
//            this.remove(applyPlaylistButton);
//        }
        gbc.gridx = 1;
        gbc.gridy = tagButtonRow;
//        if (!originalTF.equals(s.getTagFormatter())) {
//            gbc.gridwidth = 1;
//            this.add(fileTagComboBox, gbc);
//            gbc.weightx = 0;
//            gbc.gridx = 2;
//            this.add(applyTagButton, gbc);
//        } else {
//            gbc.gridwidth = 2;
//            this.add(fileTagComboBox, gbc);
//            this.remove(applyTagButton);
//        }
        this.validate();
    }

    private void updateComboValues() {
        boolean en = playlistCombo.getSelectedIndex() != 0;
//        s.getPlaylistFormatter().setStyle((PlaylistFormatter.PlaylistStyle) playlistCombo.getSelectedItem());
        for (Map.Entry<DownloadType, JComboBox> b : playlistComboBoxes.entrySet()) {
            JComboBox jComboBox = b.getValue();
            jComboBox.setEnabled(en);
            if (en) {
//                jComboBox.setSelectedItem(s.getPlaylistFormatter().getFormat(b.getKey()));
            } else {
                jComboBox.setSelectedIndex(0);
            }
        }
        en = fileTagComboBox.getSelectedIndex() != 0;
//        s.getTagFormatter().setEnabled(en);
        for (Map.Entry<DownloadType, JComboBox> b : fileTagComboBoxes.entrySet()) {
            JComboBox jComboBox = b.getValue();
            jComboBox.setEnabled(en);
            if (en) {
//                jComboBox.setSelectedItem(s.getTagFormatter().getFormat(b.getKey()));
            } else {
                jComboBox.setSelectedIndex(0);
            }
        }
    }

    private class PlaylistApplyWorker extends SwingWorkerProgressDialog.Worker {

        @Override
        protected void doAction() throws Exception {
            int max = DownloadType.values().length * 3;
            this.setMax(max);
            try {
                int i = 0;
                for (DownloadType dt : DownloadType.values()) {
//                    this.publish(Pair.of("Removing old " + dt + " playlists", i++));
//                    PlaylistGenerator.getFileNameForSinglePlaylist(s, dt, ctl.getPropertiesController()).delete();
//                    Collection<File> otherFiles = PlaylistGenerator.getFileNamesByStream(s, dt, ctl.getPropertiesController()).values();
//                    for (File file : otherFiles) {
//                        file.delete();
//                    }
//                    this.publish(Pair.of("Building " + dt + " playlists", i++));
//                    PlaylistGenerator.buildAllPlaylistsFor(s, dt, ctl.getPropertiesController());
//                    this.publish(Pair.of("Built " + dt + " playlists", i++));
                }
            } finally {
                applyPlaylistButton.setEnabled(true);
                updateButtons();
            }
//            this.publish(Pair.of("Done", max));
        }

        @Override
        protected String getDescription() {
            return "<html>Updating the playlists with <br>your new settings";
        }
    }

    private class TagApplyWorker extends SwingWorkerProgressDialog.Worker {

        @Override
        protected void doAction() throws Exception {
//            final PropertiesController pc = ctl.getPropertiesController();
            int max = DownloadType.values().length * s.getLectures().size() * 2;
            this.setMax(max);
            try {
                int i = 0;
                for (DownloadType dt : DownloadType.values()) {
                    for (Lecture lecture : s.getLectures()) {
//                        this.publish(Pair.of("Updating " + dt + " for " + lecture.getTime(), i++));
//                        if (lecture.isFilePresent(dt)) {
//                            File orig = pc.getFileFor(s, lecture, dt);
//                            File tmpFile = new File(orig.toString() + ".part");
//                            FileUtils.moveFile(orig, tmpFile);
//                            MediaTagEncoder.tag(tmpFile, orig, s, lecture, dt);
//                            tmpFile.delete();
//                        }
//                        this.publish(Pair.of("Updated " + dt + " for " + lecture.getTime(), i++));
                    }
                }
            } finally {
//                this.publish(Pair.of("Done", max));
                applyTagButton.setEnabled(true);
                updateButtons();
            }
        }

        @Override
        protected String getDescription() {
            return "<html>Updating your lecture recordings<br> with your new settings";
        }
    }
}
