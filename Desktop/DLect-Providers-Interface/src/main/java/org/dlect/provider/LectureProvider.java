/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider;

import org.dlect.exception.DLectException;
import org.dlect.immutable.model.ImmutableSubject;

/**
 *
 * @author lee
 */
public interface LectureProvider {

    public ImmutableSubjectData getLecturesIn(ImmutableSubject s) throws DLectException;

}