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

import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import org.dlect.except.DLectException;
import org.dlect.internal.data.UniversityData;
import org.dlect.log.EJBs;

@Singleton
@Asynchronous
public class AsynchronousUniversityUpdateEJB implements AsynchronousUniversityUpdateEJBLocal {

    @EJB
    private UniversityUpdateEJBLocal updateImpl;

    @Override
    public Future<UniversityData> updateUniversity(String code) {
        try {
            try {
                Thread.sleep(10000); // Test
            } catch (InterruptedException ex) {
                Logger.getLogger(AsynchronousUniversityUpdateEJB.class.getName()).log(Level.SEVERE, null, ex);
            }
            return new AsyncResult<>(updateImpl.updateUniversity(code));
        } catch (DLectException ex) {
            EJBs.LOG.error("IO Exception thrown from Asynchronous Update.", ex);
            return null;
        }
    }

}
