/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

public abstract class FilteredRunnableWrapper<T extends Event> extends RunnableWrapper<T> {

    private final EventMatcher<T> filter;

    /**
     *
     * @param listener
     * @param filter   This filter is applied before any event threads are started or accessed. This reduces the
     *                 overheads of running the events when they will simply be ignored.
     */
    public FilteredRunnableWrapper(EventListener<T> listener, EventMatcher<T> filter) {
        super(listener);
        this.filter = filter;
    }

    @Override
    public void eventFired(T event) {
        if (filter.supports(event)) {
            super.eventFired(event);
        }
    }

    
    
    
}
