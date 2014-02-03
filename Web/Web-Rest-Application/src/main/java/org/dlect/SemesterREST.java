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

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.dlect.ejb.LoginEJBLocal;
import org.dlect.ejb.SemesterEJBLocal;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.export.Semester;
import org.dlect.export.wrapper.SemesterResponse;
import org.glassfish.jersey.server.JSONP;

import static org.dlect.helpers.RestHelper.VALID_UNSIGNED_NUMBER;

/**
 * REST Web Service
 *
 * @author lee
 */
@Path("/semester")
@ManagedBean
public class SemesterREST {

    @Inject
    private LoginEJBLocal login;

    @Inject
    private SemesterEJBLocal semester;

    @GET
    @Path("/all")
    @JSONP(queryParam = "callback")
    public SemesterResponse doGetAllSemesters() throws DLectException {
        return getAllSemesters();
    }

    @GET
    @Path("/get/{code: " + VALID_UNSIGNED_NUMBER + "}")
    @JSONP(queryParam = "callback")
    public Semester doGetSemesterForCode(@PathParam("code") long code) throws DLectException {
        return getSemester(code);
    }

    // TODO: Remove these checked exceptions and replace with correct status responces
    private SemesterResponse getAllSemesters() throws DLectException {
        if (!login.validateLogin()) {
            throw CommonExceptionBuilder.getOnFailContractBreachException("LoginEJB");
        }

        Collection<Semester> res = semester.getAllSemesters();

        return new SemesterResponse(res);
    }

    // TODO: Remove these checked exceptions and replace with correct status responces
    private Semester getSemester(long code) throws DLectException {
        if (!login.validateLogin()) {
            throw CommonExceptionBuilder.getOnFailContractBreachException("LoginEJB");
        }
        return semester.getSemester(code);
    }

}
