/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.worker;

import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.dlect.controller.MainController;
import org.dlect.controller.event.ControllerType;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lee
 */
public abstract class DLectSwingWorker extends SwingWorker<DLectException, Object> {

    private final ErrorDisplayable displayable;
    private final MainController controller;
    private final ControllerType type;
    private final Object parameter;

    public DLectSwingWorker(ErrorDisplayable displayable, MainController controller, ControllerType type, Object parameter) {
        this.displayable = displayable;
        this.controller = controller;
        this.type = type;
        this.parameter = parameter;
    }

    public DLectSwingWorker(ErrorDisplayable displayable, MainController controller, ControllerType type) {
        this.displayable = displayable;
        this.controller = controller;
        this.type = type;
        this.parameter = null;
    }

    protected abstract void doAction() throws DLectException;

    @Override
    protected final void done() {
        // TODO show error message if exception thrown
        DLectException thrown;
        try {
            thrown = this.get();
        } catch (ExecutionException | InterruptedException ex) {
            thrown = new DLectException(DLectExceptionCause.INVALID_DATA_FORMAT, ex);
        }
        if (thrown != null) {
            LoggerFactory.getLogger(DLectSwingWorker.class).error("DLect error occured whilst logging in or retrieving the result.", thrown);
            displayable.showErrorBox(type, parameter, thrown.getCauseCode());
        }
    }

    @Override
    protected DLectException doInBackground() throws Exception {
        try {
            doAction();
            return null;
        } catch (DLectException ex) {
            return ex;
        }
    }

    public MainController getController() {
        return controller;
    }

}
