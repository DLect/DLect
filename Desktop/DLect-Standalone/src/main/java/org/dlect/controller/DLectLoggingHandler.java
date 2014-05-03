/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import org.dlect.controller.data.DatabaseHandler;
import org.dlect.encryption.DatabaseKeyHandler;
import org.dlect.model.helper.CommonSettingNames;
import org.dlect.model.helper.ThreadLocalDateFormat;
import org.dlect.helper.ApplicationInformation;

import static com.google.common.base.Strings.padEnd;

/**
 *
 * @author lee
 */
public class DLectLoggingHandler extends StreamHandler {

    private static final ThreadLocalDateFormat OUTPUT_FORMAT = new ThreadLocalDateFormat("yyyy-MM-dd'T'HH-mm-ss.SSSS");

    public DLectLoggingHandler() {
        super(getLoggingOutputStream(), new DLectLogFormatter());
    }

    public static OutputStream getLoggingOutputStream() {
        try {
            return new FileOutputStream(DatabaseHandler.getLoggingFile());
        } catch (FileNotFoundException ex) {
            return System.out;
        }
    }

    private static class DLectLogFormatter extends Formatter {

        private static final ImmutableList<String> CENSORED_KEYWORDS = ImmutableList.of(CommonSettingNames.USERNAME,
                                                                                        CommonSettingNames.PASSWORD,
                                                                                        DatabaseKeyHandler.AES_KEY_SETTING_NAME);

        private static final int width = 120;
        private static final int mainWidth = width - 4;

        public DLectLogFormatter() {
        }

        private String formatCheckedMessage(LogRecord record) {
            String msg = formatMessage(record);
            for (String ck : CENSORED_KEYWORDS) {
                if (msg.contains(ck)) {
                    return "This message contained private information and as a result was censured to prevent "
                           + "data breaches. The censured key word was: " + ck;
                }
            }
            return msg;
        }

        @Override
        public String getHead(Handler h) {
            StringBuilder b = new StringBuilder(500);
            b.append(padCenter("DLect Log", width, '#')).append('\n');
            b.append("# ").append(padCenter("", mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padCenter("Started At " + OUTPUT_FORMAT.format(new Date()), mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padCenter("", mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("DLect Version: " + ApplicationInformation.APPLICATION_VERSION, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("Java Version: " + ApplicationInformation.JAVA_VERSION, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("OS Type: " + ApplicationInformation.OS_TYPE, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("OS Version: " + ApplicationInformation.OS_VERSON, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("OS Arch: " + ApplicationInformation.OS_ARCHITECTURE, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padCenter("", mainWidth, ' ')).append(" #\n");
            b.append(padCenter("", width, '#')).append("\n\n");
            return b.toString();
        }

        @Override
        public String getTail(Handler h) {
            StringBuilder b = new StringBuilder(500);
            b.append("\n\n");
            b.append(padCenter("", width, '#')).append('\n');
            b.append("# ").append(padCenter("", mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padCenter("Completed At " + OUTPUT_FORMAT.format(new Date()), mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padCenter("", mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("DLect Version: " + ApplicationInformation.APPLICATION_VERSION, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("Java Version: " + ApplicationInformation.JAVA_VERSION, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("OS Type: " + ApplicationInformation.OS_TYPE, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("OS Version: " + ApplicationInformation.OS_VERSON, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padEnd("OS Arch: " + ApplicationInformation.OS_ARCHITECTURE, mainWidth, ' ')).append(" #\n");
            b.append("# ").append(padCenter("", mainWidth, ' ')).append(" #\n");
            b.append(padCenter("End DLect Log", width, '#')).append('\n');
            return b.toString();
        }

        private String padCenter(String toPad, int length, char padding) {
            if (toPad.length() == 0) {
                return Strings.repeat(String.valueOf(padding), length);
            }
            int size = (length - toPad.length()) / 2;
            String pad = Strings.repeat(String.valueOf(padding), size);
            String padded = pad + toPad + pad;
            if (padded.length() == length - 1) {
                padded = padding + padded;
            }
            return padded;
        }

        private String createTrace(Throwable thrown) {
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw)) {
                pw.println();
                thrown.printStackTrace(pw);
            }
            return sw.toString();
        }

        @Override
        public String format(LogRecord record) {
            StringBuilder b = new StringBuilder(300);

            b.append('>').append(OUTPUT_FORMAT.format(record.getMillis()));
            b.append('|').append(record.getLevel());
            b.append('|').append(record.getLoggerName());
            b.append('|').append(record.getSourceClassName());
            b.append('|').append(record.getSourceMethodName());
            b.append('<').append('\n');

            b.append(padLines(formatCheckedMessage(record), "  ")).append("\n");

            Throwable thrown = record.getThrown();
            if (thrown != null) {
                b.append(padLines(createTrace(thrown), "    "));
            }
            return b.toString();
        }

        private String padLines(String msg, String padWith) {
            if (msg.isEmpty()) {
                return padWith;
            }
            String[] split = msg.split("\n");
            if (split.length == 1) {
                return padWith + split[0];
            }

            StringBuilder b = new StringBuilder(msg.length() + padWith.length() * split.length);

            for (String string : split) {
                b.append(padWith).append(string).append('\n');
            }

            return b.substring(0, b.length() - 1);
        }
    }

}
