/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.formatter;

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
public class PlaylistFormat extends Formatable<PlaylistFormat> {

    @XmlElement(name = "style")
    private PlaylistStyle style = PlaylistStyle.DISABLED;

    public PlaylistFormat() {
        super(PlaylistFormatEventID.FORMAT);
    }

    public PlaylistStyle getStyle() {
        return style;
    }

    public void setStyle(PlaylistStyle style) {
        EventBuilder<PlaylistStyle> b = event(PlaylistFormatEventID.STYLE).before(getStyle());
        this.style = (style == null ? PlaylistStyle.DISABLED : style);
        b.after(getStyle()).fire();
    }

    public static enum PlaylistFormatEventID implements EventID {

        FORMAT, STYLE;

        @Override
        public Class<?> getAppliedClass() {
            return PlaylistFormat.class;
        }

        @Override
        public String getName() {
            return name();
        }

    }
}
