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
public class SubjectTest {

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

    @Test
    public void testSomeMethod() {
    }

}