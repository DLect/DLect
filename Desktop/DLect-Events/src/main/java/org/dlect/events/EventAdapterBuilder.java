/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

/**
 *
 * @author lee
 */
public class EventAdapterBuilder {

    public static final Class<BaseEventAdapter> DEFAULT_ADAPTER_CLASS = BaseEventAdapter.class;

    private static Class<? extends EventAdapter> defaultAdapterClass = DEFAULT_ADAPTER_CLASS;

    public static EventAdapter getNewAdapter() {
        try {
            return defaultAdapterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            EventLogger.LOG.error("Failed to initilise class" + defaultAdapterClass
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
    public static void setDefaultAdapterClass(Class<? extends EventAdapter> adapter) throws IllegalArgumentException {
        if (adapter == null || adapter == DEFAULT_ADAPTER_CLASS) {
            // No point checking if I can init the default class.
            defaultAdapterClass = DEFAULT_ADAPTER_CLASS;
            return;
        }
        try {
            // Just check that we can init the class; Just to give a better error resolution by erroring early.
            adapter.newInstance();
            defaultAdapterClass = adapter;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("The class " + adapter + " does not successfully create a new instance using `Class.newInstance()`", ex);
        }

    }

    public static Class<? extends EventAdapter> getDefaultAdapterClass() {
        return defaultAdapterClass;
    }

    public static void resetDefaultAdapterClass() {
        defaultAdapterClass = DEFAULT_ADAPTER_CLASS;
    }

    private EventAdapterBuilder() {
    }

}
