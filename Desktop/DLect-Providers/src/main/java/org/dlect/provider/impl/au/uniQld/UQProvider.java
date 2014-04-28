/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld;

import org.dlect.provider.base.blackboard.BlackboardBaseProvider;
import org.dlect.provider.base.blackboard.helper.provider.BlackboardProviderInformation;
import org.dlect.provider.impl.au.uniQld.rota.UQRotaHelper;
import org.dlect.provider.impl.au.uniQld.rota.UQRotaHelperImpl;

/**
 *
 * @author lee
 */
public class UQProvider extends BlackboardBaseProvider {

    private static final BlackboardProviderInformation UQ_PROVIDER_INFORMATION
                                                       = new BlackboardProviderInformation(921,
                                                                                           "https://learn.uq.edu.au/webapps/Bb-mobile-BBLEARN/",
                                                                                           false,
                                                                                           true);

    public UQProvider() {
        this(new UQRotaHelperImpl());
    }

    public UQProvider(UQRotaHelper helper) {
        this(helper, new UQLectureCustomiser(helper));
    }

    public UQProvider(UQRotaHelper helper, UQLectureCustomiser customiser) {
        super(UQ_PROVIDER_INFORMATION, new UQSubjectCustomiser(helper), customiser, customiser);
    }

}
