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

import java.io.IOException;
import java.util.Set;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.dlect.ejb.UniversityEJBLocal;
import org.dlect.ejb.UniversitySearchEJBLocal;
import org.dlect.except.DLectException;
import org.dlect.except.NoSupportingProvidersException;
import org.dlect.object.StatusBuilder;
import org.dlect.export.University;
import org.dlect.export.wrapper.UniversityList;
import org.dlect.log.WebLogging;
import org.dlect.object.ResultType;
import org.dlect.object.UniversitySupport;
import org.glassfish.jersey.server.JSONP;

import static org.dlect.helpers.RestHelper.UNIVERSITY_CODE_PATH_MATCHING_REGEX;
import static org.dlect.except.CommonExceptionBuilder.*;

/**
 * REST Web Service
 *
 * @author lee
 */
@Path("uni")
@ManagedBean
public class UniversityREST {

    @EJB
    private UniversityEJBLocal uniEJB;
    @EJB
    private UniversitySearchEJBLocal uniSearchEJB;

    /**
     * Creates a new instance of UniversityREST
     */
    public UniversityREST() {
    }

    @GET
    @Path("/search/{q}")
    @JSONP(queryParam = "callback")
    public UniversityList searchByUrl(@PathParam("q") String term) throws DLectException {
        return doSearch(term);
    }

    @GET
    @Path("/search")
    @JSONP(queryParam = "callback")
    public UniversityList searchByParam(@QueryParam("q") String term) throws DLectException {
        return doSearch(term);
    }

    @GET
    @Path("/data/{code: " + UNIVERSITY_CODE_PATH_MATCHING_REGEX + "}")
    @JSONP(queryParam = "callback")
    public University findByPathCode(@PathParam("code") String code) throws DLectException {
        return doFindByCode(code);
    }

    @GET
    @Path("/data")
    @JSONP(queryParam = "callback")
    public University findByQueryCode(@QueryParam("code") String code) throws DLectException {
        return doFindByCode(code);
    }

    protected UniversityList doSearch(String term) throws DLectException {
        final Set<University> matches = uniSearchEJB.getUniversitiesMatching(term);
        return new UniversityList(matches);
    }

    protected University doFindByCode(String code) throws DLectException {
        University e = uniEJB.getUniversityData(code);
        if (e == null) {
            throw getIllegalReturnTypeException("University EJB failed to find university for code: " + code, uniEJB, e);
        }
        return e;
    }

}
