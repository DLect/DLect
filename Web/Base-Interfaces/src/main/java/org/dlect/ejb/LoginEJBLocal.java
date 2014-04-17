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

import javax.ejb.Local;
import org.dlect.except.DLectException;
import org.dlect.export.LoginResult;
import org.dlect.export.Status;
import org.dlect.object.ResultType;

/**
 *
 * @author lee
 */
@Local
public interface LoginEJBLocal {

    /**
     * Attempts to login to the given university with the provided credentials.
     * This method will always return a {@linkplain LoginResult} with a
     * {@code status} of {@linkplain ResultType#OK
     *
     * @param code     The university's code to login to.
     * @param username Username credentials
     * @param password Password credentials.
     *
     * @return A boolean indicating success of the login action. This method will NEVER return false, as failure is indicated by a thrown
     *         exception.
     *
     * @throws DLectException This is an all encompassing exception that will be initialised with the required descriptive information
     */
    public boolean performLogin(String code, String username, String password) throws DLectException;

    /**
     * This method performs a check to see if credentials(if any) still allow
     * for access. This method will check if the last call to {@linkplain
     * #performLogin(org.dlect.obj.University, java.lang.String,
     * java.lang.String)} was successful and if so then check if the credentials
     * generated are still valid.
     *
     * @return {@literal true} iff the credentials stored can be directly used
     *         for access, false otherwise.
     *
     * @deprecated Use {@link #validateLogin() } as it will make an attempt to re-login if the session has timed out.
     * {@link #validateLogin() } will also throw an exception with a descriptive error message rather than
     */
    @Deprecated
    public boolean isLoggedIn();

    /**
     * This method will make a request to the provider in such a way that it
     * 'resets' the timeout counter. All this method AND check it's return value
     * before performing any processing requiring a current session.
     *
     * This method may make an attempt at using the current credentials to log
     * in again if the session has expired. If this is successful then the call
     * will return {@code true}
     *
     * @return {@code true} iff the call resulted in the session being updated
     *         and the user remaining logged in. {@code false} otherwise.
     *
     * @deprecated Use {@link #validateLogin() } as it will make an attempt to re-login if the session has timed out.
     * {@link #validateLogin() } will also throw an exception with a descriptive error message rather than
     */
    @Deprecated
    public boolean ensureLoggedIn();

    /**
     * Upon calling this method, checks are made to ensure that any further actions will be successful, if this method cannot ensure this
     * condition (E.G.&nbsp;the credentials are no longer valid) then a suitably initialised {@linkplain DLectException} is thrown.
     *
     * @return {@code true} always - as failure is indicated by a thrown exception.
     *
     * @throws DLectException On failure.
     */
    public boolean validateLogin() throws DLectException;

}
