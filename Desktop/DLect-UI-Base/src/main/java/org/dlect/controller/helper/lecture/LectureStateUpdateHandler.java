/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.lecture;

import org.dlect.controller.helper.subject.SubjectInformation;
import org.dlect.helper.SubjectHelper;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public abstract class LectureStateUpdateHandler {

    private final Subject subject;
    private SubjectInformation subjectInformation;

    public LectureStateUpdateHandler(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    public final void init() {
        subjectInformation = new SubjectInformation();
        subjectInformation.setSubject(subject);
        try {
            initImpl();
        } finally {
            subjectInformation = null;
        }
    }

    public final void updateLectures() {
        subjectInformation = new SubjectInformation();
        subjectInformation.setSubject(subject);
        try {
            updateLecturesImpl();
        } finally {
            subjectInformation = null;
        }
    }

    protected abstract void initImpl();

    protected abstract void updateLecturesImpl();

    public SubjectInformation getSubjectInformation() {
        if (subjectInformation == null) {
            SubjectInformation si = new SubjectInformation();
            si.setSubject(subject);
            return si;
        }
        subjectInformation.setSubject(subject);
        return subjectInformation;
    }

    public void setDownloadTypeEnabled(DownloadType downloadType, boolean b) {
        SubjectHelper.setDownloadTypeEnabled(subject, downloadType, b);
    }

    public boolean isDownloadTypeEnabled(DownloadType downloadType) {
        return getSubjectInformation().isDownloadTypeEnabled(downloadType);
    }

    public void setStreamEnabled(Stream stream, boolean b) {
        SubjectHelper.setStreamEnabled(subject, stream, b);
    }

    public boolean isStreamEnabled(Stream stream) {
        return getSubjectInformation().isStreamEnabled(stream);
    }
}
