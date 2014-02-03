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

package org.dlect.ejb.internal.provder.impl.blackboard.au.uq.rota.builders;

import com.google.common.cache.CacheLoader;
import java.net.URI;
import javax.ejb.Stateless;
import org.dlect.helpers.JAXBHelper;
import org.dlect.helpers.JAXBHelper.JaxbBindingSet;
import org.dlect.log.Customisers;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSubject;

/**
 *
 * @author lee
 */
@Stateless
public class UQRotaSubjectBuilder extends CacheLoader<String, UQRotaSubject> {

    private static final JaxbBindingSet<UQRotaSubject> subjectBinding = JAXBHelper.binding(UQRotaSubject.class);

    public UQRotaSubjectBuilder() {
        Customisers.LOG.error("UQRotaSubjectBuilder INIT");
    }

    @Override
    public UQRotaSubject load(String subjectCode) throws Exception {
        URI u = new URI("http", "rota.eait.uq.edu.au", "/course/" + subjectCode + ".xml", null);

        UQRotaSubject rotaSubject = JAXBHelper.unmarshalFromUri(u, subjectBinding);
        return rotaSubject;
    }

}
