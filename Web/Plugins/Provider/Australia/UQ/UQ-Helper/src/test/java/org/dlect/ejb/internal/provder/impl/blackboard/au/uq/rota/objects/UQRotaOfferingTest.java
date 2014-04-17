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
package org.dlect.ejb.internal.provder.impl.blackboard.au.uq.rota.objects;

import org.dlect.plugin.provider.australia.uq.rota.UQRotaSession;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaSeries;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaGroup;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaOffering;
import java.io.ByteArrayInputStream;
import org.dlect.helpers.JAXBHelper.JaxbBindingSet;
import org.dlect.log.Stores;
import org.dlect.test.helper.DataLoader;
import org.dlect.test.helper.LoggingSetup.LoggingSetupReset;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.dlect.helpers.JAXBHelper.binding;
import static org.dlect.helpers.JAXBHelper.unmarshalFromStream;
import static org.dlect.test.helper.LoggingSetup.disableLogging;
import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
@SuppressWarnings("unchecked")
@Ignore
public class UQRotaOfferingTest {

    JaxbBindingSet<UQRotaOffering> subjectBinding = binding(UQRotaOffering.class);
    
    private LoggingSetupReset disableLogging;

    @Before
    public void initHttpConfigInstance() {
        disableLogging = disableLogging(Stores.LOG);
    }

    @After
    public void resetLoggingLevels() {
        disableLogging.reset();
    }
    @Test
    public void testLoad() throws Exception {
        String subjectXml = DataLoader.loadResource("org/dlect/ejb/internal/provder/impl/blackboard/uq/rota/objects/offering.xml");

        UQRotaOffering t = unmarshalFromStream(new ByteArrayInputStream(subjectXml.getBytes()), subjectBinding);

        assertNotNull("Subject is null", t);
        assertNotEquals("Subject code is null", t.getId(), 0);
        assertNotEquals("Subject code is empty", t.getSemId(), 0);
        
        for (UQRotaSeries s : t.getSeries()) {
            assertNotNull(s);
            assertNotNull(s.getName());
            for (UQRotaGroup g : s.getGroups()) {
                assertNotNull(g);
                assertNotNull(g.getName());
                for (UQRotaSession ses : g.getSessions()) {
                    assertNotNull(ses);
                    assertNotNull(ses.getBuilding());
                    assertNotNull(ses.getCampus());
                    assertNotNull(ses.getDates());
                    assertNotNull(ses.getRoom());
                    assertNotEquals(ses.getEndMins(), 0);
                    assertNotEquals(ses.getStartMins(), 0);
                    assertFalse(ses.getDates().isEmpty());
                }
            }
        }
        
        
    }

}
