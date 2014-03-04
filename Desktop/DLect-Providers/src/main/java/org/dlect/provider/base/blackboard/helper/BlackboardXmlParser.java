/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper;

import java.io.InputStream;
import org.dlect.exception.DLectException;
import org.dlect.provider.base.blackboard.lecture.BlackboardSubjectContentListing;
import org.dlect.provider.base.blackboard.login.LoginResponse;
import org.dlect.provider.base.blackboard.subject.BlackboardSubjectListing;

/**
 *
 * @author lee
 */
public interface BlackboardXmlParser {

    public LoginResponse parseLogin(InputStream is) throws DLectException;

    public BlackboardSubjectContentListing parseSubjectContent(InputStream contentStream) throws DLectException;

    public BlackboardSubjectListing parseSubjectListing(InputStream listingStream) throws DLectException;

}
