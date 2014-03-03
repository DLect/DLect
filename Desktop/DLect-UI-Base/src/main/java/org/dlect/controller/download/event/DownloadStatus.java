/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.controller.download.event;

import org.dlect.events.EventID;

/**
 *
 * @author lee
 */
public enum DownloadStatus implements EventID{
    STARTING, PROGRESS, COMPLETED, FAILED;

    @Override
    public Class<?> getAppliedClass() {
        return Object.class;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean isUniqueId() {
        return false;
    }
}
