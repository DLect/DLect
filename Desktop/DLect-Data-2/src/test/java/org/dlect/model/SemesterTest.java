/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class SemesterTest {

    @Mock
    private Object o;

    @InjectMocks
    private Object testObject;



    @Test
    public void testExamplar() throws Exception {
        Object o = mock(Object.class);
        assertNotNull(o);
        fail();
    }

    /**
     * Test of getNum method, of class Semester.
     */
    @Test
    public void testGetNum() {
    }

    /**
     * Test of setNum method, of class Semester.
     */
    @Test
    public void testSetNum() {
    }

    /**
     * Test of getLongName method, of class Semester.
     */
    @Test
    public void testGetLongName() {
    }

    /**
     * Test of setLongName method, of class Semester.
     */
    @Test
    public void testSetLongName() {
    }

    /**
     * Test of getCoursePostfixName method, of class Semester.
     */
    @Test
    public void testGetCoursePostfixName() {
    }

    /**
     * Test of setCoursePostfixName method, of class Semester.
     */
    @Test
    public void testSetCoursePostfixName() {
    }

    /**
     * Test of getSubject method, of class Semester.
     */
    @Test
    public void testGetSubject() {
    }

    /**
     * Test of setSubject method, of class Semester.
     */
    @Test
    public void testSetSubject() {
    }

}