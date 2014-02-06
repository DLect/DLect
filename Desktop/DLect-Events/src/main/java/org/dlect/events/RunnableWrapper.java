/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

/**
 *
 * @author lee
 * @param <T>
 */
public abstract class RunnableWrapper<T extends Event> implements EventListener<T> {

    private final EventListener<T> listener;

    public RunnableWrapper(EventListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void eventFired(T event) {
        runEvent(new EventRunnable<>(listener, event));
    }

    public abstract void runEvent(Runnable r);

    private static class EventRunnable<E extends Event> implements Runnable {

        private final E event;
        private final EventListener<E> listener;

        public EventRunnable(EventListener<E> listener, E event) {
            this.listener = listener;
            this.event = event;
        }

        @Override
        public void run() {
            listener.eventFired(event);
        }
    }

}
