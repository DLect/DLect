/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.dlect.provider.Provider;

/**
 *
 * @author lee
 */
public final class ProviderDetail {

    private final String name;
    private final String code;
    private final Class<? extends Provider> providerClass;

    public ProviderDetail(String name, String code, Class<? extends Provider> providerClass) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
        if (code == null) {
            throw new IllegalArgumentException("Code is null");
        }
        if (providerClass == null) {
            throw new IllegalArgumentException("Provider Class is null");
        }
        validateProviderClass(providerClass);

        this.name = name;
        this.code = code;
        this.providerClass = providerClass;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Class<? extends Provider> getProviderClass() {
        return providerClass;
    }

    protected static void validateProviderClass(Class<? extends Provider> providerClass) {
        int cModifiers = providerClass.getModifiers();
        if (Modifier.isAbstract(cModifiers)) {
            throw new IllegalArgumentException("Attempting to make an abstract class a provider.");
        }
        if (Modifier.isInterface(cModifiers)) {
            throw new IllegalArgumentException("Attempting to make an interface class a provider.");
        }
        if (!Provider.class.isAssignableFrom(providerClass)) {
            throw new IllegalArgumentException("The class goven is not a provider.");
        }
        try {
            Constructor<? extends Provider> constructor = providerClass.getConstructor();
            int modifiers = constructor.getModifiers();
            if (Modifier.isAbstract(modifiers)) {

            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Failed to find constructor with no arguments", e);
        }
    }

}
