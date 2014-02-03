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
package org.dlect.ejb;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.dlect.db.DataToDatabaseEJBLocal;
import org.dlect.db.DatabaseMergerEJBLocal;
import org.dlect.ejb.internal.provder.subject.SubjectListingProvider;
import org.dlect.except.DLectException;
import org.dlect.internal.beans.LoginCredentialBean;
import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.SubjectData;
import org.dlect.internal.data.UniversityData;
import org.dlect.internal.data.merge.PartialSemester;
import org.dlect.internal.data.merge.PartialSubject;
import org.dlect.internal.data.merge.config.SemesterPartialDataConfig;
import org.dlect.internal.data.merge.config.SubjectPartialDataConfig;
import org.dlect.internal.object.ProviderSubjectData;
import org.dlect.log.EJBs;

import static org.dlect.except.CommonExceptionBuilder.*;

/**
 *
 * @author lee
 */
@Stateless
public class FindSubjectEJB implements FindSubjectEJBLocal {

    @EJB
    private DataToDatabaseEJBLocal helper;

    @Inject
    private DatabaseMergerEJBLocal merger;

    @Inject
    private LoginCredentialBean loginCreds;

    @EJB
    private LoginEJBLocal login;

    @Override
    public boolean doFindSubjects() throws DLectException {
        if (!login.validateLogin()) {
            throw getOnFailContractBreachException("Validate Login");
        }

        SubjectListingProvider subjProv = loginCreds.getProvider().getSubjectListingProvider();
        if (subjProv == null) {
            throw getInvalidObjectStateException("Subject Provider was null", loginCreds.getProvider());
        }
        UniversityData uni = helper.getFromExport(loginCreds.getUniversity());

        ProviderSubjectData subjectListing = subjProv.getSubjectListing();

        if (subjectListing == null || subjectListing.getMapping() == null) {
            throw getIllegalReturnTypeException("Provider's subject listing returned invalid data", subjProv, subjectListing);
        }

        SemesterPartialDataConfig semesterPDC = new SemesterPartialDataConfig(uni);

        List<SubjectData> databaseSubjects = Lists.newArrayList();
        Multimap<PartialSemester, PartialSubject> data = subjectListing.getMapping();

        for (Entry<PartialSemester, Collection<PartialSubject>> entry : data.asMap().entrySet()) {
            PartialSemester partialSemester = entry.getKey();
            Collection<PartialSubject> collection = entry.getValue();

            SemesterData semD = merger.mergePartialData(semesterPDC, partialSemester);

            SubjectPartialDataConfig subjectPDC = new SubjectPartialDataConfig(semD);
            for (PartialSubject ps : collection) {
                SubjectData sd = merger.mergePartialData(subjectPDC, ps);

                if (sd.getSubjectId() == null) {
                    EJBs.LOG.error("Subject ID is still null even after inserting.:\nSem{}\nPSu{}\nDSu{}", semD, ps, sd);
                }

                databaseSubjects.add(sd);
            }
        }

        loginCreds.setListing(databaseSubjects);

        return true;
    }

}
