/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.dlect.exception.DLectException;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableLectureDownload;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.provider.LectureProvider;
import org.dlect.provider.objects.ImmutableSubjectData;

/**
 *
 * @author lee
 */
public class TestLectureProvider implements LectureProvider {

    private static final long JANUARY_1 = 0;
    private static final long SINGLE_DAY = TimeUnit.DAYS.toMillis(1);

    @Override
    public ImmutableSubjectData getLecturesIn(ImmutableSemester sem, ImmutableSubject s) throws DLectException {
        Multimap<ImmutableLecture, ImmutableStream> lectureStreamMapping = HashMultimap.create();
        Collection<ImmutableLecture> lectures = Lists.newArrayList();
        Collection<ImmutableStream> streams = Lists.newArrayList();
        for (int streamNum = 0; streamNum < 3; streamNum++) {
            final ImmutableStream stream = new ImmutableStream("S" + s.getId());

            streams.add(stream);

            for (int i = 0; i < 10; i++) {
                Date d = new Date(JANUARY_1 + (i * SINGLE_DAY));

                Map<DownloadType, ImmutableLectureDownload> dts = Maps.newHashMap();

                for (DownloadType dt : DownloadType.values()) {
                    for (int download = 0; download < 10; download++) {
                        ImmutableLectureDownload idt = new ImmutableLectureDownload(
                                URI.create("http://www.google.com/?q=" + download + "--" + dt),
                                dt.toString());
                        dts.put(dt, idt);
                    }
                }

                final ImmutableLecture lec = new ImmutableLecture("C" + s.getId() + "-" + i, d, false, streams, dts);

                lectureStreamMapping.put(lec, stream);
                lectures.add(lec);
            }
        }

        ImmutableSubjectData d = new ImmutableSubjectData(lectureStreamMapping, lectures, streams);
        return d;
    }
}
