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
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.dlect.ejb.LoginEJBLocal;
import org.dlect.except.CommonExceptionBuilder;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.export.LoginResult;
import org.dlect.object.StatusBuilder;
import org.dlect.log.WebLogging;
import org.dlect.object.ResultType;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.JSONP;

import static org.dlect.helpers.ExceptionToStatus.fromException;

/**
 * REST Web Service
 *
 * @author lee
 */
@Path("/login")
@ManagedBean
public class LoginREST {

    @Inject
    private LoginEJBLocal login;

    @POST
    @Path("/do")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JSONP(queryParam = "callback")
    public LoginResult doLoginPostCodeMultipart(
            @FormDataParam(value = "username") String username,
            @FormDataParam(value = "password") String password,
            @FormDataParam(value = "code") String code) throws DLectException {
        return performLogin(username, password, code);
    }

    @POST
    @Path("/do")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @JSONP(queryParam = "callback")
    public LoginResult doLoginPostCodeFormData(
            @FormParam(value = "username") String username,
            @FormParam(value = "password") String password,
            @FormParam(value = "code") String code) throws DLectException {
        return performLogin(username, password, code);
    }

    @GET
    @Path("/status")
    @JSONP(queryParam = "callback")
    public LoginResult checkLoginStatus() {
        try {
            if (login.validateLogin()) {
                return new LoginResult(StatusBuilder.ok());
            } else {
                WebLogging.LOG.error("", CommonExceptionBuilder.getOnFailContractBreachException("validateLogin", login));
                return new LoginResult(StatusBuilder.build(ResultType.NOT_LOGGED_IN));
            }
        } catch (DLectException ex) {
            return new LoginResult(fromException(ex));
        }

    }

    private LoginResult performLogin(String username, String password, String code) throws DLectException {
        if (code == null) {
            throw DLectExceptionBuilder.builder().setResult(ResultType.BAD_INPUT)
                    .addErrorMessages("No code given.").build();
        }
        boolean ret = login.performLogin(code, username, password);
        if (!ret) {
            throw CommonExceptionBuilder.getOnFailContractBreachException("performLogin for (code: " + code + ")", login);
        }
        return new LoginResult();
    }

}
