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

import com.google.common.base.Optional;
import com.google.common.cache.CacheLoader;
import java.net.URI;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.dlect.helpers.JAXBHelper;
import org.dlect.helpers.JAXBHelper.JaxbBindingSet;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaOffering;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSubject;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSubjectSemesterPair;

/**
 *
 * @author lee
 */
@Stateless
public class UQRotaOfferingBuilder extends CacheLoader<UQRotaSubjectSemesterPair, Optional<UQRotaOffering>> {
    
    private static final JaxbBindingSet<UQRotaOffering> offeringBinding = JAXBHelper.binding(UQRotaOffering.class);

    @Inject
    private Instance<UQRotaStorage> storage;


    // TODO mock with power mock.
    @Override
    public Optional<UQRotaOffering> load(UQRotaSubjectSemesterPair key) throws Exception {
        UQRotaSubject sub = storage.get().getSubject(key.getSubjectCode());

        int semester = key.getSemesterCode();
        UQRotaOffering unfilledOffer = null;

        for (UQRotaOffering offer : sub.getOfferings()) {
            if (offer.getSemId() == semester) {
                unfilledOffer = offer;
                break;
            }
        }
        if (unfilledOffer == null) {
            return Optional.absent();
        }

        URI u = new URI("http", "rota.eait.uq.edu.au", "/offering/" + unfilledOffer.getId() + ".xml", null);
        return Optional.of(JAXBHelper.unmarshalFromUri(u, offeringBinding));

    }

}
