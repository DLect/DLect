/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.listenable;

import com.google.common.base.Objects;
import org.dlect.events.Event;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventID;

public class EventBuilder<T> {

    private static final EventBuilder<Object> NO_OP_ON_FIRE = new EventBuilder<Object>() {

        @Override
        public void fire() {
        }

    };

    private final EventAdapter adapter;
    private final EventID eid;
    private final Object source;

    private boolean beforeSet = false;
    private Object before;
    private boolean afterSet = false;
    private Object after;

    private EventBuilder() {
        this(null, null, null);
    }

    public EventBuilder(Object source, EventID eid, EventAdapter adapter) {
        this.source = source;
        this.eid = eid;
        this.adapter = adapter;
    }

    public <S> EventBuilder<S> before(T before) {
        this.before = before;
        this.beforeSet = true;
        return checkBeforeAfter();
    }

    public <S> EventBuilder<S> after(T after) {
        this.after = after;
        this.afterSet = true;
        return checkBeforeAfter();
    }

    @SuppressWarnings("unchecked") // The generics is purely for easy error checking at compile time. 
    private <S> EventBuilder<S> checkBeforeAfter() {
        if (beforeSet && afterSet && Objects.equal(before, after)) {
            return (EventBuilder<S>) NO_OP_ON_FIRE;
        }
        return (EventBuilder<S>) this;
    }

    public void fire() {
        if (!beforeSet || !afterSet) {
            throw new IllegalStateException("Before or after has not been set." + stateToString());
        }
        if (!Objects.equal(before, after)) {
            Event fire = new Event(source, eid, before, after);
            adapter.fireEvent(fire);
        }
    }

    @Override
    public String toString() {
        return "EventBuilder{" + stateToString() + "}";
    }

    private String stateToString() {
        return "Event: " + eid + "; Before(" + beforeSet + "): " + before + "; After(" + afterSet + "): " + after;
    }

}
