/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.lee.echo360.model.Tuple;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang3.tuple.Pair;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class MainController implements ControllerListener,
        DownloadProgressListener {

    public final ReentrantLock DOWNLOAD_LOCK_OUT = new ReentrantLock();
    public final ReentrantLock EXECUTION_LOCK_OUT = new ReentrantLock();
    private final Set<ControllerListener> controllerListeners = Sets.newHashSet();
    private final Set<DownloadProgressListener> downloadProgressListeners = Sets.newHashSet();
    private final PropertiesController propertiesController;
    private final ApplicationPropertiesController applicationPropertiesController;
    private final LoginController loginController;
    private final SubjectController subjectController;
    private final LectureController lectureController;
    private final DownloadController downloadController;
    private final long uuid;
    private ControllerAction currentAction = null;
    private ControllerAction lastSuccessfulAction = null;
    private final Set<Subject> currentlyDownloadingSubjects = Sets.newHashSet();
    private final Set<Tuple<Subject, Lecture, DownloadType>> currentlyDownloadingLectures = Sets.newHashSet();
    private static final Function<Tuple<Subject, Lecture, DownloadType>, Pair<Lecture, DownloadType>> SUBJECT_TUPLE_TO_PAIR = Tuple.tupleToPairBC();

    public MainController() {
        this.uuid = Double.doubleToRawLongBits(System.currentTimeMillis() * Math.random());
        this.propertiesController = new PropertiesController(this);
        this.applicationPropertiesController = new ApplicationPropertiesController(this);
        this.loginController = new LoginController(this);
        this.subjectController = new SubjectController(this);
        this.lectureController = new LectureController(this);
        this.downloadController = new DownloadController(this);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                PropertiesSavingController.saveProperties(MainController.this);
            }
        }));
    }

    public ApplicationPropertiesController getApplicationPropertiesController() {
        return applicationPropertiesController;
    }

    public PropertiesController getPropertiesController() {
        return propertiesController;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public SubjectController getSubjectController() {
        return subjectController;
    }

    public DownloadController getDownloadController() {
        return downloadController;
    }

    public void addControllerListener(ControllerListener l) {
        synchronized (controllerListeners) {
            controllerListeners.add(l);
        }
    }

    public void removeControllerListener(ControllerListener l) {
        synchronized (controllerListeners) {
            controllerListeners.remove(l);
        }
    }

    public void addDownloadProgressListener(DownloadProgressListener l) {
        synchronized (downloadProgressListeners) {
            downloadProgressListeners.add(l);
        }
    }

    public void removeDownloadProgressListener(DownloadProgressListener l) {
        synchronized (downloadProgressListeners) {
            downloadProgressListeners.remove(l);
        }
    }

    public LectureController getLectureController() {
        return lectureController;
    }

    public long getUUID() {
        return uuid;
    }

    @Override
    public void start(ControllerAction action) {
        EXECUTION_LOCK_OUT.lock();
        try {
            currentAction = action;
        } finally {
            EXECUTION_LOCK_OUT.unlock();
        }
        for (ControllerListener l : getControllerListeners()) {
            try {
                l.start(action);
            } catch (Throwable t) {
                ExceptionReporter.reportException(t);
            }
        }
    }

    @Override
    public void finished(ControllerAction action, ActionResult ar) {
        if (ar == null) {
            ExceptionReporter.reportException("Action Result is null. This IS A BUG - FIX IT.");
            ar = ActionResult.FAILED;
        }
        if (action == null) {
            ExceptionReporter.reportException("Controller Action is null. This IS A BUG - FIX IT.");
            return;
        }
        if (currentAction == action) {
            currentAction = null;
        }
        if (ar.isSuccess()) {
            lastSuccessfulAction = action;
        }
        for (ControllerListener l : getControllerListeners()) {
            try {
                l.finished(action, ar);
            } catch (Throwable t) {
                ExceptionReporter.reportException(t);
            }
        }
//        }
        PropertiesSavingController.saveProperties(this);
    }

    private ImmutableList<ControllerListener> getControllerListeners() {
        synchronized (controllerListeners) {
            return ImmutableList.copyOf(controllerListeners);
        }
    }

    @Override
    public void error(Throwable thrwbl) {
        for (ControllerListener l : getControllerListeners()) {
            l.error(thrwbl);
        }
        PropertiesSavingController.saveProperties(this);
    }

    @Override
    public void downloadingStarted(Subject s) {
        currentlyDownloadingSubjects.add(s);
        for (DownloadProgressListener l : getDownloadProgressListeners()) {
            l.downloadingStarted(s);
        }
    }

    @Override
    public void downloadingFinished(Subject s) {
        currentlyDownloadingSubjects.remove(s);
        for (DownloadProgressListener l : getDownloadProgressListeners()) {
            l.downloadingFinished(s);
        }
    }

    @Override
    public void downloadStarting(Subject s, Lecture l, DownloadType t) {
        DOWNLOAD_LOCK_OUT.lock();
        try {
            currentlyDownloadingLectures.add(Tuple.of(s, l, t));
        } finally {
            DOWNLOAD_LOCK_OUT.unlock();
        }
        for (DownloadProgressListener ls : getDownloadProgressListeners()) {
            ls.downloadStarting(s, l, t);
        }
    }

    private ImmutableList<DownloadProgressListener> getDownloadProgressListeners() {
        synchronized (downloadProgressListeners) {
            return ImmutableList.copyOf(downloadProgressListeners);
        }
    }

    @Override
    public void downloadCompleted(Subject s, Lecture l, DownloadType t) {
        currentlyDownloadingLectures.remove(Tuple.of(s, l, t));
        for (DownloadProgressListener ls : getDownloadProgressListeners()) {
            ls.downloadCompleted(s, l, t);
        }
    }

    @Deprecated
    public ControllerAction getLastSuccessfulAction() {
        return lastSuccessfulAction;
    }

    public ControllerAction getCurrentAction() {
        return currentAction;
    }

    public boolean hasCompleted(ControllerAction a) {
        return (lastSuccessfulAction == null) ? false : lastSuccessfulAction.compareTo(a) >= 0;
    }

    public Set<Subject> getCurrentlyDownloadingSubjects() {
        return Collections.unmodifiableSet(currentlyDownloadingSubjects);
    }

    public Set<Tuple<Subject, Lecture, DownloadType>> getSubjectLecutreDownloading() {
        return Collections.unmodifiableSet(currentlyDownloadingLectures);
    }

    public Set<Tuple<Subject, Lecture, DownloadType>> getSubjectLecutreDownloading(Subject filter) {
        return Sets.filter(currentlyDownloadingLectures, SubjectFilter.of(filter));
    }

    public Collection<Pair<Lecture, DownloadType>> getLectureDownloading(Subject filter) {
        return Collections2.transform(getSubjectLecutreDownloading(filter), SUBJECT_TUPLE_TO_PAIR);
    }

    private static class SubjectFilter implements Predicate<Tuple<Subject, Lecture, DownloadType>> {

        protected static Predicate<Tuple<Subject, Lecture, DownloadType>> of(Subject s) {
            if (s == null) {
                return Predicates.alwaysTrue();
            } else {
                return new SubjectFilter(s);
            }
        }
        private final Subject filter;

        public SubjectFilter(Subject filter) {
            this.filter = filter;
        }

        @Override
        public boolean apply(Tuple<Subject, Lecture, DownloadType> input) {
            return filter.equals(input.getA());
        }
    }
}
