/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.exceptions.InvalidImplemetationException;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.providers.BlackboardProviderWrapper;

/**
 *
 * @author Lee Symes
 */
public class LoginController {

    private final MainController ctl;

    public LoginController(MainController ctl) {
        this.ctl = ctl;
    }

    /**
     * This method Blocks
     *
     * @return The Result. Failed if the implementation threw an exception.
     * Fatal, if something else threw an exception. The result of the login from
     * the provider otherwise.
     */
    public ActionResult doLogin() {
        BlackboardProviderWrapper w = ctl.getPropertiesController().getProvider();
        Blackboard b = ctl.getPropertiesController().getBlackboard();
        ctl.start(ControllerAction.LOGIN);
        ActionResult r = ActionResult.FATAL;
        try {
            r = w.doLogin(b);
        } catch (InvalidImplemetationException ex) {
            r = ActionResult.FAILED;
        } finally {
            ctl.finished(ControllerAction.LOGIN, r);
        }
        return r;
    }
}
