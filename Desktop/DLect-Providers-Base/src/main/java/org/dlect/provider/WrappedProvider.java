/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.dlect.encryption.DatabaseDecryptionHandler;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.model.Database;
import org.dlect.model.Semester;
import org.dlect.model.Subject;
import org.dlect.provider.helper.WrappedProviderLectureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.dlect.model.helper.CommonSettingNames.*;
import static org.dlect.provider.helper.WrappedProviderLectureHelper.creatingMapping;

/**
 *
 * @author lee
 */
public class WrappedProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(WrappedProvider.class);
    private static final Multimap<ImmutableLecture, ImmutableStream> EMPTY_LECTURE_STREAM_MAP = ImmutableMultimap.of();

    private final Database d;
    private final DatabaseDecryptionHandler deh;
    private final Provider p;

    public WrappedProvider(Provider p, Database d) {
        if (p == null) {
            throw new IllegalArgumentException("Provider is null");
        }
        if (d == null) {
            throw new IllegalArgumentException("Database is null");
        }
        this.p = p;
        this.d = d;
        this.deh = new DatabaseDecryptionHandler(d);
    }

    public void doLogin() throws DLectException {
        Optional<String> usr = deh.getEncryptedSetting(USERNAME);
        Optional<String> pwd = deh.getEncryptedSetting(PASSWORD);

        if (!usr.isPresent() || !pwd.isPresent()) {
            throw new DLectException(DLectExceptionCause.BAD_CREDENTIALS);
        }

        LoginProvider lp = p.getLoginProvider();

        lp.doLogin(usr.get(), pwd.get());

    }

    protected boolean findSubject(Subject s) {
        for (Semester semester : d.getSemesters()) {
            for (Subject subject : semester.getSubject()) {
                if (Objects.equal(s, subject)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void getLecturesIn(Subject s) throws DLectException {
        if (s == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
        if (!findSubject(s)) {
            throw new IllegalArgumentException("Subject is not in the configured database");
        }

        ImmutableSubject is = ImmutableSubject.from(s);
        LectureProvider lp = p.getLectureProvider();
        ImmutableSubjectData data = lp.getLecturesIn(is);

        WrappedProviderLectureHelper.mergeSubjectData(s, data);
    }

    public void getSubjects() throws DLectException {
        Multimap<ImmutableSemester, ImmutableSubject> subjects = p.getSubjectProvider().getSubjects();

        insertSemestersIntoDatabase(subjects);
    }

    private void insertSemestersIntoDatabase(Multimap<ImmutableSemester, ImmutableSubject> subjects) {
        Map<Semester, Semester> semMap = creatingMapping(d.getSemesters());
        
        
        Set<Semester> semesters = Sets.newHashSet();

        for (Entry<ImmutableSemester, Collection<ImmutableSubject>> entry : subjects.asMap().entrySet()) {
            ImmutableSemester sem = entry.getKey();
            Collection<ImmutableSubject> collection = entry.getValue();

            Semester s = sem.copyToNew();

            Semester existing = semMap.get(s);
            if (existing == null) {
                existing = s;
            } else {
                sem.copyTo(existing);
            }

            semesters.add(existing);

            insertSubjectsIntoSemester(existing, collection);
        }
        d.setSemesters(semesters);
    }

    private void insertSubjectsIntoSemester(Semester sem, Collection<ImmutableSubject> imSub) {
        Map<Subject, Subject> subMap = creatingMapping(sem.getSubject());

        Set<Subject> subjects = Sets.newHashSet();
        for (ImmutableSubject is : imSub) {
            Subject s = is.copyToNew();

            Subject existing = subMap.get(s);
            if (existing == null) {
                existing = s;
            } else {
                is.copyTo(existing);
            }

            subjects.add(existing);

            if (!is.getLectures().isEmpty() || !is.getStreams().isEmpty()) {
                WrappedProviderLectureHelper.mergeSubjectData(existing,
                                                              new ImmutableSubjectData(EMPTY_LECTURE_STREAM_MAP,
                                                                                       is.getLectures(),
                                                                                       is.getStreams()));
            }
        }
        sem.setSubject(subjects);
    }

}
