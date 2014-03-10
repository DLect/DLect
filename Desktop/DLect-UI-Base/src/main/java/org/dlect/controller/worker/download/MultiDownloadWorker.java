/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.worker.download;

import com.google.common.collect.ImmutableSet;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingWorker;
import org.dlect.controller.MainController;
import org.dlect.controller.download.event.DownloadParameter;
import org.dlect.helper.Conditions;

/**
 *
 * @author lee
 */
public class MultiDownloadWorker extends SwingWorker<Void, Void> {

    private final MainController controller;
    private final DownloadErrorDisplayable displayable;
    private final Collection<DownloadParameter> toDownload;

    public MultiDownloadWorker(DownloadErrorDisplayable displayable, MainController controller, Collection<DownloadParameter> toDownload) {
        Conditions.checkNonNull(displayable, "Download Error Displayable");
        Conditions.checkNonNull(controller, "Main Controller");
        Conditions.checkNonNull(toDownload, "Download Parameter List");
        this.displayable = displayable;
        this.controller = controller;
        this.toDownload = ImmutableSet.copyOf(toDownload);
    }

    @Override
    protected Void doInBackground() throws Exception {
        ExecutorService service = Executors.newSingleThreadExecutor();
        for (DownloadParameter param : toDownload) {
            System.out.println("Running Param: " + param);
            System.out.println(param.getSubject() + "\n" + param.getLecture() + "\n" + param.getDownloadType());
            service.submit(new SingleDownloadWorker(displayable, controller, param.getSubject(), param.getLecture(), param.getDownloadType()));
        }
        service.shutdown();
        return null;
    }

}
