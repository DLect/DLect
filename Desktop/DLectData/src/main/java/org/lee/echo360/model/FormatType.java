/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import java.util.List;
import org.lee.echo360.util.StringUtil;

/**
 *
 * @author lee
 */
public enum FormatType {

    DATE_ONLY(new Formatter() {
        @Override
        public String format(Subject s, Lecture l) {
            return s.getName() + " - " + StringUtil.formatDate("yyyy/MM/dd HH:mm", l.getTime());
        }
    }, "Subject - Date"),
    DATE_WITH_STREAM(new Formatter() {
        @Override
        public String format(Subject s, Lecture l) {
            return s.getName() + " - " + formatStreams(l, " - ") + StringUtil.formatDate("yyyy/MM/dd HH:mm", l.getTime());
        }
    }, "Subject - Stream - Date"),
    STREAM_WITH_LECTURE_NUMBER(new Formatter() {
        @Override
        public String format(Subject s, Lecture l) {
            return "";
        }
    }, "Subject - Stream - Lecture No.(NYI)");

    private static String formatStreams(Lecture l, String postfix) {
        List<Stream> s = l.getStreams();
        if (s.isEmpty()) {
            return "";
        }
        StringBuilder b = new StringBuilder(s.get(0).getName());
        for (int i = 1; i < s.size(); i++) {
            b.append(", ").append(s.get(i).getName());
        }
        return b.append(postfix).toString();
    }

    public static FormatType getDefault() {
        return DATE_WITH_STREAM;
    }
    /**
     * The formatter fo this style
     */
    private final Formatter lf;
    private final String toString;

    private FormatType(Formatter lf, String toString) {
        this.lf = lf;
        this.toString = toString;
    }

    public String format(Subject s, Lecture l) {
        return this.lf.format(s, l);
    }

    @Override
    public String toString() {
        return toString;
    }

    private static interface Formatter {

        public String format(Subject s, Lecture l);
    }
}
