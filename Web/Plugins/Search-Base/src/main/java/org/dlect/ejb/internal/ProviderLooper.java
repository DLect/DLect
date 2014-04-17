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

import com.google.common.base.Predicate;
import java.util.Collection;
import org.dlect.except.DLectException;
import static org.dlect.except.DLectExceptionBuilder.*;
import static org.dlect.except.CommonExceptionBuilder.*;
import org.dlect.log.Utils;
import org.dlect.object.ResultType;

/**
 *
 * @author lee
 */
public class ProviderLooper {

    public static <R, T extends Prioritisable<T>> R getFromLoop(Collection<T> ts, String exceptionPostfix, Predicate<T> shouldApply,
                                                                ApplyFunction<T, R> application) throws DLectException {
        ProviderException<DLectException> thrownInLoop = new ProviderException<>();
        for (T prov : ts) {
            if (shouldApply.apply(prov)) {
                try {
                    R r = application.apply(prov);
                    if (r == null) {
                        Utils.LOG.error("The provider {} returned null data when it said it supported it. {}", prov.getClass(), exceptionPostfix);
                    } else {
                        return r;
                    }
                } catch (DLectException ex) {
                    Utils.LOG.error("The provider {} threw an IO Exception. {}", prov.getClass(), exceptionPostfix);
                    thrownInLoop.update(rethrow("IO Error by provider " + prov.getClass() + ". " + exceptionPostfix, ex));
                }
            }
        }
        throw build(
                "No providers found for:\n"
                + "Provider Collection: " + getDebugFor(ts) + "\n"
                + "Using predicate: " + getDebugFor(shouldApply) + "\n"
                + "Using application function: " + getDebugFor(application),
                ResultType.NOT_SUPPORTED, "No support has been implemented yet.");
    }

    private ProviderLooper() {
    }

}
