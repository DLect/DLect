/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider;

import org.dlect.exception.DLectException;

/**
 *
 * @author lee
 */
public interface Provider {

    public void init() throws DLectException;

    public LoginProvider getLoginProvider();

    public SubjectProvider getSubjectProvider();

    public LectureProvider getLectureProvider();

}
