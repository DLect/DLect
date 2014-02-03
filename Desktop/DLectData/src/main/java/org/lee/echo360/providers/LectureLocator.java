/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers;

import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Subject;

/**
 *
 * @author lee
 */
public interface LectureLocator {

    public ActionResult getLecturesIn(Blackboard b, Subject s);
}
