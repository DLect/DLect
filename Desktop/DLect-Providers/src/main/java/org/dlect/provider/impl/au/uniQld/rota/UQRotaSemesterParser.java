/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.base.Optional;
import org.dlect.exception.DLectException;
import org.dlect.immutable.model.ImmutableSemester;

/**
 *
 * @author lee
 */
public interface UQRotaSemesterParser {

    public Optional<ImmutableSemester> getSemester(int semCode) throws DLectException;

}
