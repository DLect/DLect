/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui.prefs;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.control.controllers.DownloadProgressListener;
import org.lee.echo360.control.controllers.MainController;
import org.lee.echo360.control.controllers.PropertiesController;
import org.lee.echo360.control.controllers.PropertiesSavingController;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.lee.echo360.ui.SwingWorkerProgressDialog;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class MoveWorker extends SwingWorkerProgressDialog.Worker {

    private final File from;
    private final File to;
    private final MainController c;
    private final Object WIAT_FOR_COMPLETION_LOCK = new Object();
    private int val = 0;

    public MoveWorker(File from, File to, MainController c) {
        this.from = from;
        this.to = to;
        this.c = c;
    }

    @Override
    protected String getDescription() {
        return "Moving your subjects over.";
    }

    @Override
    protected void doAction() throws Exception {
        int max = c.getPropertiesController().getBlackboard().getSubjects().size() * 3;
        max += 3;
        this.setMax(max);
        publish(of("Acquiring Llamas", val++));
        try {
            acquireLocks();
            waitForCompletion();
            acquireExecutionLock();
            publish(of("Enough Llamas Acquired. Ready to go.", val++));
            moveProperties();
        } finally {
            releaseLocks();
            publish(of("Done. Released Llamas", max));
        }
    }

    private void acquireLocks() {
        PropertiesSavingController.SAVE_LOAD_LOCK.lock();
        c.DOWNLOAD_LOCK_OUT.lock();
    }

    private void acquireExecutionLock() {
        c.EXECUTION_LOCK_OUT.lock();
    }

    private void releaseLocks() {
        while (PropertiesSavingController.SAVE_LOAD_LOCK.isLocked()) {
            PropertiesSavingController.SAVE_LOAD_LOCK.unlock();
        }
        while (c.DOWNLOAD_LOCK_OUT.isLocked()) {
            c.DOWNLOAD_LOCK_OUT.unlock();
        }
        while (c.EXECUTION_LOCK_OUT.isLocked()) {
            c.EXECUTION_LOCK_OUT.unlock();
        }
        PropertiesSavingController.saveProperties(c);
    }

    protected boolean moveProperties() { // TODO implement this to copy across, then once successful delete the old files.
        PropertiesController pc = c.getPropertiesController();
        Blackboard b = pc.getBlackboard();
        try {
            FileUtils.copyFileToDirectory(PropertiesSavingController.getDataFile(from), to, true);
            for (Subject s : b.getSubjects()) {
                publish(of("Moving " + s.getName(), val++));
                copyAllIn(from, to, s, pc);
                publish(of("Done moving " + s.getName(), val++));
            }
            PropertiesSavingController.getDataFile(from).delete();
            for (Subject subject : b.getSubjects()) {
                removeAllFrom(from, subject, pc);
                publish(of("Removing Old " + subject.getName() + " Files", val++));
            }
            c.getApplicationPropertiesController().setDataDirectory(to);
            for (Subject subject : b.getSubjects()) {
                c.getLectureController().updateInformation(subject);
            }
            return true;
        } catch (Throwable ex) {
            ExceptionReporter.reportException(ex);
            return false;
        }
    }

    private void copyAllIn(File fromDir, File toDir, Subject s, PropertiesController pc) {
        // TODO implement moving playlists
        for (Lecture l : s.getLectures()) {
            for (DownloadType dt : DownloadType.values()) {
                File f = pc.getFileFor(fromDir, s, l, dt);
                File t = pc.getFileFor(toDir, s, l, dt);
                if (f.exists()) {
                    try {
                        if (t.exists()) {
                            if (!isNewer(f, t)) {
                                continue;
                            } else {
                                t.delete();
                            }
                        }
                        FileUtils.copyFile(f, t);
                        c.getLectureController().updateInformation(s, l);
                    } catch (IOException ex) {
                        ExceptionReporter.reportException(ex);
                    }
                }
            }
        }
    }

    private void removeAllFrom(File fromDir, Subject s, PropertiesController pc) {
        if (pc.getFolderFor(fromDir, s).exists()) {
            for (Lecture l : s.getLectures()) {
                for (DownloadType dt : DownloadType.values()) {
                    pc.getFileFor(fromDir, s, l, dt).delete();
                }
            }
            if (pc.getFolderFor(fromDir, s).list().length == 0) {
                pc.getFolderFor(fromDir, s).delete();
            }
        }
    }

    private boolean isNewer(File fileA, File fileB) {
        return fileA.lastModified() >= fileB.lastModified();
    }

    private void waitForCompletion() {
        if (!c.hasCompleted(ControllerAction.LECTURES) || !c.getSubjectLecutreDownloading().isEmpty()) {
            DownloadProgressListenerNotifier dpln = new DownloadProgressListenerNotifier();
            ControllerListenerNotifier cln = new ControllerListenerNotifier();
            c.addControllerListener(cln);
            c.addDownloadProgressListener(dpln);
            while (!c.hasCompleted(ControllerAction.LECTURES) || !c.getSubjectLecutreDownloading().isEmpty()) {
                synchronized (WIAT_FOR_COMPLETION_LOCK) {
                    try {
                        WIAT_FOR_COMPLETION_LOCK.wait(1000);
                    } catch (InterruptedException ex) {
                        // No Op
                    }
                }
            }
            c.removeControllerListener(cln);
            c.removeDownloadProgressListener(dpln);
        }
    }

    private class ControllerListenerNotifier implements ControllerListener {

        @Override
        public void start(ControllerAction action) {
            synchronized (WIAT_FOR_COMPLETION_LOCK) {
                WIAT_FOR_COMPLETION_LOCK.notifyAll();
            }
        }

        @Override
        public void finished(ControllerAction action, ActionResult r) {
            synchronized (WIAT_FOR_COMPLETION_LOCK) {
                WIAT_FOR_COMPLETION_LOCK.notifyAll();
            }
            if (action == ControllerAction.LECTURES) {
                acquireExecutionLock();
            }
        }

        @Override
        public void error(Throwable e) {
            synchronized (WIAT_FOR_COMPLETION_LOCK) {
                WIAT_FOR_COMPLETION_LOCK.notifyAll();
            }
        }
    }

    private class DownloadProgressListenerNotifier implements DownloadProgressListener {

        @Override
        public void downloadingStarted(Subject subject) {
            synchronized (WIAT_FOR_COMPLETION_LOCK) {
                WIAT_FOR_COMPLETION_LOCK.notifyAll();
            }
        }

        @Override
        public void downloadingFinished(Subject subject) {
            synchronized (WIAT_FOR_COMPLETION_LOCK) {
                WIAT_FOR_COMPLETION_LOCK.notifyAll();
            }
        }

        @Override
        public void downloadStarting(Subject s, Lecture l, DownloadType t) {
            synchronized (WIAT_FOR_COMPLETION_LOCK) {
                WIAT_FOR_COMPLETION_LOCK.notifyAll();
            }
        }

        @Override
        public void downloadCompleted(Subject s, Lecture l, DownloadType t) {
            synchronized (WIAT_FOR_COMPLETION_LOCK) {
                WIAT_FOR_COMPLETION_LOCK.notifyAll();
            }
        }
    }
}
