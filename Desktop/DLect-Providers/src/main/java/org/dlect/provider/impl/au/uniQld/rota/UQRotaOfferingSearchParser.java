/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import org.dlect.exception.DLectException;

/**
 *
 * @author lee
 */
public interface UQRotaOfferingSearchParser {

    public int getOfferingId(String subjectCode, int semesterNum) throws DLectException;

}
