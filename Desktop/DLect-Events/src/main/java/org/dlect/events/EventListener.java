/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

public interface EventListener {

    /**
     * Processes the event given.
     *
     * @param e The even to process.
     */
    public void processEvent(Event e);

}
