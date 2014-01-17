/*
 * This file is part of DLect. DLect is a suite of code that facilitates the downloading of lecture recordings.
 *
 * Copyright © 2014 Lee Symes.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dlect.ejb.internal.provder;

import com.google.common.collect.Lists;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Qualifier;
import org.dlect.ejb.internal.provder.lecture.LectureListingProvider;
import org.dlect.ejb.internal.provder.login.LoginActionProvider;
import org.dlect.ejb.internal.provder.subject.SubjectListingProvider;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.except.NoSupportingProvidersException;
import org.dlect.log.Utils;
import org.dlect.object.ResultType;

/**
 *
 * @author lee
 */
@SessionScoped
public class LazyUniversityActionProvider implements InternalUniversityActionProvider {

    private Class<? extends LoginActionProvider> loginActionProviderClass;
    @Inject
    private Instance<LoginActionProvider> loginActionProvider;
    private Class<? extends SubjectListingProvider> subjectListingProviderClass;
    @Inject
    private Instance<SubjectListingProvider> subjectListingProvider;
    private Class<? extends LectureListingProvider> lectureListingProviderClass;
    @Inject
    private Instance<LectureListingProvider> lectureListingProvider;

//    @Inject
//    private BlackboardSubjectListingProvider bslp;
    public LazyUniversityActionProvider() {
        System.out.println("Init");
    }

    @Override
    public void initClass(UniversityActionClassProvider prov) throws DLectException {
        this.loginActionProviderClass = prov.getLoginActionProvider();
        this.subjectListingProviderClass = prov.getSubjectListingProvider();
        this.lectureListingProviderClass = prov.getLectureListingProvider();
    }

    @Override
    public LoginActionProvider getLoginActionProvider() throws DLectException {
        try {
            return loginActionProvider.select(loginActionProviderClass).get();
        } catch (IllegalArgumentException e) {
            throw DLectExceptionBuilder.builder().setResult(ResultType.NOT_SUPPORTED).setCause(e).build();
        }
    }

    @Override

    public SubjectListingProvider getSubjectListingProvider() throws DLectException {
        try {

            Utils.LOG.error("SL: " + subjectListingProvider);
            Utils.LOG.error("\tLC: " + subjectListingProviderClass);

            List<Annotation> annotations = Lists.newArrayList();
            for (Annotation a : subjectListingProviderClass.getAnnotations()) {
                Utils.LOG.error("\tAnnot: {}; Annotated: {}", a, a.annotationType().getAnnotations());
                if (a.annotationType().isAnnotationPresent(Qualifier.class)) {
                    annotations.add(a);
                }
            }

            Instance<? extends SubjectListingProvider> is = subjectListingProvider.select(subjectListingProviderClass/*, annotations.toArray(new Annotation[0])*/);
            Utils.LOG.error("\tIS: " + is);
            Utils.LOG.error("\tUS: " + is.isUnsatisfied());
            Utils.LOG.error("\tAM: " + is.isAmbiguous());
            Utils.LOG.error("\tAM: " + is.get());

            return is.get();
        } catch (IllegalArgumentException e) {
            throw DLectExceptionBuilder.builder().setResult(ResultType.NOT_SUPPORTED).setCause(e).build();
        }
    }

    @Override
    public LectureListingProvider getLectureListingProvider() throws DLectException {
        try {
            return lectureListingProvider.select(lectureListingProviderClass).get();
        } catch (IllegalArgumentException e) {
            throw DLectExceptionBuilder.builder().setResult(ResultType.NOT_SUPPORTED).setCause(e).build();
        }
    }

}
