/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update;

/**
 *
 * @author lee
 */
public interface UpdateExecutor {

    public void executeUpdate(UpdateStyle us) throws UpdateException;

}
