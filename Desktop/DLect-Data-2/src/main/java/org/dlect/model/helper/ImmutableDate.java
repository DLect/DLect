/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.helper;

import java.util.Date;

/**
 *
 * @author lee
 */
@SuppressWarnings("deprecation")
public class ImmutableDate extends Date {

    public static ImmutableDate of(Date d) {
        if (d instanceof ImmutableDate) {
            return (ImmutableDate) d;
        } else {
            return new ImmutableDate(d.getTime());
        }
    }

    public static ImmutableDate of(long time) {
        return new ImmutableDate(time);
    }

    private static final long serialVersionUID = 1L;

    public ImmutableDate() {
    }

    public ImmutableDate(long date) {
        super(date);
    }

    @Override
    @Deprecated
    public void setDate(int date) {
        throw new UnsupportedOperationException("Can't modify this date");
    }

    @Override
    @Deprecated
    public void setHours(int hours) {
        throw new UnsupportedOperationException("Can't modify this date");
    }

    @Override
    @Deprecated
    public void setMinutes(int minutes) {
        throw new UnsupportedOperationException("Can't modify this date");
    }

    @Override
    @Deprecated
    public void setMonth(int month) {
        throw new UnsupportedOperationException("Can't modify this date");
    }

    @Override
    @Deprecated
    public void setSeconds(int seconds) {
        throw new UnsupportedOperationException("Can't modify this date");
    }

    @Override
    @Deprecated
    public void setTime(long time) {
        throw new UnsupportedOperationException("Can't modify this date");
    }

    @Override
    @Deprecated
    public void setYear(int year) {
        throw new UnsupportedOperationException("Can't modify this date");
    }

    @Override
    public Object clone() {
        return new ImmutableDate(this.getTime());
    }

}
