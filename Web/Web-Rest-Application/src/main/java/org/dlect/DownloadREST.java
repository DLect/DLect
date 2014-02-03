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

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.dlect.ejb.LectureEJBLocal;
import org.dlect.ejb.LoginEJBLocal;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.export.Lecture;
import org.dlect.export.LoginResult;
import org.dlect.object.ResultType;
import org.glassfish.jersey.server.JSONP;

import static com.google.common.collect.ImmutableList.of;
import static org.dlect.helpers.RestHelper.VALID_UNSIGNED_NUMBER;

/**
 *
 * @author lee
 */
@Path("/download")
@ManagedBean
public class DownloadREST {

    @Inject
    private LoginEJBLocal login;

    @Inject
    private LectureEJBLocal download;

    @GET
    @Path("/get/{lectureId: " + VALID_UNSIGNED_NUMBER + "}")
    @JSONP(queryParam = "callback")
    public Lecture getDownloadsFromPath(@PathParam("lectureId") long lectureId) throws DLectException {
        validatePreconditions();

        return download.getDownloadsFor(lectureId);
    }

    @GET
    @Path("/get")
    @JSONP(queryParam = "callback")
    public Lecture getDownloadsFromQuery(@QueryParam("lectureId") long lectureId) throws DLectException {
        validatePreconditions();

        return download.getDownloadsFor(lectureId);
    }

    public void validatePreconditions() throws DLectException {
        if (!login.validateLogin()) {
            throw CommonExceptionBuilder.getOnFailContractBreachException("LoginEJB");
        }
    }

}
