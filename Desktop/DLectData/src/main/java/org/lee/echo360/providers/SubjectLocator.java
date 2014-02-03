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
public interface SubjectLocator {

    public ActionResult getSubjects(Blackboard b);
}
