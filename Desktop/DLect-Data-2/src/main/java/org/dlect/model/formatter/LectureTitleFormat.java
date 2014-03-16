/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.formatter;

import com.google.common.base.Joiner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlEnum;
import org.dlect.model.Lecture;
import org.dlect.model.Stream;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
@XmlEnum(String.class)
public enum LectureTitleFormat {

    DATE_ONLY() {
                @Override
                public String format(Subject s, Lecture l) {
                    return s.getName() + " - " + formatDate("yyyy/MM/dd HH:mm", l.getTime());
                }

            },
    DATE_WITH_STREAM() {
                @Override
                public String format(Subject s, Lecture l) {
                    return s.getName() + " - " + formatStreams(l, " - ") + formatDate("yyyy/MM/dd HH:mm", l.getTime());
                }
            };
//    STREAM_WITH_LECTURE_NUMBER(new Formatter() {
//        @Override
//        public String format(Subject s, Lecture l) {
//            return "";
//        }
//    }, "Subject - Stream - Lecture No.(NYI)");

    private static String formatDate(String format, Date time) {
        return new SimpleDateFormat(format).format(time);
    }

    private static String formatStreams(Lecture l, String postfix) {
        List<Stream> s = l.getStreams().asList();
        if (s.isEmpty()) {
            return "";
        } else {
            return Joiner.on(", ").join(s) + postfix;
        }
    }

    public abstract String format(Subject s, Lecture l);

}
