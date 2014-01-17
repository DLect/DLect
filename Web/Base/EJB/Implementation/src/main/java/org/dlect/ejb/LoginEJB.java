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

import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.dlect.ejb.internal.LoginImpl;
import org.dlect.ejb.internal.provder.UniversityActionEJBLocal;
import org.dlect.ejb.internal.provder.UniversityActionProvider;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.export.University;
import org.dlect.helpers.TimeHelper;
import org.dlect.internal.beans.LoginCredentialBean;
import org.dlect.object.ResultType;

import static org.dlect.except.CommonExceptionBuilder.*;
import static org.dlect.except.DLectExceptionBuilder.builder;

/**
 *
 * @author lee
 */
@Stateful
public class LoginEJB implements LoginEJBLocal {

    @Inject
    private Instance<LoginCredentialBean> loginCredentials;
    @Inject
    private LoginImpl loginImpl;
    @EJB
    private UniversityEJBLocal uniEJB;
    @EJB
    private UniversityActionEJBLocal university;

    @Override
    public boolean performLogin(String code, String username, String password) throws DLectException {
        if (code == null) {
            throw builder().setResult(ResultType.BAD_INPUT).addErrorMessages("No univeristy code given.").setMessage("Code given to performLogin was null.").build();
        }

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }

        University u = uniEJB.getUniversityData(code);
        if (u == null) {
            throw getInvalidObjectStateException("University EJB returned null for code: " + code, uniEJB);
        }

        // Will never be null.
        UniversityActionProvider prov = university.getProviderFor(u);

        if (prov == null) {
            throw getInvalidObjectStateException("University EJB returned null for code: " + code, uniEJB);
        }
        
        boolean login = loginImpl.doLoginImpl(prov, u, username, password, loginCredentials.get());

        if (!login) {
            throw getOnFailContractBreachException("Login to " + getDebugFor(prov) + "(code " + code + ")");
        }

        return true;
    }
     
    @Override
    @Deprecated
    public boolean isLoggedIn() {
        return loginCredentials.get().isValid();
    }

    @Override
    @Deprecated
    public boolean ensureLoggedIn() {
        try {
            return validateLogin();
        } catch (DLectException e) {
            // No Op.
            return false;
        }

    }

    @Override
    public boolean validateLogin() throws DLectException {
        LoginCredentialBean creds = loginCredentials.get();
        if (creds.isValid()) {
            if (creds.getLastRequestMade() < TimeHelper.ago(10, TimeUnit.MINUTES)) {
                /**
                 * If the university information has changed between the
                 * last request and this one, then we MUST use the old one
                 * as that is the information that cookies and the like are
                 * linked to.
                 */
                University uni = creds.getUniversity();

                boolean b = loginImpl.doLoginImpl(creds.getProvider(), uni, creds.getUsername(), creds.getPassword(), creds);
                if (!b) {
                    throw getOnFailContractBreachException("Do Login Impl");
                }
                return b;
            } else {
                return true;
            }
        } else {
            throw DLectExceptionBuilder.build(ResultType.NOT_LOGGED_IN, "No valid credentials were stored.");
        }
    }

}
