/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import java.util.ArrayList;
import java.util.List;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.exceptions.InvalidImplemetationException;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.BlackboardProviderWrapper;

/**
 *
 * @author Lee Symes
 */
public class SubjectController {

    private final MainController ctl;

    public SubjectController(MainController ctl) {
        this.ctl = ctl;
    }

    public void getSubjects() {
        ctl.start(ControllerAction.COURSES);
        Blackboard b = ctl.getPropertiesController().getBlackboard();
        BlackboardProviderWrapper w = ctl.getPropertiesController().getProvider();
        List<Subject> oldSubjs = new ArrayList<Subject>(b.getSubjects());
        ActionResult res = null;
        try {
            res = w.getSubjects(b);
        } catch (InvalidImplemetationException ex) {
            res = ActionResult.FATAL;
        } finally {
            try {
                if (!b.getSubjects().isEmpty() && res == ActionResult.SUCCEDED) {
                    List<Subject> newSubjs = new ArrayList(b.getSubjects());
                    newSubjs.removeAll(oldSubjs);
                    int maxSem = b.getSubjects().first().getSemesterCode();
                    for (Subject subject : newSubjs) {
                        // Enable the new subjects with the highest semester code.
                        if (subject.getSemesterCode() == maxSem) {
                            subject.setDownloadEnabled(true);
                        }
                    }
                    PropertiesSavingController.saveProperties(ctl);
                }
            } finally {
                ctl.finished(ControllerAction.COURSES, res);
            }
        }
    }
}
