/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.formatter;

import java.util.Map;
import org.dlect.events.listenable.Listenable;

/**
 *
 * @author lee
 */
public class TagFormatter extends Listenable<TagFormatter> {

    Map<DownloadType, LectureFormat> formats;

    public TagFormatter() {
    }

    public void setFormat(DownloadType dt, LectureFormat format) {
        this.formats.put(dt, format);
    }

    public LectureFormat getFormat(DownloadType dt) {
        return formats.get(dt);
    }

}
