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
package org.dlect.ejb.internal.provder.impl.blackboard.au.uq;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import org.dlect.ejb.internal.provder.impl.blackboard.au.uq.rota.builders.UQRotaStorage;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.internal.data.merge.PartialSubjectWithSemester;
import org.dlect.object.ResultType;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSubject;

import static org.dlect.helpers.RegularExpressionHelper.findGroup;

/**
 *
 * @author lee
 */
public class UQSubjectBuilder {

    private static final Pattern semsterFromID = Pattern.compile("[^_]*?_(\\d*)");
    private static final Pattern subjectCodeFromID = Pattern.compile("([A-Z]{4}[0-9]{4})");

    public static PartialSubjectWithSemester getSubjectFor(UQRotaStorage storage, String bbid, String subjectid) throws DLectException {
        try {
            String subjectCode = findGroup(subjectCodeFromID, subjectid, 1);
            int semId = Integer.parseInt(findGroup(semsterFromID, subjectid, 1), 10);

            return getSubjectFor(storage, subjectCode, semId);
        } catch (NumberFormatException ex) {
            throw DLectExceptionBuilder.build(ResultType.INTERNAL_ERROR, "Failed to parse semester from subject id: " + subjectid);
        }
    }

    public static PartialSubjectWithSemester getSubjectFor(UQRotaStorage storage, String subjectCode, int semId) throws DLectException {
        // TODO handle DLect Exceptions better.
        try {
            UQRotaSubject rotaSubject = storage.getSubject(subjectCode);

            PartialSubjectWithSemester ps = new PartialSubjectWithSemester();
            ps.setName(rotaSubject.getCode());
            ps.setDescription(rotaSubject.getName());
            ps.setSemesterCode(Integer.toString(semId));

            return ps;
        } catch (ExecutionException ex) {
            throw DLectExceptionBuilder.rethrow("Get Subject for failed", ResultType.UNIVERSITY_ERROR, ex);
        }
    }

    private UQSubjectBuilder() {
    }

}
