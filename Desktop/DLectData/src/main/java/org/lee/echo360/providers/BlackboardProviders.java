/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lee.echo360.control.ProviderListListener;
import org.lee.echo360.control.exceptions.InvalidImplemetationException;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class BlackboardProviders {

    private static transient boolean isInitilised = false;
    private static final String[] providerClasses = {
        "org.lee.echo360.providers.implementers.au.uniqld.UniQldBlackboardProvider"/*,
     "org.lee.echo360.providers.implementers.au.griffith.GriffithBlackboardProvider", 
     "org.lee.echo360.providers.implementers.au.testImpl.TestBlackboardProvider"*/

    };
        private static transient final Map<Class<? extends BlackboardProvider>, BlackboardProviderWrapper> providers = new HashMap<Class<? extends BlackboardProvider>, BlackboardProviderWrapper>();
    private static final List<ProviderListListener> providerListeners = new ArrayList<ProviderListListener>();

    public static boolean isInitilised() {
        return isInitilised;
    }

    private static void initProvider() {
        if (!isInitilised) {
            doInit();
        }
    }

    public static void removeBadProvider(BlackboardProviderWrapper provider, String msg) {
        removeBadProvider(provider, msg, null);
    }

    public static void removeBadProvider(BlackboardProviderWrapper provider, String msg, Throwable t) {
        notifyBadProvider(provider, msg, t);
        if (provider != null) {
            Class c = provider.getProviderClass();
            synchronized (providerClasses) {
                providers.remove(c);
                notifyListeners(c, provider);
            }
        }
    }

    private static void notifyBadProvider(BlackboardProviderWrapper provider, String message, Throwable t) {
        if (provider == null) {
            ExceptionReporter.reportException(new NullPointerException("Trying to remove a null provider is not allowed"));
        } else {
            String msg = "The provider \"" + provider.getProviderClass() + "\" is behaving badly.";
            msg += "\nMessage: " + message;
            ExceptionReporter.reportException(t, msg);
        }
    }

    public static void addProviderListListener(ProviderListListener lst) {
        providerListeners.add(lst);
    }

    public static void removeProviderListListener(ProviderListListener lst) {
        providerListeners.remove(lst);
    }

    /**
     *
     * @return An Unmodifiable list containing the valid providers. This will
     * track any removals that this class performs
     *
     * @see #removeBadProvider(org.lee.echo360.control.BlackboardProvider,
     * java.lang.String)
     * @see #removeBadProvider(org.lee.echo360.control.BlackboardProvider,
     * java.lang.String, java.lang.Throwable)
     */
    public static List<BlackboardProviderWrapper> getProviders() {
        initProvider();
        return new ArrayList<BlackboardProviderWrapper>(Collections.unmodifiableCollection(providers.values()));
    }

    /**
     *
     * @return An Unmodifiable list containing the valid providers. This will
     * track any removals that this class performs
     *
     * @see #removeBadProvider(org.lee.echo360.control.BlackboardProvider,
     * java.lang.String)
     * @see #removeBadProvider(org.lee.echo360.control.BlackboardProvider,
     * java.lang.String, java.lang.Throwable)
     */
    public static List<BlackboardProviderWrapper> getLazyProviders() {
        return new ArrayList<BlackboardProviderWrapper>(Collections.unmodifiableCollection(providers.values()));
    }

    private static void notifyListeners(Class rem, BlackboardProviderWrapper provider) {
        for (ProviderListListener providerListListener : providerListeners) {
            try {
                providerListListener.notifyBadProviderRemoved(rem, provider);
            } catch (Throwable t) {
                ExceptionReporter.reportException(t, "The listener: " + providerListListener + " errored");
            }
        }
    }

    private static void notifyListPopulated() {
        for (ProviderListListener providerListListener : providerListeners) {
            try {
                providerListListener.notifyListPopulated();
            } catch (Throwable t) {
                ExceptionReporter.reportException(t, "The listener: " + providerListListener + " errored");
            }
        }
    }

    /**
     * Wraps an unknown provider in a provider th
     *
     * @param p
     *
     * @return
     */
    private static BlackboardProviderWrapper wrap(BlackboardProvider p) throws InvalidImplemetationException {
        BlackboardProviderWrapper w = new BlackboardProviderWrapper(p);
        return w;
    }

    public static BlackboardProviderWrapper getByClass(Class provider) {
        initProvider();
        return providers.get(provider);
    }

    protected static synchronized void doInit() {
        if (!isInitilised) {
            for (String c : providerClasses) {
                if (c == null) {
                    ExceptionReporter.reportException("A provider class was null. Continuing over.");
                } else {
                    initProviderClass(c);
                }
            }
            isInitilised = true;
            notifyListPopulated();
        }
    }

    protected static void initProviderClass(String c) {
        ClassLoader cl = BlackboardProviders.class.getClassLoader();
        try {
            Class<?> cls = cl.loadClass(c);
            if (BlackboardProvider.class.isAssignableFrom(cls)) {
                Class<? extends BlackboardProvider> bpClass = (Class<? extends BlackboardProvider>) cls;
                try {
                    bpClass.getConstructor().setAccessible(true);
                    BlackboardProvider provider = bpClass.getConstructor().newInstance();
                    providers.put(provider.getClass(), wrap(provider));
                } catch (InvalidImplemetationException ex) {
                    ExceptionReporter.reportException(ex, "The class " + c + " caused an error.\n Ignoring this provider");
                } catch (InstantiationException ex) {
                    ExceptionReporter.reportException(ex, "The class " + c + " caused an error.\n Ignoring this provider");
                } catch (IllegalAccessException ex) {
                    ExceptionReporter.reportException(ex, "The class " + c + " caused an error.\n Ignoring this provider");
                } catch (IllegalArgumentException ex) {
                    ExceptionReporter.reportException(ex, "The class " + c + " caused an error.\n Ignoring this provider");
                } catch (InvocationTargetException ex) {
                    ExceptionReporter.reportException(ex, "The class " + c + " caused an error.\n Ignoring this provider");
                } catch (NoSuchMethodException ex) {
                    ExceptionReporter.reportException(ex, "The class " + c + " caused an error.\n Ignoring this provider");
                } catch (SecurityException ex) {
                    ExceptionReporter.reportException(ex, "The class " + c + " caused an error.\n Ignoring this provider");
                }
            } else {
                ExceptionReporter.reportException(null, "The class " + c + " is recorded in BlackboardProviders, but is not a provider. Ignoring this non-provider");
            }
        } catch (ClassNotFoundException ex) {
            ExceptionReporter.reportException(ex, "The class " + c + " is recorded in BlackboardProviders, but is not loadable. Ignoring this non-provider");
        }
    }

    private BlackboardProviders() {
    }
}
