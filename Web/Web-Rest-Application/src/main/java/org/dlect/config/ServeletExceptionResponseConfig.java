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
package org.dlect.config;

import javax.annotation.Priority;
import javax.servlet.ServletException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import org.dlect.helpers.ExceptionToStatus;
import org.dlect.log.WebLogging;
import org.dlect.object.ResultType;

/**
 *
 * @author lee
 */
// Go to this one last.
@Provider
@Priority(Integer.MAX_VALUE)
public class ServeletExceptionResponseConfig implements ExceptionMapper<ServletException> {

    @Context
    private HttpHeaders headers;
    @Context
    private UriInfo uri;
    @Context
    private Request request;
    @Context
    private Providers providers;

    @Override
    public Response toResponse(ServletException e) {
        WebLogging.LOG.error("", e);
//        WebLogging.LOG.error("Exception Response Config caught", e);
//        WebLogging.LOG.error("Media Types: {}", headers.getAcceptableMediaTypes());
//        WebLogging.LOG.error("Languages  : {}", headers.getAcceptableLanguages());
//        WebLogging.LOG.error("Headers    : {}", headers.getRequestHeaders());
//        WebLogging.LOG.error("Media Type : {}", headers.getMediaType());
//        WebLogging.LOG.error("Uri        : {}", uri.getRequestUri());
//        WebLogging.LOG.error("Query Param: {}", uri.getQueryParameters());
//        WebLogging.LOG.error("Path Segs  : {}", uri.getPathSegments());
//        WebLogging.LOG.error("Path Param : {}", uri.getPathParameters());
//        WebLogging.LOG.error("Base URI   : {}", uri.getBaseUri());
//        WebLogging.LOG.error("Abs URI    : {}", uri.getAbsolutePath());
//        WebLogging.LOG.error("Method     : {}", request.getMethod());

        org.dlect.export.Status s = ExceptionToStatus.fromException(e);
        return Response.status(mapResult(s.getStatus()))
                .entity(s)
                .build();
    }

    public Status mapResult(ResultType type) {
        if (type != null) {
            switch (type) {
                case BAD_INPUT:
                    return Status.BAD_REQUEST;
                case INTERNAL_ERROR:
                case UNIVERSITY_ERROR:
                    return Status.INTERNAL_SERVER_ERROR;
                case NOT_SUPPORTED:
                    return Status.NOT_IMPLEMENTED;
                case NOT_LOGGED_IN:
                case BAD_CREDENTIALS:
                    return Status.UNAUTHORIZED;
                case OK:
                    return Status.OK;

            }
        }
        return Status.INTERNAL_SERVER_ERROR;
    }

}
