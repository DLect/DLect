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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.dlect.except.DLectException;
import org.dlect.except.DLectExceptionBuilder;
import org.dlect.export.University;
import org.dlect.object.ResultType;

/**
 *
 * @author Lee
 */
@Stateless
public class UniversityActionEJB implements UniversityActionEJBLocal {

    // TODO re-write
    @Inject
    Instance<UniversityActionProvider> actionProvider;

    @Override
    @Nonnull
    public UniversityActionProvider getProviderFor(University university) throws DLectException {
        UniversityActionProvider prov = getProviderFor(university.getCode());
        if (prov == null) {
            throw DLectExceptionBuilder.builder().setResult(ResultType.NOT_SUPPORTED).build();
        } else {
            return prov;
        }
    }

    @Override
    public boolean hasProviderFor(String universityCode) throws DLectException {
        return getProviderFor(universityCode) != null;
    }

    @Nullable
    protected UniversityActionProvider getProviderFor(String code) {
        Instance<UniversityActionProvider> selected
                                           = actionProvider.select(new UniversityProviderAnnotationLiteral(code));

        if (selected == null || selected.isAmbiguous() || selected.isUnsatisfied()) {
            return null;
        } else {
            return selected.get();
        }
    }

}
