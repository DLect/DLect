/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.provider;

import java.net.URI;
import java.net.URISyntaxException;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;

/**
 *
 * @author lee
 */
public class BlackboardProviderInitiliserWithBackupImpl extends BlackboardProviderInitiliserImpl {

    private final BlackboardProviderInformation providerInformation;

    /**
     *
     * @param providerInformation
     */
    public BlackboardProviderInitiliserWithBackupImpl(BlackboardProviderInformation providerInformation) {
        super(providerInformation.getProviderCode());
        this.providerInformation = providerInformation;
    }

    private BlackboardProviderDetails buildProviderFromInformation() throws DLectException {
        try {
            return new BlackboardProviderDetails(new URI(providerInformation.getBlackboardUrl()),
                                                 providerInformation.isHttpAuth(),
                                                 providerInformation.isSsl());
        } catch (URISyntaxException ex) {
            throw new DLectException(DLectExceptionCause.ILLEGAL_PROVIDER_STATE, "Failed to create URI from given string: " + providerInformation.getBlackboardUrl(), ex);
        }
    }

    @Override
    public BlackboardProviderDetails getProviderDetails() throws DLectException {
        try {
            return super.getProviderDetails();
        } catch (DLectException e) {
            if (e.getCauseCode() == DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE) {
                return buildProviderFromInformation();
            } else {
                throw new DLectException(e);
            }
        }
    }

}
