/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lee
 */
public class EventAdapterBuilder {

    public static final Class<BaseEventAdapter> DEFAULT_ADAPTER_CLASS = BaseEventAdapter.class;

    private static Class<? extends EventAdapter> defaultAdapterClass = DEFAULT_ADAPTER_CLASS;

    public static  EventAdapter getNewAdapter() {
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

    public static void setDefaultAdapterClass(Class<? extends EventAdapter> adapter) throws IllegalArgumentException {
        if (adapter == null || adapter == DEFAULT_ADAPTER_CLASS) { // No point checking if I can init the default class.
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

}
