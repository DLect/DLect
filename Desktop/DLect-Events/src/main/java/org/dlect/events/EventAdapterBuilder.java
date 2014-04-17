/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import javax.annotation.Nonnull;

/**
 *
 * @author lee
 */
public class EventAdapterBuilder {

    /**
     * The default {@link EventAdapter} class. Currently defined to {@link BaseEventAdapter}.
     */
    public static final Class<? extends EventAdapter> DEFAULT_ADAPTER_CLASS = BaseEventAdapter.class;

    private static Class<? extends EventAdapter> eventAdapterClass = DEFAULT_ADAPTER_CLASS;

    /**
     * No-op private constructor.
     */
    private EventAdapterBuilder() {
    }

    /**
     * Creates a new adapter of the class defined by calling {@link #setEventAdapterClass(java.lang.Class) }. This class
     * may return an event adapter of a different class to he one returned by {@link #getEventAdapterClass() }.
     *
     * @return A new non-null EventAdapter.
     */
    @Nonnull
    public static EventAdapter getNewAdapter() {
        try {
            return eventAdapterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            EventLogger.LOG.error("Failed to initilise class" + eventAdapterClass
                                  + ". Returning the default implementation.", ex);
            /*
             * I havn't got a clue what went wrong; so I'll give them something
             */
            return new BaseEventAdapter();
        }
    }

    /**
     * Configures the adapter class to be used when {@link #getNewAdapter() } is called. This does not affect any
     * previous {@link EventAdapter} retrieved from {@link #getNewAdapter() }.
     *
     * @param adapter The new class to be initilised. This class will have a new instance created(through
     *                {@link Class#newInstance() adapter.newInstance() }). If creating this instance fails for any
     *                reason including the default constructor not being accessible then this method throws an
     *                {@link IllegalArgumentException} and does not set the information.
     *
     * @throws IllegalArgumentException
     */
    public static void setEventAdapterClass(Class<? extends EventAdapter> adapter) {
        if (adapter == null || adapter.equals(DEFAULT_ADAPTER_CLASS)) {
            // No point checking if I can init the default class.
            eventAdapterClass = DEFAULT_ADAPTER_CLASS;
            return;
        }
        try {
            // Just check that we can init the class; Just to give a better error resolution by erroring early.
            adapter.newInstance();
            eventAdapterClass = adapter;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("The class " + adapter + " does not successfully create a new instance using `Class.newInstance()`", ex);
        }

    }

    /**
     * Gets the current event adapter.
     *
     * @return The current event adapter.
     */
    public static Class<? extends EventAdapter> getEventAdapterClass() {
        return eventAdapterClass;
    }

    /**
     * Resets the event adapter class to be the value of {@link #DEFAULT_ADAPTER_CLASS}.
     */
    public static void resetDefaultAdapterClass() {
        eventAdapterClass = DEFAULT_ADAPTER_CLASS;
    }

}
