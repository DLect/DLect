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
 * TODO(Later) replace all references of SimpleDateFormat with this implementation and remove the synchronisation.
 *
 * @see SimpleDateFormat
 * @author lee
 */
public class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {

    private final String dateFormat;
    private final TimeZone timeZone;

    public ThreadLocalDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.timeZone = TimeZone.getTimeZone("UTC");
    }

    public ThreadLocalDateFormat(String dateFormat, TimeZone formatTimeZone) {
        this.dateFormat = dateFormat;
        this.timeZone = formatTimeZone;
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

    public final String format(long date) {
        return get().format(new Date(date));
    }

    public Date parse(String source) throws ParseException {
        return get().parse(source);
    }

}
