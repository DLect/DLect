/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.subject.settings;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import org.dlect.controller.helper.ControllerStateHelper;
import org.dlect.controller.helper.ControllerStateHelper.ControllerStateHelperEventID;
import org.dlect.controller.worker.download.PreconfiguredSubjectDownloader;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.events.wrapper.Wrappers;
import org.dlect.helper.LectureHelper;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.model.helper.ThreadLocalDateFormat;
import org.dlect.ui.decorator.DownloadButtonDotter;
import org.dlect.ui.helper.WrappingUtil;

/**
 *
 * @author lee
 */
public class LectureRowHandler implements EventListener {

    private static final Border LABEL_BORDER = new CompoundBorder(new MatteBorder(0, 0, 1, 1, Color.BLACK),
                                                                  new EmptyBorder(0, 1, 1, 2));
    private static final Border CHECK_BORDER = new MatteBorder(0, 1, 1, 0, Color.BLACK);
    private static final Border BUTTON_BORDER = new MatteBorder(0, 1, 1, 1, Color.BLACK);

    private static final ThreadLocalDateFormat LECTURE_DATE_FORMAT = new ThreadLocalDateFormat("yyyy/MM/dd HH:mm");
    private final PreconfiguredSubjectDownloader psd;
    private final ControllerStateHelper csh;

    private final Lecture lecture;
    private final Subject subject;

    private boolean init;
    private JLabel timeLabel;
    private JPanel labelPanel;

    private final EnumMap<DownloadType, LectureRowInput> inputs = Maps.newEnumMap(DownloadType.class);

    public LectureRowHandler(PreconfiguredSubjectDownloader psd, ControllerStateHelper csh, Subject s, Lecture l) {
        this.lecture = l;
        this.subject = s;
        this.psd = psd;
        this.csh = csh;
        Wrappers.addSwingListenerTo(this, csh);
        Wrappers.addSwingListenerTo(this, l);
        Wrappers.addSwingListenerTo(this, s, Stream.class);
    }

    public void createItems() {
        timeLabel = new JLabel(getLabelName());
        labelPanel = new JPanel();
        WrappingUtil.wrap(labelPanel, timeLabel, GridBagConstraints.CENTER, GridBagConstraints.BOTH, LABEL_BORDER);

        for (DownloadType dt : DownloadType.values()) {
            LectureDownload ld = getLectureDownloadFor(dt);

            JCheckBox check = new JCheckBox();
            check.addActionListener(new LectureDownloadCheckBoxListener(lecture, dt, check));
            JPanel checkPanel = new JPanel();
            WrappingUtil.wrap(checkPanel, check, GridBagConstraints.CENTER, GridBagConstraints.NONE, CHECK_BORDER);
            WrappingUtil.initRedirectListeners(check, checkPanel);

            JButton button = new JButton();
            button.addActionListener(new LectureDownloadButtonListener(psd, lecture, dt));
            JPanel buttonPanel = new JPanel();
            WrappingUtil.wrap(buttonPanel, button, GridBagConstraints.CENTER, GridBagConstraints.NONE, BUTTON_BORDER);

            DownloadButtonDotter dbd = new DownloadButtonDotter(button);
            LectureRowInput lri = new LectureRowInput(check, checkPanel, button, buttonPanel, dbd);

            updateInputGroup(dt, ld, lri);

            inputs.put(dt, lri);
        }
        init = true;
    }

    public void doUpdate() {
        if (!init) {
            return;
        }
        timeLabel.setText(getLabelName());
        for (DownloadType dt : DownloadType.values()) {
            LectureDownload ld = getLectureDownloadFor(dt);

            updateInputGroup(dt, ld, inputs.get(dt));
        }
    }

    public void doLayout(JComponent c, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        c.add(labelPanel, gbc);

        gbc.weightx = 0;
        for (DownloadType dt : DownloadType.values()) {
            LectureRowInput lri = inputs.get(dt);

            gbc.gridx++;
            c.add(lri.getCheckPanel(), gbc);

            gbc.gridx++;
            c.add(lri.getButtonPanel(), gbc);
        }
    }

    public void removeFromLayout(JComponent c) {
        c.remove(timeLabel);
        for (DownloadType dt : DownloadType.values()) {
            LectureRowInput lri = inputs.get(dt);

            c.remove(lri.getCheckPanel());
            c.remove(lri.getButtonPanel());
        }
    }

    public String getLabelName() {
        String name = LECTURE_DATE_FORMAT.format(lecture.getTime());

        ImmutableSortedSet<Stream> s = lecture.getStreams();
        List<String> str = new ArrayList<>();
        for (Stream stream : s) {
            str.add(stream.getName());
        }
        if (str.isEmpty()) {
            return name;
        }
        return name + " [" + Joiner.on(", ").skipNulls().join(str) + "]";
    }

    private LectureDownload getLectureDownloadFor(DownloadType dt) {
        LectureDownload ld = lecture.getLectureDownloads().get(dt);
        if (ld == null) {
            ld = new LectureDownload(null, null, false, false);
        }
        return ld;
    }

    private void updateInputGroup(DownloadType dt, LectureDownload ld, LectureRowInput lri) {
        if (csh.isDownloading(lecture, dt)) {
            lri.getButtonDotter().updateAndStart("Downloading");
            lri.getDownloadButton().setEnabled(false);
            lri.getEnabled().setEnabled(false);
            lri.getEnabled().setSelected(true);
        } else if (ld.isDownloaded()) {
            lri.getButtonDotter().updateAndStop("Downloaded");
            lri.getDownloadButton().setEnabled(false);
            lri.getEnabled().setEnabled(false);
            lri.getEnabled().setSelected(true);
        } else {
            lri.getButtonDotter().updateAndStop("Download");
            lri.getDownloadButton().setEnabled(true);
            lri.getEnabled().setEnabled(true);
            lri.getEnabled().setSelected(LectureHelper.isLectureDownloadEnabled(lecture, ld));
        }
    }

    @Override
    public void processEvent(Event e) {
        if (e.getSource() instanceof Stream
            || e.getSource() instanceof Lecture
            || e.getSource() instanceof LectureDownload) {
            doUpdate();
        } else if (e.getEventID().equals(ControllerStateHelperEventID.DOWNLOAD)) {
            doUpdate();
        }
    }

    private static class LectureDownloadButtonListener implements ActionListener {

        private final DownloadType dt;
        private final PreconfiguredSubjectDownloader psd;
        private final Lecture lecture;

        public LectureDownloadButtonListener(PreconfiguredSubjectDownloader psd, Lecture lecture, DownloadType dt) {
            this.psd = psd;
            this.lecture = lecture;
            this.dt = dt;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            psd.doDownload(lecture, dt);
        }
    }

    private static class LectureDownloadCheckBoxListener implements ActionListener {

        private final JCheckBox check;
        private final DownloadType dt;
        private final Lecture lecture;

        private LectureDownloadCheckBoxListener(Lecture lecture, DownloadType dt, JCheckBox check) {
            this.lecture = lecture;
            this.dt = dt;
            this.check = check;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean en = check.isSelected();
            LectureDownload ld = lecture.getLectureDownloads().get(dt);
            if (ld != null) {
                ld.setDownloadEnabled(en);
                if (en) {
                    lecture.setEnabled(true);
                }
            }
        }

    }

    private static final class LectureRowInput {

        private final JCheckBox enabled;
        private final JPanel checkPanel;
        private final JButton downloadButton;
        private final JPanel buttonPanel;
        private final DownloadButtonDotter dotter;

        public LectureRowInput(JCheckBox enabled, JPanel checkPanel, JButton downloadButton, JPanel buttonPanel, DownloadButtonDotter dotter) {
            this.enabled = enabled;
            this.checkPanel = checkPanel;
            this.downloadButton = downloadButton;
            this.buttonPanel = buttonPanel;
            this.dotter = dotter;
        }

        public JCheckBox getEnabled() {
            return enabled;
        }

        public JPanel getCheckPanel() {
            return checkPanel;
        }

        public DownloadButtonDotter getButtonDotter() {
            return dotter;
        }

        public JPanel getButtonPanel() {
            return buttonPanel;
        }

        public JButton getDownloadButton() {
            return downloadButton;
        }

    }

}
