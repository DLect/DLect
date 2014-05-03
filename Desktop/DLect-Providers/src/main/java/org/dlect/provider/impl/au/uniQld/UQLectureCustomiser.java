/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld;

import com.google.common.base.Optional;
import java.net.URI;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.model.helper.ThreadLocalDateFormat;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.lecture.BlackboardStreamProvider;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureItemParser;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureItemParserBuilder;
import org.dlect.provider.base.blackboard.lecture.plugin.impl.BlackboardInlineLectureItemParser;
import org.dlect.provider.base.blackboard.lecture.plugin.impl.BlackboardLectureCustomiser;
import org.dlect.provider.base.blackboard.lecture.plugin.impl.echo.EchoCenterLecture;
import org.dlect.provider.base.blackboard.lecture.plugin.impl.echo.EchoCenterLectureCustomiser;
import org.dlect.provider.base.blackboard.lecture.plugin.impl.echo.EchoCenterLectureProvider;
import org.dlect.provider.impl.au.uniQld.rota.UQRotaHelper;

/**
 *
 * @author lee
 */
public class UQLectureCustomiser extends BlackboardLectureItemParserBuilder implements BlackboardStreamProvider,
                                                                                       EchoCenterLectureCustomiser,
                                                                                       BlackboardLectureCustomiser {

    //private static final TimeZone UQ_TIMEZONE = TimeZone.getTimeZone("GMT+10:00");
    private static final ThreadLocalDateFormat LECTOPIA_DATE_FORMAT = new ThreadLocalDateFormat("yyyy-MM-dd hh:mm:ss");
    //private static final Pattern ROOM_NUMBER_FROM_LECTURE_TITLE = Pattern.compile("");

    private final UQRotaHelper helper;

    public UQLectureCustomiser(UQRotaHelper helper) {
        this.helper = helper;
    }

    @Override
    public List<BlackboardLectureItemParser> buildParsers(BlackboardHttpClient c) {
        return of(new EchoCenterLectureProvider(c, this),
                  new BlackboardInlineLectureItemParser(c, this));
    }

    @Override
    public Collection<ImmutableStream> getLectureStream(URI u, String title, Date lectureTime, ImmutableSemester sem, ImmutableSubject s) {
        // TODO discover room number and building.
        return helper.getStreamsFor(s.getName(), sem.getNum(), lectureTime, null, null);
    }

    @Override
    public Collection<ImmutableStream> getLectureStreamsFor(ImmutableSemester sem, ImmutableSubject s) {
        return helper.getStreamsFor(s.getName(), sem.getNum());
    }

    @Override
    public Optional<Date> getLectureTime(URI u, String title, String captureDate) {
        try {
            return Optional.of(LECTOPIA_DATE_FORMAT.parse(captureDate));
        } catch (ParseException ex) {
            return Optional.absent();
        }
    }

    @Override
    public Collection<ImmutableStream> getStreamsFor(EchoCenterLecture lecture, ImmutableSemester sem, ImmutableSubject s) {
        return helper.getStreamsFor(s.getName(), sem.getNum(), lecture.getStartTime(), null, null);
    }

}
