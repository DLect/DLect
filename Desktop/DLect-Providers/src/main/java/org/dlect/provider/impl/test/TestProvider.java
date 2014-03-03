/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableLectureDownload;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.provider.ImmutableSubjectData;
import org.dlect.provider.LectureProvider;
import org.dlect.provider.LoginProvider;
import org.dlect.provider.Provider;
import org.dlect.provider.SubjectProvider;

import static java.util.concurrent.TimeUnit.DAYS;

/**
 *
 * @author lee
 */
public class TestProvider implements Provider, LoginProvider, SubjectProvider, LectureProvider {

    private static final ImmutableList<ImmutableSubject> EMPTY_SUBJECT_LIST = ImmutableList.of();
    private static final ImmutableList<ImmutableLecture> EMPTY_LECTURE_LIST = ImmutableList.of();
    private static final ImmutableList<ImmutableStream> EMPTY_STREAM_LIST = ImmutableList.of();
    private static final ImmutableMap<DownloadType, ImmutableLectureDownload> EMPTY_LECTURE_DOWNLOAD_MAP = ImmutableMap.of();

    private static final long JANUARY_1 = 0;
    private static final long SINGLE_DAY = TimeUnit.DAYS.toMillis(1);

    @Override
    public void doLogin(String username, String password) throws DLectException {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (username.equals("bad")) {
            throw new DLectException(DLectExceptionCause.INVALID_DATA_FORMAT);
        } else if (username.equals("no")) {
            throw new DLectException(DLectExceptionCause.NO_CONNECTION);
        } else if (!username.equals("good")) {
            throw new DLectException(DLectExceptionCause.BAD_CREDENTIALS);
        }
    }

    @Override
    public LectureProvider getLectureProvider() {
        return this;
    }

    @Override
    public ImmutableSubjectData getLecturesIn(ImmutableSubject s) throws DLectException {
        Multimap<ImmutableLecture, ImmutableStream> lectureStreamMapping = HashMultimap.create();
        Collection<ImmutableLecture> lectures = Lists.newArrayList();
        Collection<ImmutableStream> streams = Lists.newArrayList();
        final ImmutableStream stream = new ImmutableStream("S" + s.getId(), 1);

        streams.add(stream);

        for (int i = 0; i < 10; i++) {
            Date d = new Date(JANUARY_1 + (i * SINGLE_DAY));

//            Map<DownloadType, ImmutableDownloadType> dt
            final ImmutableLecture lec = new ImmutableLecture("C" + s.getId() + "-" + i, d, false, streams, EMPTY_LECTURE_DOWNLOAD_MAP);

            lectureStreamMapping.put(lec, stream);
            lectures.add(lec);
        }

        ImmutableSubjectData d = new ImmutableSubjectData(lectureStreamMapping, lectures, streams);
        return d;
    }

    @Override
    public LoginProvider getLoginProvider() {
        return this;
    }

    @Override
    public SubjectProvider getSubjectProvider() {
        return this;
    }

    @Override
    public Multimap<ImmutableSemester, ImmutableSubject> getSubjects() throws DLectException {
        ImmutableSemester s1 = new ImmutableSemester(1, "Semester 1", "Sem 1", EMPTY_SUBJECT_LIST);
        ImmutableSemester s2 = new ImmutableSemester(2, "Semester 2", "Sem 2", EMPTY_SUBJECT_LIST);

        ImmutableSubject subA = new ImmutableSubject("ID A1", "Subject A", EMPTY_LECTURE_LIST, EMPTY_STREAM_LIST);
        ImmutableSubject subB = new ImmutableSubject("ID B1", "Subject B", EMPTY_LECTURE_LIST, EMPTY_STREAM_LIST);
        ImmutableSubject subC = new ImmutableSubject("ID C2", "Subject C", EMPTY_LECTURE_LIST, EMPTY_STREAM_LIST);
        ImmutableSubject subD = new ImmutableSubject("ID D2", "Subject D", EMPTY_LECTURE_LIST, EMPTY_STREAM_LIST);

        Multimap<ImmutableSemester, ImmutableSubject> m = HashMultimap.create();
        m.put(s1, subA);
        m.put(s1, subB);
        m.put(s2, subC);
        m.put(s2, subD);
        return m;
    }

    @Override
    public void init() throws DLectException {
        // No Op.
    }

}
