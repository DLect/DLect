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
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.dlect.except.DLectException;
import org.dlect.export.University;
import org.dlect.db.DataToDatabaseEJB;
import org.dlect.helpers.UniversityDataHelper;
import org.dlect.internal.data.UniversityData;
import org.dlect.internal.data.UniversityData_;

import static org.dlect.except.CommonExceptionBuilder.*;
import static org.dlect.db.DatabaseToExport.*;
import static org.dlect.helpers.TimeHelper.ago;
import static org.dlect.helpers.TimeHelper.in;
import static org.dlect.helpers.TimeHelper.now;

/**
 *
 * @author lee
 */
@Singleton
public class UniversityEJB implements UniversityEJBLocal {

    @EJB
    private AsynchronousUniversityUpdateEJBLocal asyncUpdate;
    @EJB
    private UniversityUpdateEJBLocal update;

    @EJB
    private DataToDatabaseEJB dbHelper;

    @Override
    public University getUniversityData(String code) throws DLectException {
        UniversityData find = dbHelper.getEqualTo(UniversityData_.code, code);
        // If there is none, or the one that is in there is older than a week. update it now.
        if (find == null || find.getTimeoutLong() < ago(7, TimeUnit.DAYS)) {
            find = update.updateUniversity(code);
            if (find == null) {
                throw getIllegalReturnTypeException("University updater return null for code " + code, update, find);
            }
        } else if (find.getTimeoutLong() < now()) {
            // Timed out some time in the past.
            // Give it 10 minutes to update the data.
            find.setTimeout(in(10, TimeUnit.MINUTES));
            asyncUpdate.updateUniversity(code);
        }
        return export(find, nothing());
    }

}
