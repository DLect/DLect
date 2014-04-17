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

import org.dlect.plugin.provider.australia.uq.rota.UQRotaSubject;
import org.dlect.plugin.provider.australia.uq.rota.UQRotaOffering;
import java.io.ByteArrayInputStream;
import javax.xml.bind.JAXBContext;
import org.dlect.log.Stores;
import org.dlect.test.helper.DataLoader;
import org.dlect.test.helper.LoggingSetup.LoggingSetupReset;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.dlect.helpers.JAXBHelper.*;
import static org.dlect.test.helper.LoggingSetup.disableLogging;
import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
@Ignore
public class UQRotaSubjectTest {

    
    private LoggingSetupReset disableLogging;

    @Before
    public void initHttpConfigInstance() {
        disableLogging = disableLogging(Stores.LOG);
    }

    @After
    public void resetLoggingLevels() {
        disableLogging.reset();
    }
    
    JaxbBindingSet<UQRotaSubject> subjectBinding = binding(UQRotaSubject.class);

    @Test
    public void testJaxbBinding() throws Exception {
        JAXBContext j = JAXBContext.newInstance(subjectBinding.getBindings());
        assertEquals(j.getClass(), org.eclipse.persistence.jaxb.JAXBContext.class);
        assertEquals(j.createUnmarshaller().getClass(), org.eclipse.persistence.jaxb.JAXBUnmarshaller.class);
    }

    @Test
    public void testLoad() throws Exception {
        String subjectXml = DataLoader.loadResource("org/dlect/ejb/internal/provder/impl/blackboard/uq/rota/objects/subject.xml");

        UQRotaSubject t = unmarshalFromStream(new ByteArrayInputStream(subjectXml.getBytes()), subjectBinding);

        assertNotNull("Subject is null", t);
        assertNotNull("Subject code is null", t.getCode());
        assertNotEquals("Subject code is empty", t.getCode(), "");
        assertNotNull("Subject name is null", t.getName());
        assertNotEquals("Subject name is empty", t.getName(), "");
        assertNotNull("Subject offerings is null", t.getOfferings());
        assertFalse("Subject name is empty", t.getOfferings().isEmpty());

        for (UQRotaOffering uQRotaOffering : t.getOfferings()) {
            assertNotNull("Offering", uQRotaOffering);
            assertNotEquals("Offering Id", uQRotaOffering.getId(), 0);
            assertNotNull("Offering Semester Id(" + uQRotaOffering.getSemIdString() + ")", uQRotaOffering.getSemIdString());
        }
    }

}
