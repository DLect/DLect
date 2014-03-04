/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.login;

import java.io.IOException;
import java.io.InputStream;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.logging.ProviderLogger;
import org.dlect.provider.LoginProvider;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlParser;

/**
 *
 * @author lee
 */
public abstract class BlackboardLoginProvider implements LoginProvider {

    private final BlackboardXmlParser xmlParser;

    public BlackboardLoginProvider(BlackboardXmlParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    @Override
    public void doLogin(String username, String password) throws DLectException {
        InputStream loginIs;
        try {
            loginIs = doProviderLogin(username, password);
        } catch (IOException ex) {
            ProviderLogger.LOGGER.error("An error occured whilst connecting to the blackboard site. This is not a parse error.", ex);
            throw new DLectException(DLectExceptionCause.NO_CONNECTION, ex);
        }

        LoginResponse lr = xmlParser.parseLogin(loginIs);

        if (!"OK".equalsIgnoreCase(lr.getStatus())) {
            throw new DLectException(DLectExceptionCause.BAD_CREDENTIALS, "Login failed. The returned status was: " + lr.getStatus());
        }
    }

    public abstract InputStream doProviderLogin(String username, String password) throws IOException;

}
