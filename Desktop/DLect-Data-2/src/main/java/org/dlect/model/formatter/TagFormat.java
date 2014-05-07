/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.formatter;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import org.dlect.events.EventID;
import org.dlect.events.listenable.EventBuilder;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TagFormat extends DataFormat<TagFormat> {

    @XmlElement(name = "enabled")
    private boolean enabled;

    public TagFormat() {
        super(TagFormatterEventID.FORMAT);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        EventBuilder<Boolean> b = event(TagFormatterEventID.ENABLED).before(isEnabled());
        this.enabled = enabled;
        b.after(isEnabled()).fire();
    }

    @Override
    public Map<String, Object> getObjectsToFormat() {
        return build().put("enabled", enabled).build();
    }

    public static enum TagFormatterEventID implements EventID {

        ENABLED,
        FORMAT;

        @Override
        public Class<?> getAppliedClass() {
            return TagFormat.class;
        }

        @Override
        public String getName() {
            return name();
        }

    }

}
