/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.loader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.dlect.file.FileController;
import org.dlect.helper.Conditions;
import org.dlect.model.Database;
import org.dlect.provider.Provider;
import org.dlect.provider.WrappedProvider;
import org.dlect.provider.impl.au.uniQld.UQProviderDetailBuilder;
import org.dlect.provider.impl.test.TestProviderDetailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lee
 */
public class ProviderLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderLoader.class);

    public static final List<Class<? extends ProviderDetailBuilder>> PROVIDERS = ImmutableList
            .<Class<? extends ProviderDetailBuilder>>builder()
//            .add(TestProviderDetailBuilder.class)
            .add(UQProviderDetailBuilder.class)
            .build();

    public List<ProviderDetail> getProviders() {
        List<ProviderDetail> details = Lists.newArrayList();
        for (Class<? extends ProviderDetailBuilder> c : PROVIDERS) {
            try {
                ProviderDetailBuilder i = c.newInstance();
                ProviderDetail pd = i.getProviderDetail();
                if (pd != null) {
                    details.add(pd);
                } else {
                    LOGGER.error("Null details returned from class: " + c + "; instance: " + i);
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.error("Failed to load class " + c, ex);
            }
        }
        return details;
    }

    public WrappedProvider loadProvider(ProviderDetail pd, Database d, FileController fc) throws ExecutionException {
        Conditions.checkNonNull(pd, "Provider Detail");
        Conditions.checkNonNull(d, "Database");
        Conditions.checkNonNull(fc, "File Controller");
        try {
            Provider p = pd.getProviderClass().newInstance();

            WrappedProvider wp = new WrappedProvider(p, d, fc);

            return wp;
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.error("Failed to create new provider of class " + pd.getProviderClass(), ex);
            throw new ExecutionException("Provider breached contract. Exception thrown on construction", ex);
        }
    }

}
