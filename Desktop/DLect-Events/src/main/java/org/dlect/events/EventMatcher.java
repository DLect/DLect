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
public interface EventMatcher<T extends Event> {

    public boolean supports(T e);

}
