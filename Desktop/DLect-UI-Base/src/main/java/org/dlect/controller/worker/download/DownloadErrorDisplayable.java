/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.worker.download;

import org.dlect.exception.DLectExceptionCause;
import org.dlect.model.Lecture;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
public interface DownloadErrorDisplayable {

    public void showDownloadError(Subject subject, Lecture lecture, DownloadType downloadType, DLectExceptionCause failureCause);
    
}
