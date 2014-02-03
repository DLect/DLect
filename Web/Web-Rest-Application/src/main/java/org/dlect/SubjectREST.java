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

package org.dlect;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.dlect.ejb.LoginEJBLocal;
import org.dlect.ejb.SubjectEJBLocal;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.export.Semester;
import org.dlect.export.Subject;
import org.dlect.export.wrapper.SubjectResponse;
import org.glassfish.jersey.server.JSONP;

import static org.dlect.helpers.RestHelper.VALID_UNSIGNED_NUMBER;

/**
 *
 * @author lee
 */
@Path("/subject")
@ManagedBean
public class SubjectREST {

    @Inject
    private LoginEJBLocal login;

    @Inject
    private SubjectEJBLocal subject;

    @GET
    @Path("/all")
    @JSONP(queryParam = "callback")
    public SubjectResponse doGetAllSubjects() throws DLectException {
        return getAllSubjects();
    }

    @GET
    @Path("/get/{code: " + VALID_UNSIGNED_NUMBER + "}")
    @JSONP(queryParam = "callback")
    public Subject doGetSubjectPath(@PathParam("code") long code) throws DLectException {
        validatePreconditions();
        return subject.getSubject(code);
    }

    @GET
    @Path("/get")
    @JSONP(queryParam = "callback")
    public Subject doGetSubjectQuery(@QueryParam("code") long code) throws DLectException {
        validatePreconditions();
        return subject.getSubject(code);
    }

    private SubjectResponse getAllSubjects() throws DLectException {
        validatePreconditions();

        Collection<Subject> sub = subject.getAllSubjects();

        Set<Semester> sem = Sets.newHashSet();

        for (Subject subj : sub) {
            sem.add(subj.getSemester());
        }

        return new SubjectResponse(sub, sem);

    }

    public void validatePreconditions() throws DLectException {
        if (!login.validateLogin()) {
            throw CommonExceptionBuilder.getOnFailContractBreachException("LoginEJB");
        }
    }

}
