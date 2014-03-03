/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.immutable.model;

/**
 *
 * @author lee
 */
public interface ImmutableModel<T> {

    public void copyTo(T s);
    
    public T copyToNew();
}
