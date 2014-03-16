/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update;

import javax.annotation.Nonnull;
import org.dlect.controller.MainController;
import org.dlect.model.Database;
import org.dlect.model.helper.CommonSettingNames;

/**
 *
 * @author lee
 */
public class UpdateCheckingHelper {

    private final MainController mc;

    private final OnlineUpdateChecker updateChecker;

    private final UpdateExecutor updateExecutor;

    public UpdateCheckingHelper(MainController mc) {
        this(mc, new OnlineUpdateCheckerImpl(), new UpdateExecutorImpl());
    }

    public UpdateCheckingHelper(MainController mc, OnlineUpdateChecker updateChecker, UpdateExecutor updateExecutor) {
        this.updateChecker = updateChecker;
        this.mc = mc;
        this.updateExecutor = updateExecutor;
    }

    public void doUpdate(@Nonnull UpdateStyle us) throws UpdateException {
        final Database db = mc.getDatabaseHandler().getDatabase();
        boolean update = updateChecker.isUpdateAvaliable(db.getSetting(CommonSettingNames.PROVIDER_CODE),
                                                         db.getSetting(CommonSettingNames.UUID),
                                                         db.getSetting(CommonSettingNames.BBID));

        if (update) {
            updateExecutor.executeUpdate(us);
        }
    }

}
