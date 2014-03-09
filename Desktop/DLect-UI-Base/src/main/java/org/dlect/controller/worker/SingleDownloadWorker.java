/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.worker;

import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.dlect.controller.MainController;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.model.Lecture;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

import static org.dlect.provider.WrappedProvider.LOGGER;

/**
 *
 * @author lee
 */
public class SingleDownloadWorker extends SwingWorker<DLectException, Void> {

    private final MainController controller;
    private final DownloadErrorDisplayable displayable;
    private final DownloadType downloadType;
    private final Lecture lecture;
    private final Subject subject;

    public SingleDownloadWorker(DownloadErrorDisplayable displayable, MainController controller, Subject subject, Lecture lecture, DownloadType downloadType) {
        this.displayable = displayable;
        this.controller = controller;
        this.subject = subject;
        this.lecture = lecture;
        this.downloadType = downloadType;
    }

    @Override
    protected final void done() {
        DLectException thrown;
        try {
            thrown = this.get();
        } catch (ExecutionException | InterruptedException ex) {
            thrown = new DLectException(DLectExceptionCause.PROVIDER_CONTRACT, ex);
        }
        if (thrown != null) {
            LOGGER.error("DLect error occured whilst attempting download on " + subject + "; " + lecture + "; " + downloadType, thrown);
            displayable.showDownloadError(subject, lecture, downloadType, thrown.getCauseCode());
        }
    }

    @Override
    protected DLectException doInBackground() throws Exception {
        try {
            controller.getDownloadController().downloadLectureDownload(subject, lecture, downloadType);
            return null;
        } catch (DLectException ex) {
            return ex;
        }
    }

}
