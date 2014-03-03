/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper.subject;

import java.util.Map.Entry;
import org.dlect.controller.MainController;
import org.dlect.events.Event;
import org.dlect.events.EventID;
import org.dlect.events.EventListener;
import org.dlect.events.listenable.Listenable;
import org.dlect.model.Database;
import org.dlect.model.Database.DatabaseEventID;
import org.dlect.model.Subject;
import org.dlect.model.Subject.SubjectEventID;

/**
 *
 * @author lee
 */
public class SubjectDisplayHelper extends Listenable<SubjectDisplayHelper> implements EventListener {

    private static final String KEY_PREFIX = "SUBJECT_DISPLAYED_BBID::";

    private final MainController mc;

    public SubjectDisplayHelper(MainController mc) {
        this.mc = mc;
        mc.getDatabaseHandler().addListener(this, Database.class, Subject.class);
    }

    private String getKeyFor(Subject s) {
        return getKeyFor(s.getId());
    }

    private String getKeyFor(String bbid) {
        return KEY_PREFIX + bbid;
    }

    public boolean isSubjectDisplayed(Subject s) {
        String displayedKey = getKeyFor(s);
        String displayed = mc.getDatabaseHandler().getSetting(displayedKey);
        if (displayed == null) {
            setSubjectDisplayed(s, false);
        }
        return Boolean.valueOf(displayed);
    }

    @Override
    public void processEvent(Event e) {
//        if (e.getEventID().equals(DatabaseEventID.SETTING)) {
//            Entry<?, ?> before = (Entry<?, ?>) e.getBefore();
//            Entry<?, ?> after = (Entry<?, ?>) e.getAfter();
//
//            boolean dispBefore = valueToBoolean(before);
//            boolean dispAfter = valueToBoolean(after);
//
//            event(SubjectDisplayHelperEventID.SUBJECT_DISPLAY_CHANGED_EVENT).before(dispBefore).after(dispAfter).fire();
//        } else 
        if (e.getEventID().equals(SubjectEventID.ID)) {
            String before = (String) e.getBefore();
            String after = (String) e.getAfter();

            if (before == null) {
                // Add so ignore.
            } else if (after == null) {
                // remove so remove the key from the map.
                mc.getDatabaseHandler().removeSetting(getKeyFor(before));
            } else {
                // Re-name.
                String oldValue = mc.getDatabaseHandler().getSetting(getKeyFor(before));
                mc.getDatabaseHandler().removeSetting(getKeyFor(before));

                mc.getDatabaseHandler().addSetting(getKeyFor(after), oldValue);
            }
        }
    }
//
//    private boolean valueToBoolean(Entry<?, ?> entry) {
//        if (entry == null) {
//            return false;
//        } else if (entry.getValue() == null) {
//            return false;
//        } else {
//            return Boolean.parseBoolean(entry.getValue().toString());
//        }
//    }

    private void setSubjectDisplayed(Subject s, boolean b) {
        String displayedKey = getKeyFor(s);

        mc.getDatabaseHandler().addSetting(displayedKey, Boolean.toString(b));
    }
}
