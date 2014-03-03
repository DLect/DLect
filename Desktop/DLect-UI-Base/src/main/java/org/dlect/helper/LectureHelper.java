/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.helper;

import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;

/**
 *
 * @author lee
 */
public class LectureHelper {
    
    public static boolean isLectureDownloadEnabled(Lecture l, LectureDownload ld) {
        return ld.isDownloaded() || (l.isEnabled() && ld.isDownloadEnabled());
    }
    
}
