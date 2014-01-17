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

import javax.inject.Singleton;
import org.dlect.ejb.internal.AbstractPrioritisable;
import org.dlect.ejb.internal.provder.UniversityActionClassProvider;
import org.dlect.ejb.internal.provder.lecture.LectureListingProvider;
import org.dlect.ejb.internal.provder.login.LoginActionProvider;
import org.dlect.ejb.internal.provder.subject.SubjectListingProvider;
import org.dlect.except.NoSupportingProvidersException;
import org.dlect.export.University;

@Singleton
public class UniversityActionClassProviderImpl {//extends AbstractPrioritisable<UniversityActionClassProvider> implements UniversityActionClassProvider {

    public UniversityActionClassProviderImpl() {
        //  super(1);
    }

    public Class<? extends LoginActionProvider> getLoginActionProvider() throws NoSupportingProvidersException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Class<? extends SubjectListingProvider> getSubjectListingProvider() throws NoSupportingProvidersException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Class<? extends LectureListingProvider> getLectureListingProvider() throws NoSupportingProvidersException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean supports(University uni) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getPriority() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
