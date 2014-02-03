/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.HashBiMap;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.control.controllers.DownloadProgressListener;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;
import org.lee.echo360.ui.advanced.AdvancedSubjectPreferencesDialog;
import org.lee.echo360.ui.layout.TableConstraints;
import org.lee.echo360.ui.layout.TableLayout;
import org.lee.echo360.ui.util.ColorUtil;
import org.lee.echo360.util.SubjectHelper;
import org.lee.echo360.util.ThreadUtil;

/**
 *
 * @author lee
 */
public final class CourseDetailPanel extends javax.swing.JPanel implements
        PropertyChangeListener, ControllerListener, DownloadProgressListener {

    private final CheckBoxUpdateWorker checkBoxUpdateWorker = new CheckBoxUpdateWorker();
    private Subject subject;
    private BiMap<Stream, LeftRightCheck> lectureStreams = HashBiMap.create();
    private BiMap<DownloadType, JCheckBox> downloadTypeCheckBoxes = EnumHashBiMap.create(DownloadType.class);
    private JButton downloadButton;
    private JSeparator leftSeperator;
    private JSeparator rightSeperator;
    private JButton advancedButton;
    private JCheckBox selectAllCheckbox;
    private final MainController controller;
    private final DownloadButtonDotter dbd;

    public CourseDetailPanel(MainController controller) {
        this.controller = controller;
        initComponents();
        dbd = new DownloadButtonDotter(downloadButton);
        controller.addControllerListener(this);
        controller.addDownloadProgressListener(this);
        refresh();
    }

    public final void setSubject(final Subject course) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (subject == course || course == null) {
                    return;
                }
                subject = course;
                course.addPropertyChangeListener(CourseDetailPanel.this);
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
                for (Stream stream : subject.getStreams()) {
                    stream.setEnabled(b);
                }
                CourseDetailPanel.this.repaint();
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
                ThreadUtil.runExecution(new Runnable() {
                    @Override
                    public void run() {
                        controller.getDownloadController().downloadSubject(subject);
                    }
                });
            }
        });
        add(downloadButton, co);

        advancedButton.setText("Advanced...");
        advancedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdvancedSubjectPreferencesDialog(SwingUtilities.windowForComponent(CourseDetailPanel.this), subject, controller).setVisible(true);
            }
        });
        co = TableConstraints.create(5, 1);
        add(advancedButton, co);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        refresh();
    }

    @Override
    public void start(final ControllerAction action) {
        refresh();
    }

    @Override
    public void finished(final ControllerAction action, final ActionResult ar) {
        refresh();
    }

    @Override
    public void error(Throwable thrwbl) {
        //No Op
    }

    @Override
    public void downloadingStarted(Subject subject) {
        if (this.subject.equals(subject)) {
            refresh();
        }
    }

    @Override
    public void downloadingFinished(Subject subject) {
        if (this.subject.equals(subject)) {
            refresh();
        }
    }

    @Override
    public void downloadStarting(Subject s, Lecture l, DownloadType t) {
        if (this.subject.equals(s)) {
            refresh();
        }
    }

    @Override
    public void downloadCompleted(Subject s, Lecture l, DownloadType t) {
        if (this.subject.equals(s)) {
            refresh();
        }
    }

    public void refresh() {
        checkBoxUpdateWorker.update();
    }

    private void updateDownloadButton() {
        if (!controller.hasCompleted(ControllerAction.LECTURES)) {
            dbd.updateAndStart("Loading Data");
        } else if (!controller.getSubjectLecutreDownloading(subject).isEmpty()) {
            dbd.updateAndStart("Downloading");
        } else if (subject.getLectures().isEmpty()) {
            dbd.stop();
            downloadButton.setText("None Detected");
            downloadButton.setEnabled(false);
        } else {
            switch (SubjectHelper.getSubjectSelectionState(subject)) {
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
        }
    }

    private void updateDownloadTypeChecks() {
        if (downloadTypeCheckBoxes.isEmpty()) {
            addDownloadTypeCheckBoxes();
        } else {
            for (Map.Entry<DownloadType, JCheckBox> entry : downloadTypeCheckBoxes.entrySet()) {
                entry.getValue().setSelected(subject.isDownloadTypeEnabled(entry.getKey()));
                // TODO disable or otherwise button when all are downloaded.
            }
        }
    }

    private synchronized void updateLectureStreams() {
        Set<Stream> s = new HashSet<Stream>(subject.getStreams());
        s.removeAll(lectureStreams.keySet());
        if (s.isEmpty()) {
            for (Map.Entry<Stream, LeftRightCheck> entry : lectureStreams.entrySet()) {
                Stream stream = entry.getKey();
                LeftRightCheck leftRightCheck = entry.getValue();
                updateExistingLectureStreams(stream, leftRightCheck);
            }
        } else {
            cleanLectureStreams();
            GridBagConstraints check = new GridBagConstraints();
            check.gridx = 2;
            check.fill = GridBagConstraints.HORIZONTAL;
            check.anchor = GridBagConstraints.EAST;
            int i = 1;
            for (Stream stream : subject.getStreams()) {
                LeftRightCheck lect = createCheckbox(stream);
                lectureStreams.put(stream, lect);
                check.gridy = i;
                CourseDetailPanel.this.add(lect, check);
                i++;
            }
        }
    }

    private void addDownloadTypeCheckBoxes() {
        TableConstraints co;
        ActionListener checkListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(e.getSource() instanceof JCheckBox)) {
                    return;
                }
                JCheckBox b = (JCheckBox) e.getSource();
                DownloadType dt = downloadTypeCheckBoxes.inverse().get(b);
                subject.setDownloadTypeEnabled(dt, b.isSelected());
            }
        };
        int row = 1;
        for (DownloadType dt : DownloadType.values()) {
            JCheckBox c = new JCheckBox();
            c.setSelected(subject.isDownloadTypeEnabled(dt));
            c.setText(dt.toString());
            co = TableConstraints.create(0, row++);
            add(c, co);
            c.addActionListener(checkListener);
            downloadTypeCheckBoxes.put(dt, c);
        }
    }

    private LeftRightCheck createCheckbox(final Stream stream) {
        String lectureCount = String.format("%d Lecture%s", stream.getCount(), (stream.getCount() == 1 ? "" : "s"));
        final LeftRightCheck check = new LeftRightCheck(stream.getName(), lectureCount, stream.isEnabled());
        check.setRightForeground(ColorUtil.whiten(check.getForeground(), 0.5));
        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stream.setEnabled(check.isSelected());
            }
        });
        return check;
    }

    private void cleanLectureStreams() {
        Iterator<LeftRightCheck> ilrc = lectureStreams.values().iterator();
        while (ilrc.hasNext()) {
            this.remove(ilrc.next());
            ilrc.remove();
        }
    }

    private void updateExistingLectureStreams(final Stream stream, LeftRightCheck check) { // TODO
        String lectureCount = String.format("%d Lecture%s", stream.getCount(), (stream.getCount() == 1 ? "" : "s"));
        check.setRightText(lectureCount);
        check.setText(stream.getName());
        check.setSelected(stream.isEnabled());
    }

    private class CheckBoxUpdateWorker extends SwingWorker<Void, Boolean> {

        @Override
        protected void process(List<Boolean> chunks) {
            if (subject != null) {
                updateDownloadButton();
                updateLectureStreams();
                updateDownloadTypeChecks();
                for (Stream stream : subject.getStreams()) {
                    updateExistingLectureStreams(stream, lectureStreams.get(stream));
                }
            }
        }

        @Override
        protected Void doInBackground() throws Exception {
            return null;
        }

        private void update() {
            publish(true);
        }
    }
}
