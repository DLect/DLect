/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import java.util.SortedSet;

/**
 *
 * @author lee
 */
public class BlackboardPrinter {

    public static final String HEADER = "++-----------------------------------------------------------------------------++";

    public static String toDeepString(Blackboard b) {
        StringBuilder sb = new StringBuilder(HEADER).append("\n");
        sb.append("Blackboard ").append(b.getUsername()).append("\n");
        final SortedSet<Subject> subjects = b.getSubjects();
        toDeepString(subjects, 1, sb);
        sb.append(HEADER).append("\n");
        return sb.toString();
    }

    public static String toDeepString(SortedSet<Subject> allSubjects) {
        return toDeepString(allSubjects, 0);
    }

    private static String toDeepString(final SortedSet<Subject> subjects, int indent) {
        StringBuilder sb = new StringBuilder(HEADER).append("\n");
        toDeepString(subjects, indent, sb);
        return sb.append(HEADER).append("\n").toString();
    }

    public static void toDeepString(final SortedSet<Subject> subjects, int nIndent, StringBuilder sb) {
        StringBuilder indent = buildIndent(nIndent);
        StringBuilder lecInd = new StringBuilder(indent).append("\t\t");
        sb.append(indent).append(subjects.size()).append(" Subjects:");
        for (Subject j : subjects) {
            sb.append(indent).append("\tSubject:").append(j);
            sb.append(indent).append("\t\tName:     ").append(j.getName());
            sb.append(indent).append("\t\tSemester: ").append(j.getSemesterCode());
            final SortedSet<Lecture> lectures = j.getLectures();
            toDeepString(sb, lecInd, lectures);
        }
    }

    public static void toDeepString(StringBuilder sb, StringBuilder indent, final SortedSet<Lecture> lectures) {
        sb.append(indent).append(lectures.size()).append(" Lectures:");
        for (Lecture l : lectures) {
            sb.append(indent).append("\tLecture Date: ").append(l.getTime());
        }
    }

    public static StringBuilder buildIndent(int nIndent) {
        StringBuilder indent = new StringBuilder("");
        for (int j = 0; j < nIndent; j++) {
            indent.append("\t");
        }
        return indent;
    }

    private BlackboardPrinter() {
    }
}
