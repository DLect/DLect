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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lee
 * @param <T>
 */
public abstract class DLectSwingWorker<T> extends SwingWorker<DLectException, Void> {

    private final ErrorDisplayable displayable;
    private final MainController controller;
    private final ControllerType type;
    private final T parameter;
    private static final Logger LOGGER = LoggerFactory.getLogger(DLectSwingWorker.class);

    public DLectSwingWorker(ErrorDisplayable displayable, MainController controller, ControllerType type, T parameter) {
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

    public T getParameter() {
        return parameter;
    }

    protected abstract void doAction() throws DLectException;

    @Override
    protected final void done() {
        DLectException thrown;
        try {
            thrown = this.get();
        } catch (ExecutionException | InterruptedException ex) {
            thrown = new DLectException(DLectExceptionCause.PROVIDER_CONTRACT, ex);
        }
        if (thrown != null) {
            LOGGER.error("DLect error occured whilst attempting " + type + (parameter != null ? " with parameter: " + parameter : ""), thrown);
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
