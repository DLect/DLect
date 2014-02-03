/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers;

import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;

/**
 *
 * @author lee
 */
public interface LoginExecuter {

    /**
     * Performs the login using the credentials stored in the blackboard object.
     *
     * @param b The blackboard object that contains the credentials.
     *
     * @return
     */
    public ActionResult doLogin(Blackboard b);
}
