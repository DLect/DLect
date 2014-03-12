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

/**
 *
 * @author lee
 */
public class PreconfiguredDownloader {

    DownloadErrorDisplayable displayable;
    MainController mc;

    public PreconfiguredDownloader(DownloadErrorDisplayable displayable, MainController mc) {
        this.displayable = displayable;
        this.mc = mc;
    }

    public void doDownload(Subject s, Lecture l, DownloadType dt) {
        // TODO change this.
        new SingleDownloadWorker(displayable, mc, s, l, dt).execute();
    }

}
