/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.worker.download;

import org.dlect.controller.MainController;
import org.dlect.model.Lecture;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

public class PreconfiguredSubjectDownloader extends PreconfiguredDownloader {

    private final Subject s;

    public PreconfiguredSubjectDownloader(DownloadErrorDisplayable displayable, MainController mc, Subject s) {
        super(displayable, mc);
        this.s = s;
    }

    public void doDownload(Lecture l, DownloadType dt) {
        super.doDownload(s, l, dt);
    }

}
