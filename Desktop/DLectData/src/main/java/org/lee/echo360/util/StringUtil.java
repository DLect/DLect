/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author lee
 */
public class StringUtil {

    public static final String XML_DATE_FORMAT_WITH_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String XML_DATE_FORMAT_ZULU = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final DateFormat xmlTimeZoneDateFormat = new SimpleDateFormat(XML_DATE_FORMAT_WITH_TIME_ZONE);
    private static final DateFormat xmlZuluDateFormat = new SimpleDateFormat(XML_DATE_FORMAT_ZULU);
    private static final LoadingCache<String, Pattern> cache = CacheBuilder.newBuilder().softValues().expireAfterAccess(10, TimeUnit.SECONDS).build(new CacheLoader<String, Pattern>() {
        @Override
        public Pattern load(String key) throws Exception {
            return Pattern.compile(key);
        }
    });

    public static int parseInteger(String textContent) {
        try {
            return Integer.parseInt(textContent);
        } catch (NumberFormatException ex) {
            ExceptionReporter.reportException(ex);
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

    public static Date parseDate(String value) {
        try {
            synchronized (xmlTimeZoneDateFormat) {
                return xmlTimeZoneDateFormat.parse(value);
            }
        } catch (ParseException ex) {
            ExceptionReporter.reportException(ex);
            try {
                synchronized (xmlZuluDateFormat) {
                    return xmlZuluDateFormat.parse(value);
                }
            } catch (ParseException ex1) {
                ExceptionReporter.reportException(ex1);
                return null;
            }
        }
    }

    public static String regex(String str, String reg, int group, String noMatch) {
        try {
            if (str == null || reg == null || group < 0) {
                return noMatch;
            }
            Matcher m = cache.get(reg).matcher(str);
            if (m.find()) {
                if (group <= m.groupCount()) {
                    return m.group(group);
                }
            }
            return noMatch;
        } catch (ExecutionException ex) {
            ExceptionReporter.reportException(ex);
            return noMatch;
        }
    }

    public static String formatDate(String format, Date time) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return (time == null ? "" : df.format(time));
    }

    public static String getStringFromStream(InputStream content) {
        try {
            return IOUtils.toString(content);
        } catch (IOException ex) {
            ExceptionReporter.reportException(ex);
            return null;
        } finally {
            try {
                content.close();
            } catch (IOException ex) {
                ExceptionReporter.reportException(ex);
            }
        }
    }

    private StringUtil() {
    }
}
