/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.model.formatter;

import org.dlect.model.Lecture;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public class LectureFormat {
    
    String formatString;

    public LectureFormat() {
    }

    public LectureFormat(String formatString) {
        this.formatString = formatString;
    }
    
    public String format(Subject s, Lecture l) {
        return formatString;
    }
    
}
