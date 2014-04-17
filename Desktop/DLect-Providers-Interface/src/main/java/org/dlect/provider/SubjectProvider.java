/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider;

import com.google.common.collect.Multimap;
import org.dlect.exception.DLectException;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableSubject;

/**
 *
 * @author lee
 */
public interface SubjectProvider {

    public Multimap<ImmutableSemester, ImmutableSubject> getSubjects() throws DLectException;

}
