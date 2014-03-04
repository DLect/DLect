/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.provider.base.blackboard.BlackboardLectureCustomiser;
import org.dlect.provider.impl.au.uniQld.rota.UQRotaHelper;

/**
 *
 * @author lee
 */
public class UQLectureCustomiser implements BlackboardLectureCustomiser {

    private static final TimeZone UQ_TIMEZONE = TimeZone.getTimeZone("GMT+10:00");
    private static final DateFormat LECTOPIA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final Pattern ROOM_NUMBER_FROM_LECTURE_TITLE = Pattern.compile("");

    private final UQRotaHelper helper;

    public UQLectureCustomiser(UQRotaHelper helper) {
        this.helper = helper;
    }

    @Override
    public Collection<ImmutableStream> getLectureStream(URI u, String title, Date lectureTime, ImmutableSubject s) {

        Set<ImmutableStream> streams = helper.getStreamsFor(s.getName(), Integer.parseInt(s.getId()), lectureTime, null);
        Set<ImmutableStream> lectureStreams = Sets.newHashSet();
        for (ImmutableStream is : streams) {
            if (is.getName().startsWith("L")) {
                lectureStreams.add(is);
            }
        }
        return lectureStreams;
    }

    @Override
    public Collection<ImmutableStream> getLectureStreamsFor(ImmutableSubject s) {
        Set<ImmutableStream> streams = helper.getStreamsFor(s.getName(), Integer.parseInt(s.getId()));
        Set<ImmutableStream> lectureStreams = Sets.newHashSet();
        for (ImmutableStream is : streams) {
            if (is.getName().startsWith("L")) {
                lectureStreams.add(is);
            }
        }
        return lectureStreams;
    }

    @Override
    public Optional<Date> getLectureTime(URI u, String title, String captureDate) {
        synchronized (LECTOPIA_DATE_FORMAT) {
            try {
                // Not thread safe - sigh
                return Optional.of(LECTOPIA_DATE_FORMAT.parse(captureDate));
            } catch (ParseException ex) {
                return Optional.absent();
            }
        }
    }

}
