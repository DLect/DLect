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

package org.dlect.ejb.internal;

import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import org.dlect.ejb.internal.provder.UniversityActionProvider;
import org.dlect.ejb.internal.provder.login.LoginActionProvider;
import org.dlect.except.DLectException;
import org.dlect.export.University;
import org.dlect.internal.beans.LoginCredentialBean;

import static org.dlect.except.CommonExceptionBuilder.*;

/**
 *
 * @author Lee
 */
@Stateless
public class LoginImpl {

    public boolean doLoginImpl(@NotNull UniversityActionProvider prov, @NotNull University u, @NotNull String username,
                               @NotNull String password, @NotNull LoginCredentialBean loginCredentials) throws DLectException {
        loginCredentials.clear();

        LoginActionProvider loginProv = prov.getLoginActionProvider();
        if (loginProv == null) {
            throw getIllegalReturnTypeException("Provider gave a null login action provider", prov, loginProv);
        }

        // Always copy university.
        boolean result = loginProv.doLoginFor(u.copyOf(), username, password);

        if (!result) {
            throw getOnFailContractBreachException("doLoginFor", loginProv);
        }

        loginCredentials.updateFrom(prov, u, username, password);

        return result;
    }

}
