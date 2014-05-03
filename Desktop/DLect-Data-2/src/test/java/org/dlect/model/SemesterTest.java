/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import com.google.common.collect.Sets;
import java.util.Iterator;
import org.dlect.events.ListEvent;
import org.dlect.logging.TestLogging;
import org.dlect.model.Semester.SemesterEventID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.dlect.test.MarshalCapableTester.*;
import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class SemesterTest {

    @Test
    public void testJaxB() {
        Semester s = new Semester();
        s.setSubjects(Sets.newHashSet(new Subject("1", "Sub1"), new Subject("2", "Sub2")));
        s.setNum(1020);
        s.setLongName("Semester 1020 Long Name");
        s.setCoursePostfixName("Sem 1020. Course Prefix");
        assertEquals(2, s.getSubjects().size());

        String xml = testMarshalNonRootObject(s, Semester.class, Subject.class);

        TestLogging.LOG.error(xml);

        assertTrue(xml.contains("1020"));
        assertTrue(xml.contains(s.getLongName()));
        assertTrue(xml.contains(s.getCoursePostfixName()));

        Semester loaded = testUnmarshalNonRootObject(xml, Semester.class, Subject.class);

        assertEquals(s.getNum(), loaded.getNum());
        assertEquals(s.getLongName(), loaded.getLongName());
        assertEquals(s.getCoursePostfixName(), loaded.getCoursePostfixName());

        assertEquals(s.getSubjects().size(), loaded.getSubjects().size());

        RecordingEventListener rel = RecordingEventListener.addListener(loaded);

        Iterator<Subject> sub = loaded.getSubjects().iterator();

        loaded.setSubjects(Sets.<Subject>newHashSet());

        rel.assertEvent(ListEvent.getRemoveEvent(loaded, SemesterEventID.SUBJECT, sub.next()));
        rel.assertEvent(ListEvent.getRemoveEvent(loaded, SemesterEventID.SUBJECT, sub.next()));
        rel.noMoreEvents();
    }

}
