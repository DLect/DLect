/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import org.dlect.encryption.DatabaseDecryptionHandler;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.model.Database;
import org.dlect.model.Semester;
import org.dlect.model.Subject;
import org.dlect.provider.helper.WrappedProviderLectureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.dlect.model.helper.CommonSettingNames.*;

/**
 *
 * @author lee
 */
public class WrappedProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(WrappedProvider.class);

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

        // TODO merge subjects.
    }

}
