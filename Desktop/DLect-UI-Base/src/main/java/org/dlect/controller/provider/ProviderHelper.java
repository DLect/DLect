/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.provider;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.dlect.controller.helper.Initilisable;
import org.dlect.controller.MainController;
import org.dlect.controller.data.DatabaseHandler;
import org.dlect.controller.data.DatabaseHandler.DatabaseHandlerEventID;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
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

    public ProviderHelper(MainController mc) {
        // TODO add checks for username change.
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
        if (e.getEventID().equals(DatabaseHandlerEventID.DATABASE_LOADED)) {
            updateProvider();
        } else if (e.getEventID().equals(DatabaseEventID.SETTING)) {
            if (e.getAfter() instanceof Entry) {
                Entry<?, ?> newSetting = (Entry<?, ?>) e.getAfter();
                if (newSetting.getKey().equals(CommonSettingNames.PROVIDER_CODE)) {
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
        if (pd == null) {
            provider = null;
            return;
        }

        try {
            provider = this.pl.loadProvider(pd, db);
        } catch (ExecutionException ex) {
            // TODO log
            provider = null;
        }
    }

    public WrappedProvider getProvider() {
        return provider;
    }

}
