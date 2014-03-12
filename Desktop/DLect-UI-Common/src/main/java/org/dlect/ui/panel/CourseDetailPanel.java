/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.panel;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.HashBiMap;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.dlect.controller.MainController;
import org.dlect.controller.event.ControllerType;
import org.dlect.controller.helper.ControllerStateHelper;
import org.dlect.controller.helper.subject.SubjectInformation;
import org.dlect.controller.worker.ErrorDisplayable;
import org.dlect.controller.worker.LectureWorker;
import org.dlect.controller.worker.download.DownloadErrorDisplayable;
import org.dlect.controller.worker.download.DownloadWorkerHelper;
import org.dlect.event.util.EventIdListings;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.events.wrapper.Wrappers;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.helper.SubjectHelper;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.ui.LeftRightCheck;
import org.dlect.ui.decorator.DownloadButtonDotter;
import org.dlect.ui.layout.TableConstraints;
import org.dlect.ui.layout.TableLayout;
import org.dlect.ui.prefs.subject.AdvancedSubjectPreferencesDialog;

import static org.dlect.controller.helper.SubjectDataHelper.DownloadState.ALL_DOWNLOADED;
import static org.dlect.controller.helper.SubjectDataHelper.DownloadState.NONE_SELECTED;

/**
 *
 * @author lee
 */
public final class CourseDetailPanel extends javax.swing.JPanel implements EventListener, ErrorDisplayable, DownloadErrorDisplayable {

    private static final long serialVersionUID = 1L;

    private Subject subject;
    private final BiMap<Stream, LeftRightCheck> lectureStreams = HashBiMap.create();
    private final BiMap<DownloadType, JCheckBox> downloadTypeCheckBoxes = EnumHashBiMap.create(DownloadType.class);
    private JButton downloadButton;
    private JSeparator leftSeperator;
    private JSeparator rightSeperator;
    private JButton advancedButton;
    private JCheckBox selectAllCheckbox;
    private final MainController controller;
    private final DownloadButtonDotter dbd;
    private LectureWorker worker;

    public CourseDetailPanel(MainController controller) {
        this.controller = controller;
        initComponents();
        dbd = new DownloadButtonDotter(downloadButton);

        Wrappers.addSwingListenerTo(this, this.controller.getControllerStateHelper(), ControllerStateHelper.class);
        refresh();
    }

    public void setSubject(final Subject course) {
        if (subject == course || course == null) {
            return;
        }
        if (subject != null) {
            Wrappers.removeSwingListenerFrom(this, subject);
        }
        subject = course;

        Wrappers.addSwingListenerTo(this, subject, Subject.class, Lecture.class, LectureDownload.class, Stream.class);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    private void initComponents() {
        TableConstraints co;

        leftSeperator = new JSeparator();
        rightSeperator = new JSeparator();
        downloadButton = new JButton();
        advancedButton = new JButton();
        selectAllCheckbox = new JCheckBox();

        setLayout(new TableLayout());

        leftSeperator.setOrientation(SwingConstants.VERTICAL);
        leftSeperator.setMinimumSize(new Dimension(0, 5));
        leftSeperator.setPreferredSize(new Dimension(10, 10));
        co = TableConstraints.create(1);
        add(leftSeperator, co);

        selectAllCheckbox.setText("Select All");
        co = TableConstraints.create(2, 0);
        add(selectAllCheckbox, co);
        selectAllCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean b = selectAllCheckbox.isSelected();
                SubjectHelper.setEnabled(subject, b);
            }
        });
        rightSeperator.setOrientation(SwingConstants.VERTICAL);
        rightSeperator.setMinimumSize(new Dimension(0, 5));
        rightSeperator.setPreferredSize(new Dimension(10, 10));
        co = TableConstraints.create(3);
        add(rightSeperator, co);

        downloadButton.setText("Download");
        co = TableConstraints.create(5, 0);
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadWorkerHelper.downloadAllSelectedIn(CourseDetailPanel.this, controller, subject);
            }
        });
        add(downloadButton, co);

        advancedButton.setText("Advanced...");
        advancedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdvancedSubjectPreferencesDialog(
                        SwingUtilities.windowForComponent(CourseDetailPanel.this),
                        subject,
                        controller
                ).setVisible(true);
            }
        });
        co = TableConstraints.create(5, 1);
        add(advancedButton, co);
    }

    @Override
    public void processEvent(Event e) {
        if (subject == null) {
            return;
        }
        SubjectInformation i = controller.getSubjectDataHelper().getSubjectInformation(e);
        if (EventIdListings.DOWNLOAD_UPDATE_EVENT_IDS.contains(e.getEventID())) {
            updateDownloadButton(i);
        }
        if (EventIdListings.LECTURE_STREAM_UPDATE_EVENT_IDS.contains(e.getEventID())) {
            updateLectureStreams(i);
        }
        if (EventIdListings.DOWNLOAD_TYPE_UPDATE_EVENT_IDS.contains(e.getEventID())) {
            updateDownloadTypeChecks(i);
        }
    }

    public void refresh() {
        if (subject == null) {
            return;
        }
        SubjectInformation i = new SubjectInformation();
        i.setSubject(subject);

        updateDownloadButton(i);
        updateLectureStreams(i);
        updateDownloadTypeChecks(i);
    }

    private void updateDownloadButton(SubjectInformation si) {
        ControllerStateHelper csh = controller.getControllerStateHelper();
        if (!csh.hasCompleted(ControllerType.LECTURE, subject)) {
            dbd.updateAndStart("Loading Data");
        } else if (csh.isDownloading(subject)) {
            dbd.updateAndStart("Downloading");
        } else if (subject.getLectures().isEmpty()) {
            dbd.stop();
            downloadButton.setText("None Detected");
            downloadButton.setEnabled(false);
            this.validate();
        } else {
            si.setSubject(subject);
            switch (si.getDownloadedStatus()) {
                case ALL_DOWNLOADED:
                    dbd.stop();
                    downloadButton.setText("All Downloaded");
                    downloadButton.setEnabled(false);
                    break;
                case NONE_SELECTED:
                    dbd.stop();
                    downloadButton.setText("None Selected");
                    downloadButton.setEnabled(false);
                    break;
                default:
                    dbd.stop();
                    downloadButton.setText("Download");
            }
            this.validate();
        }
    }

    private void updateDownloadTypeChecks(SubjectInformation si) {
        si.setSubject(subject);
        if (downloadTypeCheckBoxes.isEmpty()) {
            addDownloadTypeCheckBoxes(si);
        } else {
            for (Map.Entry<DownloadType, JCheckBox> entry : downloadTypeCheckBoxes.entrySet()) {
                entry.getValue().setSelected(si.isDownloadTypeEnabled(entry.getKey()));
            }
        }
    }

    private void addDownloadTypeCheckBoxes(SubjectInformation si) {
        int row = 1;
        for (final DownloadType dt : DownloadType.values()) {
            final JCheckBox c = new JCheckBox();
            c.setSelected(si.isDownloadTypeEnabled(dt));
            // TODO(Later) change case. \/
            c.setText(dt.toString()); // TODO(Later) i18n
            c.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    SubjectHelper.setDownloadTypeEnabled(subject, dt, c.isSelected());
                }
            });
            TableConstraints co = TableConstraints.create(0, row++);
            add(c, co);
            downloadTypeCheckBoxes.put(dt, c);
        }
    }

    private void updateLectureStreams(SubjectInformation si) {
        si.setSubject(subject);
        Set<Stream> changedStreams = new HashSet<>(subject.getStreams());
        changedStreams.removeAll(lectureStreams.keySet());
        if (changedStreams.isEmpty()) {
            for (Map.Entry<Stream, LeftRightCheck> entry : lectureStreams.entrySet()) {
                Stream stream = entry.getKey();
                LeftRightCheck leftRightCheck = entry.getValue();
                updateExistingLectureStreams(stream, leftRightCheck, si);
            }
        } else {
            cleanLectureStreams();
            GridBagConstraints check = new GridBagConstraints();
            check.gridx = 2;
            check.fill = GridBagConstraints.HORIZONTAL;
            check.anchor = GridBagConstraints.EAST;
            int i = 1;
            for (Stream stream : subject.getStreams()) {
                LeftRightCheck lect = createCheckbox(stream, si);
                lectureStreams.put(stream, lect);
                check.gridy = i;
                CourseDetailPanel.this.add(lect, check);
                i++;
            }
        }
    }

    private LeftRightCheck createCheckbox(final Stream stream, SubjectInformation si) {
        final LeftRightCheck check = new LeftRightCheck();
        check.setRightForeground(whiten(check.getForeground(), 0.5));
        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubjectHelper.setStreamEnabled(subject, stream, check.isSelected());
            }
        });
        updateExistingLectureStreams(stream, check, si);
        return check;
    }

    private Color whiten(Color c, double percentage) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hsb[2] += percentage;
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    private void cleanLectureStreams() {
        Iterator<LeftRightCheck> ilrc = lectureStreams.values().iterator();
        while (ilrc.hasNext()) {
            this.remove(ilrc.next());
            ilrc.remove();
        }
    }

    private void updateExistingLectureStreams(final Stream stream, LeftRightCheck check, SubjectInformation si) {
        int streamCount = si.getStreamLectureCount().count(stream);

        String lectureCount = String.format("%d Lecture%s", streamCount, (streamCount == 1 ? "" : "s"));
        check.setRightText(lectureCount);
        check.setText(stream.getName());
        check.setSelected(si.isStreamEnabled(stream));
    }

    public void loadLectures() {
        if (worker == null) {
            doLoadLecture();
        }
    }

    public void doLoadLecture() {
        worker = new LectureWorker(this, controller, subject);
        worker.execute();
    }

    @Override
    public void showErrorBox(ControllerType type, Object parameter, DLectExceptionCause get) {
        // TODO show error box for lecture event.
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showDownloadError(Subject subject, Lecture lecture, DownloadType downloadType, DLectExceptionCause failureCause) {
        // TODO show error box for download event
    }

}
