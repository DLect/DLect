/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control;

import org.lee.echo360.providers.BlackboardProviderWrapper;

/**
 *
 * @author lee
 */
public interface ProviderListListener {

    public void notifyListPopulated();

    public void notifyBadProviderRemoved(Class cls, BlackboardProviderWrapper provider);
}
