/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @see SimpleDateFormat
 * @author lee
 */
public class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {

    private final String dateFormat;
    private final TimeZone timeZone;

    public ThreadLocalDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.timeZone = TimeZone.getTimeZone("GMT");
    }

    public ThreadLocalDateFormat(String dateFormat, TimeZone setGmtTimezone) {
        this.dateFormat = dateFormat;
        this.timeZone = setGmtTimezone;
    }

    @Override
    protected DateFormat initialValue() {
        DateFormat f = new SimpleDateFormat(dateFormat);
        f.setTimeZone(timeZone);
        return f;
    }

    public final String format(Date date) {
        return get().format(date);
    }

    public Date parse(String source) throws ParseException {
        return get().parse(source);
    }

}
