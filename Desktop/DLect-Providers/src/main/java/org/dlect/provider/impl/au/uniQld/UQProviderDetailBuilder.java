/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.provider.impl.test;

import org.dlect.provider.loader.ProviderDetail;
import org.dlect.provider.loader.ProviderDetailBuilder;

/**
 *
 * @author lee
 */
public class TestProviderDetailBuilder implements ProviderDetailBuilder {

    @Override
    public ProviderDetail getProviderDetail() {
        return new ProviderDetail("Test Provider", "TEST", TestProvider.class);
    }
    
}
