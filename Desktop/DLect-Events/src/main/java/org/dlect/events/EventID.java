/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

/**
 * This class represents a single type of event that can be fired. This class is expected to implement equality and as
 * such it is recommended that implementations are enums, as that provides equality and name internally. If a class does
 * not want to implement this using a enum then equality is defined on the combination {@link #getAppliedClass() } and
 * {@link #getName() }. The {@link #isUniqueId() } is for applications to determine if they should update their internal
 * IDs from this event.
 *
 * @author lee
 */
public interface EventID {

    /**
     * The class that this EventID is associated with.
     *
     * @return The class that this event applies to.
     */
    public Class<?> getAppliedClass();

    /**
     * An easy to read name
     *
     * @return
     */
    public String getName();

    public boolean isUniqueId();
}
