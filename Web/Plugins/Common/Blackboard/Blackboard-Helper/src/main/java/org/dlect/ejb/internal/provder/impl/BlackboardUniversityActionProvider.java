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

package org.dlect.ejb.internal.provder.impl;

import org.dlect.export.University;
import org.dlect.object.UniversitySupport;
import org.dlect.provider.common.blackboard.helpers.BlackboardMobileHelper;

public abstract class BlackboardUniversityActionProvider {

    private static final long serialVersionUID = 12453L;
    private static final int PRIORITY = 0;

    public BlackboardUniversityActionProvider() {
    }

    public boolean supports(University uni) {
        return BlackboardMobileHelper.isCodeSupported(uni.getCode()) && checkSupportStyle(uni.getSupport());
    }

    protected abstract boolean checkSupportStyle(UniversitySupport support);

}
