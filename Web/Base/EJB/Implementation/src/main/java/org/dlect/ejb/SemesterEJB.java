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

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.except.UnknownInternalDLectException;
import org.dlect.export.Semester;
import org.dlect.db.DataToDatabaseEJB;
import org.dlect.db.DatabaseToExport;
import org.dlect.internal.beans.LoginCredentialBean;
import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.SubjectData;
import org.dlect.log.EJBs;
import static org.dlect.helpers.DataHelpers.*;
import org.dlect.object.ResultType;

import static org.dlect.helpers.ExportIncludes.SEMESTER;
import static org.dlect.helpers.ExportIncludes.SUBJECT;

/**
 *
 * @author lee
 */
@Stateless
public class SemesterEJB implements SemesterEJBLocal {

    @EJB
    private LoginEJBLocal login;

    @Inject
    private LoginCredentialBean loginCreds;

    @EJB
    private FindSubjectEJBLocal find;

    @Override
    public Collection<Semester> getAllSemesters() throws DLectException {
        if (!checkPreconditions()) {
            throw new UnknownInternalDLectException("Precondition check returned false but did not throw an exception.");
        }

        List<SubjectData> listing = loginCreds.getListing();

        Map<SemesterData, Semester> semMap = Maps.newHashMap();

        for (SubjectData sub : listing) {
            EJBs.LOG.error("Subject: ({}){} -> Sem:{}", sub.getSubjectId(), sub.getName(), sub.getSemesterId().getSemesterId());
            for (SubjectData sdL : sub.getSemesterId().getSubjectList()) {
                EJBs.LOG.error("\t\tSemSubj: ({}){}", sdL.getSubjectId(), sdL.getName(), sdL.getSemesterId().getSemesterId());
            }
            SemesterData semD = sub.getSemesterId();
            Semester sem = semMap.get(semD);
            if (sem == null) {
                sem = DatabaseToExport.export(semD, EnumSet.of(SEMESTER, SUBJECT));
                semMap.put(semD, sem);
            }
        }
        return wrap(semMap.values());
    }

    @Override
    public Semester getSemester(long semCode) throws DLectException {
        if (!checkPreconditions()) {
            throw CommonExceptionBuilder.getOnFailContractBreachException("Semester Check Preconditions");
        }

        SemesterData data = null;
        for (SubjectData subjectData : loginCreds.getListing()) {
            if (subjectData.getSemesterId().getSemesterId().equals(semCode)) {
                data = subjectData.getSemesterId();
                break;
            }
        }

        if (data == null) {
            throw DLectExceptionBuilder
                    .builder()
                    .setResult(ResultType.BAD_INPUT)
                    .addErrorMessages("The semester code is not found in this users listing.")
                    .build();
        } else {
            Semester s = DatabaseToExport.export(data, EnumSet.of(SEMESTER, SUBJECT));
            return s;
        }
    }

    protected boolean checkPreconditions() throws DLectException {
        if (!login.validateLogin()) {
            throw CommonExceptionBuilder.getOnFailContractBreachException("Validate Login");
        }
        if (!loginCreds.isListingValid()) {
            if (!find.doFindSubjects()) {
                throw CommonExceptionBuilder.getOnFailContractBreachException("Find Subjects");
            }
        }
        return true;
    }

}
