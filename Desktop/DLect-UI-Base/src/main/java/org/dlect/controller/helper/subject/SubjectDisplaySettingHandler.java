/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import org.dlect.controller.MainController;
import org.dlect.controller.helper.Initilisable;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.model.Database;
import org.dlect.model.Subject;
import org.dlect.model.Subject.SubjectEventID;

/**
 *
 * @author lee
 */
public class SubjectDisplaySettingHandler implements EventListener, Initilisable {

    private final MainController mc;
    private SubjectDisplaySettingHelper helper;

    public SubjectDisplaySettingHandler(MainController mc) {
        this.mc = mc;
        mc.addListener(this, Database.class, Subject.class);
    }

    @Override
    public void init() {
        helper = new SubjectDisplaySettingHelper(mc.getDatabaseHandler().getDatabase());
    }

    public boolean isSubjectDisplayed(Subject s) {
        return helper.isSubjectDisplayed(s);
    }

    public void setSubjectDisplayed(Subject s, boolean b) {
        helper.setSubjectDisplayed(s, b);
    }

    @Override
    public void processEvent(Event e) {
        if (helper == null) {
            return;
        }
        if (e.getEventID().equals(SubjectEventID.ID)) {
            String before = (String) e.getBefore();
            String after = (String) e.getAfter();

            if (before == null) {
                // Add so ignore.
            } else if (after == null) {
                // remove so remove the key from the map.
                mc.getDatabaseHandler().removeSetting(helper.getKeyFor(before));
            } else {
                // Re-name.
                String oldValue = mc.getDatabaseHandler().getSetting(helper.getKeyFor(before));
                mc.getDatabaseHandler().removeSetting(helper.getKeyFor(before));

                mc.getDatabaseHandler().addSetting(helper.getKeyFor(after), oldValue);
            }
        }
    }

}
