/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helper.formattable;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author lee
 */
public class Formattables {

    private static final String TAB = "    ";

    protected static String getIndent(int indent) {
        if (indent == 0) {
            return "";
        } else if (indent == 1) {
            return " ";
        } else if (indent == 2) {
            return "  ";
        } else {
            StringBuilder b = new StringBuilder(indent);
            for (int j = 0; j < indent; j++) {
                b.append(' ');
            }
            return b.toString();
        }
    }

    public static String toString(Formattable f) {
        return toString(0, f);
    }

    public static String toString(int indent, Formattable f) {
        StringBuilder b = new StringBuilder();
        toString(b, indent, f);
        return b.toString();
    }

    public static void toString(StringBuilder b, int indent, Formattable f) {
        b.append(f.getClass()).append(" {\n");

        String dtab = getIndent(indent);
        String tab = dtab + TAB;
        
        Map<String, Object> formater = f.getObjectsToFormat();

        for (Entry<String, Object> entry : formater.entrySet()) {
            String name = entry.getKey();
            Object object = entry.getValue();

            b.append(tab).append(name).append('=');

            if (object instanceof Formattable) {
                toString(b, indent + 4, (Formattable) object);
            } else {
                b.append(object);
            }

            b.append('\n');
        }
        b.append(dtab).append('}');
    }

}
