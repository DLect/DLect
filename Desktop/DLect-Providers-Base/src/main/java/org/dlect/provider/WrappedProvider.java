/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.dlect.encryption.DatabaseDecryptionHandler;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.file.FileController;
import org.dlect.immutable.model.ImmutableLectureDownload;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.model.Database;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Semester;
import org.dlect.model.Subject;
import org.dlect.provider.helper.WrappedProviderLectureHelper;
import org.dlect.provider.helper.WrappedProviderSubjectHelper;
import org.dlect.provider.objects.ImmutableSubjectData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.dlect.helper.Conditions.checkNonNull;
import static org.dlect.model.helper.CommonSettingNames.*;

/**
 *
 * @author lee
 */
public class WrappedProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(WrappedProvider.class);

    private final Database d;
    private final DatabaseDecryptionHandler deh;
    private final FileController fc;
    private final Provider p;
    private boolean hasInit = false;

    public WrappedProvider(Provider p, Database d, FileController fc) {
        checkNonNull(p, "Provider");
        checkNonNull(d, "Database");
        checkNonNull(fc, "File Controller");
        this.p = p;
        this.d = d;
        this.deh = new DatabaseDecryptionHandler(d);
        this.fc = fc;
    }

    public void doLogin() throws DLectException {
        Optional<String> usr = deh.getEncryptedSetting(USERNAME);
        Optional<String> pwd = deh.getEncryptedSetting(PASSWORD);

        if (!usr.isPresent() || !pwd.isPresent()) {
            throw new DLectException(DLectExceptionCause.BAD_CREDENTIALS);
        }

        doInit();

        LoginProvider lp = p.getLoginProvider();

        lp.doLogin(usr.get(), pwd.get());
    }

    private void ensureExists(Subject s, Lecture l, LectureDownload ld) {
        if (!s.getLectures().contains(l)) {
            throw new IllegalArgumentException("The lecture is not contained in the subject");
        }
        if (!l.getLectureDownloads().containsValue(ld)) {
            throw new IllegalArgumentException("The download is not contained in the lecture");
        }
        for (Semester semester : d.getSemesters()) {
            if (semester.getSubjects().contains(s)) {
                return;
            }
        }
        throw new IllegalArgumentException("The subject is not contained in the database.");
    }

    protected boolean findSubject(Subject s) {
        for (Semester semester : d.getSemesters()) {
            for (Subject subject : semester.getSubjects()) {
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
        if (lp == null) {
            throw new DLectException(DLectExceptionCause.PROVIDER_CONTRACT, "The provider(" + p + ") failed to return a valid LectureProvider");
        }
        ImmutableSubjectData data = lp.getLecturesIn(is);

        WrappedProviderLectureHelper.mergeSubjectData(s, data);
    }

    public void getSubjects() throws DLectException {
        Multimap<ImmutableSemester, ImmutableSubject> subjects = p.getSubjectProvider().getSubjects();

        WrappedProviderSubjectHelper.insertSemestersIntoDatabase(d, subjects);
    }

    public void doDownload(Subject s, Lecture l, LectureDownload ld) throws DLectException {
        ensureExists(s, l, ld);

        InputStream is = new BufferedInputStream(p.getDownloadProvider().getDownloadStreamFor(ImmutableLectureDownload.from(ld)));
        OutputStream os;
        try {
            os = new BufferedOutputStream(new FileOutputStream(fc.getStreamForDownload(s, l, ld)));
        } catch (IOException ex) {
            throw new DLectException(DLectExceptionCause.DISK_ERROR, "The file was not found. "
                                                                     + "This is a problem with "
                                                                     + "getFileForDownload(" + s
                                                                     + ", " + l + ", " + ld + ")", ex);
        }
        byte[] load = new byte[8192];
        int lastRead;
        int totalRead = 0;
        do {
            try {
                lastRead = is.read(load);
            } catch (IOException e) {
                throw new DLectException(DLectExceptionCause.NO_CONNECTION, "Failed to read from download stream.", e);
            }
            if (lastRead < 0) {
                break;
            }
            totalRead += lastRead;
            try {
                os.write(load, 0, lastRead);
            } catch (IOException ex) {
                throw new DLectException(DLectExceptionCause.DISK_ERROR);
            }
        } while (lastRead >= 0);
    }

    public synchronized void doInit() throws DLectException {
        if (!hasInit) {
            p.init();
            hasInit = true;
        }
    }

}
