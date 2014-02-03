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
package org.dlect.provider.common.blackboard.helpers;

import static org.dlect.object.UniversitySupport.*;

import org.dlect.object.UniversitySupport;

/**
 *
 * @author lee
 */
public class BlackboardMobileHelper {

    /**
     * Blackboard university codes are designated to start with {@code "BBM"}
     * then followed by a positive valid {@code long} in decimal form. For
     * example, {@code "BBM109"} will indicate that this is a blackboard mobile
     * university with
     *
     * @param uniCode
     * @return
     */
    public static boolean isCodeSupported(String uniCode) {
        return uniCode.matches("BBM[0-9]+");
    }

    /**
     * Returns the client ID of the supplied code. CAUTION: This method does no
     * validity checking, use {@link #isCodeSupported(java.lang.String) } to
     * check if a code is valid.
     *
     * @param code The code to convert into a client ID string.
     * @return
     */
    public static String toClientId(String code) {
        return code.substring(3);
    }

    public static int toClientIdInt(String code) {
        return Integer.parseInt(toClientId(code));
    }

    public static String toUniversityCode(long clientId) {
        return "BBM" + Long.toString(clientId);
    }
    
    public static UniversitySupport toSupport(boolean canSslLogin, boolean httpAuth) {
        if (canSslLogin) {
            return HTTPS;
        } else if (httpAuth) {
            return HTTP_AUTH;
        } else {
            return HTTP;
        }
    }

    private BlackboardMobileHelper() {
    }

}
