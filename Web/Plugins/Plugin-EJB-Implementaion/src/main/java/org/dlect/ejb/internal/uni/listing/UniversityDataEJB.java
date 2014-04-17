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
package org.dlect.ejb.internal.uni.listing;

import com.google.common.base.Predicate;
import java.util.SortedSet;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.dlect.ejb.internal.ApplyFunction;
import org.dlect.ejb.internal.ProviderLooper;
import org.dlect.except.DLectException;
import org.dlect.internal.data.UniversityData;

/**
 *
 * @author lee
 */
@Singleton
public class UniversityDataEJB implements UniversityDataEJBLocal {

    // TODO

    //@Inject
    //private UniversityDataProducer producer;

    private SortedSet<UniversityDataProvider> providers;

    @Override
    public UniversityData getUniversityForCode(final String code) throws DLectException {
        return ProviderLooper.getFromLoop(getProviders(), "Code: " + code, new UDPPredicate(code), new UDPFunction(code));
    }

    private SortedSet<UniversityDataProvider> getProviders() {
        if (providers == null) {
        //    providers = producer.create();
        }
        return providers;
    }

    private static class UDPPredicate implements Predicate<UniversityDataProvider> {

        private final String code;

        public UDPPredicate(String code) {
            this.code = code;
        }

        @Override
        public boolean apply(UniversityDataProvider input) {
            return input.supports(code);
        }

    }

    private static class UDPFunction implements ApplyFunction<UniversityDataProvider, UniversityData> {

        private final String code;

        public UDPFunction(String code) {
            this.code = code;
        }

        @Override
        public UniversityData apply(UniversityDataProvider input) throws DLectException {
            return input.getUniversityForCode(code);
        }

    }

}
