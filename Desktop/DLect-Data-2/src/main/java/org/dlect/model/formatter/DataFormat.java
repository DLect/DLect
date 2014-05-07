/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.formatter;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.dlect.events.EventID;
import org.dlect.model.helper.XmlListenable;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class DataFormat<T extends DataFormat<T>> extends XmlListenable<T> {

    @XmlElementWrapper(name = "formats")
    @XmlElement(name = "format")
    private final Map<DownloadType, LectureTitleFormat> formats;

    public DataFormat(EventID formatEventID) {
        this.formats = newWrappedMap(formatEventID);
    }

    public ImmutableMap<DownloadType, LectureTitleFormat> getFormats() {
        return copyOf(formats);
    }

    public LectureTitleFormat getFormat(DownloadType dt) {
        return formats.get(dt);
    }

    public void setFormats(T formatter) {
        if (formatter == null) {
            this.formats.clear();
        } else {
            setFormats(formatter.getFormats());
        }
    }

    public void setFormats(Map<DownloadType, LectureTitleFormat> formats) {
        setMap(this.formats, formats);
    }

    public void setFormat(DownloadType dt, LectureTitleFormat f) {
        formats.put(dt, f);
    }

}
