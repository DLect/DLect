/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
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
import org.dlect.provider.DownloadProvider;
import org.dlect.provider.LectureProvider;
import org.dlect.provider.LoginProvider;
import org.dlect.provider.Provider;
import org.dlect.provider.SubjectProvider;

/**
 *
 * @author lee
 */
public class TestProvider implements Provider, LoginProvider, SubjectProvider {

    private static final ImmutableList<ImmutableSubject> EMPTY_SUBJECT_LIST = ImmutableList.of();
    private static final ImmutableList<ImmutableLecture> EMPTY_LECTURE_LIST = ImmutableList.of();
    private static final ImmutableList<ImmutableStream> EMPTY_STREAM_LIST = ImmutableList.of();
    private static final ImmutableMap<DownloadType, ImmutableLectureDownload> EMPTY_LECTURE_DOWNLOAD_MAP = ImmutableMap.of();

    private TestLectureProvider tlp;
    private TestDownloadProvider tdp;

    @Override
    public void doLogin(String username, String password) throws DLectException {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (username.equals("bad")) {
            throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, "The responce were bad");
        } else if (username.equals("no")) {
            throw new DLectException(DLectExceptionCause.NO_CONNECTION, "No connection detected");
        } else if (!username.equals("good")) {
            throw new DLectException(DLectExceptionCause.BAD_CREDENTIALS, "Bad credentials");
        }
    }

    @Override
    public LectureProvider getLectureProvider() {
        return tlp;
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
    public DownloadProvider getDownloadProvider() {
        return tdp;
    }

    @Override
    public Multimap<ImmutableSemester, ImmutableSubject> getSubjects() throws DLectException {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        tlp = new TestLectureProvider();
    }

}
