/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.listenable;

import org.dlect.events.EventAdapterBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class ListenableDefaultTest {

    @Test
    public void defaultAdapter() throws Exception {
        Listenable<TestListenableObject> l = new Listenable<>();

        assertEquals(l.getAdapter().getClass(), EventAdapterBuilder.DEFAULT_ADAPTER_CLASS);
    }

}
