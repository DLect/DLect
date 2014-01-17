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
package org.dlect.ejb.internal.provder;

import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.dlect.ejb.internal.provder.subject.impl.BlackboardSubjectListingProvider;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.except.NoSupportingProvidersException;
import org.dlect.export.University;
import org.dlect.log.Providers;
import org.dlect.object.ResultType;

/**
 *
 * @author Lee
 */
@Singleton
public class UniversityActionEJB implements UniversityActionEJBLocal {

    @Inject
    UniversityActionClassProducer producer;

    @Inject
    Instance<InternalUniversityActionProvider> actionProvider;

    @Override
    public UniversityActionProvider getProviderFor(University university) throws DLectException {
        for (UniversityActionClassProvider prov : producer.create()) {
            if (prov.supports(new University(university))) {
                Providers.LOG.error("AP Pre");
                InternalUniversityActionProvider uap = actionProvider.get();
                uap.initClass(prov);
                return uap;
            }
        }

        throw DLectExceptionBuilder.builder().setResult(ResultType.NOT_SUPPORTED).build();
    }

}
