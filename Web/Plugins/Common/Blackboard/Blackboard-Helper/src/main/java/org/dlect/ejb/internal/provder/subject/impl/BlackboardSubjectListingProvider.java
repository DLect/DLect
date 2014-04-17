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
package org.dlect.ejb.internal.provder.subject.impl;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.dlect.ejb.internal.provder.impl.blackboard.BlackboardUniversityCustomisation;
import org.dlect.ejb.internal.provder.subject.SubjectListingProvider;
import org.dlect.except.DLectException;
import org.dlect.export.University;
import org.dlect.helpers.JAXBHelper;
import org.dlect.internal.beans.HttpConfigurationBean;
import org.dlect.internal.beans.LoginCredentialBean;
import org.dlect.internal.data.merge.PartialSemester;
import org.dlect.internal.data.merge.PartialSubject;
import org.dlect.internal.data.merge.PartialSubjectWithSemester;
import org.dlect.internal.object.ProviderSubjectData;
import org.dlect.object.ResultType;
import org.dlect.provider.common.blackboard.xml.BlackboardCourse;
import org.dlect.provider.common.blackboard.xml.BlackboardCourseListing;

import static org.dlect.except.DLectExceptionBuilder.builder;

/**
 * 
 * @author lee
 * @deprecated 
 */
@SessionScoped
@Deprecated
public class BlackboardSubjectListingProvider implements SubjectListingProvider {

    private static final long serialVersionUID = 1L;
    private static final String ENROLLMENTS_BASE_URL = "enrollments?course_type=ALL&include_grades=false&language=en_GB&v=1&ver=4.0.3";
    private static final String COURSE_MAP_BASE_URL = "courseMap?v=1&language=en_GB&ver=3.1.2&course_id=";
    private static final ResponseHandler<BlackboardCourseListing> COURSE_HANDLER
            = JAXBHelper.responseHandlerFor(BlackboardCourseListing.class);

    @Inject
    private Instance<HttpConfigurationBean> httpBeanInjector;

    @Inject
    private Instance<LoginCredentialBean> loginBeanInjector;

    @Override
    public ProviderSubjectData getSubjectListing() throws DLectException {
        try {
            HttpConfigurationBean httpBean = httpBeanInjector.get();
            LoginCredentialBean loginBean = loginBeanInjector.get();
            final University uni = loginBean.getUniversity();

            BlackboardUniversityCustomisation buc = null;//uniCustomiser.get().getCustomiserFor(uni);

            URL baseUrl = new URL(uni.getUrl());

            final URI url = new URL(baseUrl, ENROLLMENTS_BASE_URL).toURI();

            //Utils.LOG.error("Accessing URL: {}", url);

            BlackboardCourseListing bbcl = httpBean.getClient().execute(new HttpGet(url), COURSE_HANDLER);

            if (bbcl.getCourses() == null) {
                // No courses found, respond to the user with no lists.
                return new ProviderSubjectData(null);
            }

            Multimap<PartialSemester, PartialSubject> semSubMapping = HashMultimap.create();
            Map<String, PartialSemester> semesters = Maps.newHashMapWithExpectedSize(bbcl.getCourses().size());

            for (BlackboardCourse bbc : bbcl.getCourses()) {
                Optional<PartialSubjectWithSemester> opSub = buc.getSubjectDataFor(bbc.getBbid(), bbc.getName(), bbc.getCourseId(), bbc.getEnrollmentDate());

                if (!opSub.isPresent()) {
                    continue;
                }
                PartialSubjectWithSemester d = opSub.get();

                final String semCode = d.getSemesterCode();
                PartialSemester sd = semesters.get(semCode);
                if (sd == null) {
                    sd = createSemester(buc, semCode);
                    semesters.put(semCode, sd);
                }
                d.setUrl(new URL(baseUrl, COURSE_MAP_BASE_URL + bbc.getBbid()).toString());

                semSubMapping.put(sd, d);
            }

            ProviderSubjectData psd = new ProviderSubjectData(semSubMapping);

            return psd;
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(BlackboardSubjectListingProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw builder().setCause(ex).setResult(ResultType.UNIVERSITY_ERROR).setErrorMessages("Failed to access university").build();
        }
    }

    public PartialSemester createSemester(BlackboardUniversityCustomisation buc, final String semCode) {
        Optional<PartialSemester> opSem = buc.getSemesterFor(semCode);
        PartialSemester cSem;
        if (!opSem.isPresent()) {
            // Provider failed - Don't know why.
            // Just use an 'unknown' semester to save the hassle.
            cSem = getUnknownSemester(semCode);
        } else {
            cSem = opSem.get();
        }
        return cSem;
    }

    private PartialSemester getUnknownSemester(String semCode) {
        PartialSemester ps = new PartialSemester();
        ps.setSemesterCode(semCode);
        ps.setName("${Unknown-Semester}");
        return ps;
    }

}
