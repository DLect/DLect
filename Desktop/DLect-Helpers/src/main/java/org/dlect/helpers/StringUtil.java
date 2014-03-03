/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lee
 */
public class StringUtil {

    public static final String XML_DATE_FORMAT_WITH_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String XML_DATE_FORMAT_ZULU = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final DateFormat xmlTimeZoneDateFormat = new SimpleDateFormat(XML_DATE_FORMAT_WITH_TIME_ZONE);
    private static final DateFormat xmlZuluDateFormat = new SimpleDateFormat(XML_DATE_FORMAT_ZULU);

    public static int parseInteger(String textContent) {
        try {
            return Integer.parseInt(textContent);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public static boolean parseBoolean(String textContent) {
        try {
            return Integer.parseInt(textContent) != 0;
        } catch (NumberFormatException ex) {
            return Boolean.parseBoolean(textContent);
        }
    }

    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return URLDecoder.decode(url);
        }
    }

    public static Date parseDate(String value) {
        try {
            synchronized (xmlTimeZoneDateFormat) {
                return xmlTimeZoneDateFormat.parse(value);
            }
        } catch (ParseException ex) {
            try {
                synchronized (xmlZuluDateFormat) {
                    return xmlZuluDateFormat.parse(value);
                }
            } catch (ParseException ex1) {
                return null;
            }
        }
    }

    public static String regex(String str, String reg, int group, String noMatch) {
        if (str == null || reg == null || group < 0) {
            return noMatch;
        }
        Matcher m = Pattern.compile(reg).matcher(str);
        if (m.find()) {
            if (group <= m.groupCount()) {
                return m.group(group);
            }
        }
        return noMatch;
    }

    public static String regex(String str, Pattern reg, int group, String noMatch) {
        if (str == null || reg == null || group < 0) {
            return noMatch;
        }
        Matcher m = reg.matcher(str);
        if (m.find()) {
            if (group <= m.groupCount()) {
                return m.group(group);
            }
        }
        return noMatch;
    }

    public static String formatDate(String format, Date time) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return (time == null ? "" : df.format(time));
    }

    private StringUtil() {
    }
}
