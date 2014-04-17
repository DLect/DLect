/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.provider;

import org.dlect.exception.DLectException;

/**
 *
 * @author lee
 */
public interface BlackboardProviderInitiliser {

    public BlackboardProviderDetails getProviderDetails() throws DLectException;

}
