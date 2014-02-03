/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers;

import java.awt.Image;
import java.io.File;
import org.lee.echo360.control.exceptions.InvalidImplemetationException;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author Lee Symes
 */
public final class BlackboardProviderWrapper {

    private final BlackboardProvider key;
    private final Class<? extends BlackboardProvider> providerClass;

    public  BlackboardProviderWrapper(BlackboardProvider key) {
        providerClass = key.getClass();
        this.key = key;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.providerClass != null ? this.providerClass.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BlackboardProviderWrapper other = (BlackboardProviderWrapper) obj;
        if (this.providerClass != other.getProviderClass() && (this.providerClass == null || !this.providerClass.equals(other.getProviderClass()))) {
            return false;
        }
        return true;
    }

    public String getProviderName() throws InvalidImplemetationException {
        try {
            return key.getProviderName();
        } catch (Throwable t) {
            BlackboardProviders.removeBadProvider(this, "Wrapper: getProviderName() threw Error", t);
            return null;
        }
    }

    public Blackboard createBlackboard() throws InvalidImplemetationException {
        try {
            Blackboard b = key.createBlackboard();
            b = b == null ? new Blackboard() : b;
            b.setUsername("");
            b.setPassword("");
            return b;
        } catch (Throwable t) {
            BlackboardProviders.removeBadProvider(this, "Wrapper: createBlackboard() threw Error", t);
            return null;
        }
    }

    public Image getProviderImage() throws InvalidImplemetationException {
        try {
            return key.getProviderImage();
        } catch (Throwable t) {
            BlackboardProviders.removeBadProvider(this, "Wrapper: getProviderImage() threw Error", t);
            return null;
        }
    }

    public ActionResult getLecturesIn(Blackboard b, Subject s) throws InvalidImplemetationException {
        checkParamsNonNull(b, s);
        try {
            LectureLocator ll = key.getLectureLocator();
            if (ll != null) {
                ActionResult r = ll.getLecturesIn(b, s);
                if (r == null) {
                    return ActionResult.FATAL;
                } else {
                    return r;
                }
            } else {
                BlackboardProviders.removeBadProvider(this, "Returned Null Lecture Locator");
                throw ExceptionReporter.reportException(new InvalidImplemetationException("Null Lecture Locator"));
            }
        } catch (Throwable t) {
            BlackboardProviders.removeBadProvider(this, "Lecture Locator Threw Error");
            throw ExceptionReporter.reportException(new InvalidImplemetationException(t));
        }
    }

    public ActionResult doLogin(Blackboard b) throws InvalidImplemetationException {
        checkParamsNonNull(b);
        try {
            LoginExecuter le = key.getLoginExecuter();
            if (le != null) {
                ActionResult login = le.doLogin(b);
                return (login == null ? ActionResult.FATAL : login);
            } else {
                BlackboardProviders.removeBadProvider(this, "Returned Null Lecture Locator");
                throw ExceptionReporter.reportException(new InvalidImplemetationException("Null Lecture Locator"));
            }
        } catch (Throwable t) {
            BlackboardProviders.removeBadProvider(this, "Returned Null Lecture Locator", t);
            throw ExceptionReporter.reportException(new InvalidImplemetationException(t));
        }
    }

    public ActionResult getSubjects(Blackboard b) throws InvalidImplemetationException {
        checkParamsNonNull(b);
        try {
            SubjectLocator le = key.getSubjectLocator();
            if (le != null) {
                ActionResult s = le.getSubjects(b);
                return (s == null ? ActionResult.FATAL : s);
            } else {
                BlackboardProviders.removeBadProvider(this, "Returned Null Subject Locator");
                throw ExceptionReporter.reportException(new InvalidImplemetationException("Null Lecture Locator"));
            }
        } catch (Throwable t) {
            BlackboardProviders.removeBadProvider(this, "Subject Locator Error", t);
            throw ExceptionReporter.reportException(new InvalidImplemetationException(t));
        }
    }

    public Class getProviderClass() {
        return providerClass;
    }

    public ActionResult downloadLectureTo(Blackboard b, Subject s, Lecture l, DownloadType dt, File fls) throws InvalidImplemetationException {
        checkParamsNonNull(b, s, l, fls);
        try {
            LectureDownloader ld = key.getLectureDownloader();
            if (ld != null) {
                ActionResult r = ld.downloadLectureTo(b, s, l, dt, fls);
                return (r == null ? ActionResult.FATAL : r);
            } else {
                BlackboardProviders.removeBadProvider(this, "Returned Null Lecture Downloader");
                throw ExceptionReporter.reportException(new InvalidImplemetationException("Null Lecture Downloader"));
            }
        } catch (Throwable t) {
            BlackboardProviders.removeBadProvider(this, "Lecture Downloader Error", t);
            throw ExceptionReporter.reportException(new InvalidImplemetationException(t));
        }
    }

    private void checkParamsNonNull(Object... o) {
        for (int i = 0; i < o.length; i++) {
            Object object = o[i];
            if (object != null) {
                if (object.getClass().isArray()) {
                    if (object instanceof Object[]) {
                        try {
                            checkParamsNonNull((Object[]) object);
                        } catch (NullPointerException e) {
                            throw new IllegalArgumentException("An parameter was null. It's at index " + i, e);
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("An parameter was null. It's at index " + i);
            }
        }
    }
}
