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
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.export.Semester;
import org.dlect.export.Subject;
import org.dlect.helpers.EnumSetHelper;
import org.dlect.helpers.ExportIncludes;
import org.dlect.internal.beans.LoginCredentialBean;
import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.SubjectData;
import org.dlect.object.ResultType;

import static org.dlect.db.DatabaseToExport.export;
import static org.dlect.db.DatabaseToExport.nothing;

/**
 *
 * @author lee
 */
@Stateless
public class SubjectEJB implements SubjectEJBLocal {

    @EJB
    private LoginEJBLocal login;

    @Inject
    private LoginCredentialBean loginCreds;

    @EJB
    private FindSubjectEJBLocal findSub;

    @EJB
    private FindLectureEJBLocal findLec;

    @Override
    public Collection<Subject> getAllSubjects() throws DLectException {
        checkPreconditions();

        List<SubjectData> raw = loginCreds.getListing();

        Map<SemesterData, Semester> semesters = Maps.newHashMap();
        List<Subject> ret = Lists.newArrayList();
        for (SubjectData sd : raw) {
            Semester sem = semesters.get(sd.getSemesterId());
            if (sem == null) {
                sem = export(sd.getSemesterId(), nothing());
                semesters.put(sd.getSemesterId(), sem);
            }

            Subject s = export(sd, nothing());
            s.setSemester(sem);

            ret.add(s);
        }
        return ret;
    }

    @Override
    public Subject getSubject(long code) throws DLectException {
        checkPreconditions();

        findLec.findLecturesFor(code);

        for (SubjectData sd : loginCreds.getListing()) {
            if (sd.getSubjectId().equals(code)) {
                // Export everthing
                Subject s = export(sd, EnumSetHelper.without(EnumSet.allOf(ExportIncludes.class), ExportIncludes.LECTURE_CONTENT));
                s.setSemesterByData(export(sd.getSemesterId(), nothing()));
                s.setSemesterById(null);
                return s;
            }
        }
        throw DLectExceptionBuilder.build(ResultType.INTERNAL_ERROR, "Cound not find code " + code);
    }

    protected void checkPreconditions() throws DLectException {
        login.validateLogin();

        if (!loginCreds.isListingValid()) {
            findSub.doFindSubjects();
        }

    }

}
