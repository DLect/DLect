/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.provider;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.dlect.controller.MainController;
import org.dlect.controller.data.DatabaseHandler;
import org.dlect.controller.data.DatabaseHandler.DatabaseHandlerEventID;
import org.dlect.controller.helper.Initilisable;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.logging.ProviderLogger;
import org.dlect.model.Database;
import org.dlect.model.Database.DatabaseEventID;
import org.dlect.model.helper.CommonSettingNames;
import org.dlect.provider.WrappedProvider;
import org.dlect.provider.loader.ProviderDetail;
import org.dlect.provider.loader.ProviderLoader;

/**
 *
 * @author lee
 */
public class ProviderHelper implements EventListener, Initilisable {

    private final MainController mc;
    private final ProviderLoader pl;
    private WrappedProvider provider;
    private ProviderDetail providerDetail;

    public ProviderHelper(MainController mc) {
        this.mc = mc;
        this.pl = new ProviderLoader();
        this.mc.addListener(this, Database.class, DatabaseHandler.class);
    }

    @Override
    public void init() {
        updateProvider();
    }

    @Override
    public void processEvent(Event e) {
        // TODO(Later) add checks for username change.
        if (e.getEventID().equals(DatabaseHandlerEventID.DATABASE_LOADED)) {
            updateProvider();
        } else if (e.getEventID().equals(DatabaseEventID.SETTING)) {
            Object after = e.getAfter();
            if (after instanceof Entry) {
                Entry<?, ?> newSetting = (Entry<?, ?>) after;
                if (CommonSettingNames.PROVIDER_CODE.equals(newSetting.getKey())) {
                    updateProvider();
                }
            }
        }
    }

    public List<ProviderDetail> getProviders() {
        return pl.getProviders();
    }

    protected void updateProvider() {
        Database db = mc.getDatabaseHandler().getDatabase();
        if (db == null) {
            // Can't do a thing with it.
            provider = null;
            return;
        }
        String providerCode = db.getSetting(CommonSettingNames.PROVIDER_CODE);
        if (providerCode == null || providerCode.isEmpty()) {
            // No code, so don't try.
            provider = null;
            return;
        }
        List<ProviderDetail> detail = this.pl.getProviders();
        ProviderDetail pd = null;
        for (ProviderDetail p : detail) {
            if (p.getCode().equals(providerCode)) {
                pd = p;
                break;
            }
        }
        providerDetail = pd;
        if (pd == null) {
            provider = null;
            return;
        }

        try {
            provider = this.pl.loadProvider(pd, db, mc.getFileController());
        } catch (ExecutionException ex) {
            ProviderLogger.LOGGER.error("Failed to load provider for detail:" + pd, ex);
            provider = null;
        }
    }

    public WrappedProvider getProvider() {
        return provider;
    }

    public ProviderDetail getProviderDetail() {
        if (provider == null) {
            return null;
        }
        return providerDetail;
    }

}
