/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.collections;

import java.util.Iterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
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
public class EventFiringIteratorTest {

    @Mock
    private Iterator<Object> delegate;
    @Mock
    private CollectionEventHelper<Object> helper;

    @InjectMocks
    private EventFiringIterator<Object> testMockIteratorObject;

    /**
     * Test of delegate method, of class EventFiringIterator.
     */
    @Test
    public void testDelegate() {
        assertSame(delegate, testMockIteratorObject.delegate());
    }

    /**
     * Test of next method, of class EventFiringIterator.
     */
    @Test
    public void testNext() {
        Object expNext = new Object();
        when(delegate.next()).thenReturn(expNext);

        Object next = testMockIteratorObject.next();

        assertEquals(expNext, next);

        verify(delegate).next();

        verifyZeroInteractions(helper);
        verifyNoMoreInteractions(delegate);
    }

    /**
     * Test of next method, of class EventFiringIterator.
     */
    @Test
    public void testNext_Exception() {
        Object expNext = new Object();
        when(delegate.next()).thenThrow(IllegalStateException.class);

        try {
            testMockIteratorObject.next();
            fail("No exception Thrown!");
        } catch (IllegalStateException e) {
            verify(delegate).next();

            verifyZeroInteractions(helper);
            verifyNoMoreInteractions(delegate);
        }
    }

    /**
     * Test of remove method, of class EventFiringIterator.
     */
    @Test
    public void testRemove() {
        Object expNext = new Object();
        when(delegate.next()).thenReturn(expNext);

        testMockIteratorObject.next();
        testMockIteratorObject.remove();

        InOrder inOrder = inOrder(delegate, helper);

        inOrder.verify(delegate).next();
        inOrder.verify(delegate).remove();
        inOrder.verify(helper).fireRemove(expNext);

        verifyNoMoreInteractions(delegate, helper);
    }

    /**
     * Test of remove method, of class EventFiringIterator.
     */
    @Test
    public void testRemove_ExceptionThrown() {
        Object expNext = new Object();
        when(delegate.next()).thenReturn(expNext);
        doThrow(IllegalStateException.class).when(delegate).remove();

        testMockIteratorObject.next();
        try {
            testMockIteratorObject.remove();
            fail("No exception Thrown!");
        } catch (IllegalStateException e) {
            verify(delegate).next();
            verify(delegate).remove();

            verifyZeroInteractions(helper);
            verifyNoMoreInteractions(delegate);
        }
    }

}
