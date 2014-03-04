/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper;

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.logging.ProviderLogger;
import org.dlect.provider.base.blackboard.lecture.BlackboardSubjectContentListing;
import org.dlect.provider.base.blackboard.login.LoginResponse;
import org.dlect.provider.base.blackboard.subject.BlackboardSubjectListing;

public class BlackboardXmlParserImpl implements BlackboardXmlParser {

    @Override
    public LoginResponse parseLogin(InputStream is) throws DLectException {
        return unmarshalStream(is, LoginResponse.class);
    }

    @Override
    public BlackboardSubjectContentListing parseSubjectContent(InputStream is) throws DLectException {
        return unmarshalStream(is, BlackboardSubjectContentListing.class);

    }

    @Override
    public BlackboardSubjectListing parseSubjectListing(InputStream is) throws DLectException {
        return unmarshalStream(is, BlackboardSubjectListing.class);
    }

    protected <T> T unmarshalStream(InputStream is, Class<? extends T> base) throws DLectException {
        try {
            Object um = JAXBContext.newInstance(base).createUnmarshaller().unmarshal(is);
            if (base.isInstance(um)) {
                return base.cast(um);
            }
            throw new DLectException(DLectExceptionCause.INVALID_DATA_FORMAT, "Unmarshalled into invalid object: " + um);
        } catch (JAXBException ex) {
            ProviderLogger.LOGGER.error("Failed to unmarshal login responce.", ex);
            throw new DLectException(DLectExceptionCause.INVALID_DATA_FORMAT, ex);
        }
    }

}
