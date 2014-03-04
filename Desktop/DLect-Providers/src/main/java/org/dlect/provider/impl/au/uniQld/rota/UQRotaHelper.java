/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import java.util.Date;
import java.util.Set;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;

/**
 *
 * @author lee
 */
public interface UQRotaHelper {

    public ImmutableSemester getSemester(int semCode);

    public Set<ImmutableStream> getStreamsFor(String subjectCode, int semCode);

    public Set<ImmutableStream> getStreamsFor(String subjectCode, int semCode, Date lectureTime, String lectureRoom);

}
