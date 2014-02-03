/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.exceptions.InvalidImplemetationException;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.BlackboardProviderWrapper;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author Lee Symes
 */
public class LectureController {

    private final MainController ctl;

    public LectureController(MainController ctl) {
        this.ctl = ctl;
    }

    public void updateInformation(Subject s, Lecture l) {
        PropertiesController pc = ctl.getPropertiesController();
        for (DownloadType downloadType : DownloadType.values()) {
            l.setFilePresent(downloadType, pc.getFileFor(s, l, downloadType).exists());
        }
    }

    public void updateInformation(Subject s) {
        for (Lecture lecture : s.getLectures()) {
            updateInformation(s, lecture);
        }
    }

    public void getLecturesFor(final Subject subject) {
        final Blackboard b = ctl.getPropertiesController().getBlackboard();
        final BlackboardProviderWrapper prov = ctl.getPropertiesController().getProvider();
        boolean allocateDefaultLecture = subject.getStreams().isEmpty();
        try {
            prov.getLecturesIn(b, subject);
        } catch (InvalidImplemetationException ex) {
            ExceptionReporter.reportException(ex);
        }
        for (final Lecture l : subject.getLectures()) {
            updateInformation(subject, l);
        }
        if (allocateDefaultLecture) {
            int lecStr = -1;
            Stream maxSt = null;
            for (Stream stream : subject.getStreams()) {
                if (stream.isActualStream() && stream.getCount() > lecStr) {
                    lecStr = stream.getCount();
                    maxSt = stream;
                }
            }
            if (maxSt != null) {
                maxSt.setEnabled(true);
            }
        }
        PropertiesSavingController.saveProperties(ctl);

    }

    public void getAllLectures() {
        ctl.start(ControllerAction.LECTURES);
        try {
            final Blackboard b = ctl.getPropertiesController().getBlackboard();
            new DeadlockReportingThread(ctl, ControllerAction.LECTURES, Thread.currentThread(), 60000, 1000).start();
            for (final Subject subject : b.getSubjects()) {
                try {
                    getLecturesFor(subject);
                } catch (Throwable t) {
                    ExceptionReporter.reportException(t);
                }
            }
        } finally {
            System.out.println("In Finally");
            ctl.finished(ControllerAction.LECTURES, ActionResult.SUCCEDED);
            System.out.println("Bye");
        }
    }
}
