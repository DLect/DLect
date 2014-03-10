/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.provider;

/**
 *
 * @author lee
 */
public class BlackboardProviderInformation {

    private final int providerCode;
    private final String blackboardUrl;
    private final boolean httpAuth;
    private final boolean ssl;

    public BlackboardProviderInformation(int providerCode, String blackboardUrl, boolean httpAuth, boolean ssl) {
        this.providerCode = providerCode;
        this.blackboardUrl = blackboardUrl;
        this.httpAuth = httpAuth;
        this.ssl = ssl;
    }

    public int getProviderCode() {
        return providerCode;
    }

    public String getBlackboardUrl() {
        return blackboardUrl;
    }

    public boolean isHttpAuth() {
        return httpAuth;
    }

    public boolean isSsl() {
        return ssl;
    }

}
